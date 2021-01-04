package it.unipi.lsmsdb.stocksim.client.database;

import com.datastax.oss.driver.api.core.cql.BoundStatement;
import com.datastax.oss.driver.api.core.cql.PreparedStatement;
import com.datastax.oss.driver.api.core.cql.ResultSet;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import it.unipi.lsmsdb.stocksim.client.admin.Admin;
import it.unipi.lsmsdb.stocksim.client.app.ClientUtil;
import it.unipi.lsmsdb.stocksim.client.user.User;
import it.unipi.lsmsdb.stocksim.lib.database.cassandra.CQLSessionException;
import it.unipi.lsmsdb.stocksim.lib.database.cassandra.CassandraDB;
import it.unipi.lsmsdb.stocksim.lib.database.cassandra.CassandraDBFactory;
import it.unipi.lsmsdb.stocksim.lib.database.mongoDB.MongoDB;
import it.unipi.lsmsdb.stocksim.lib.database.mongoDB.MongoDBFactory;
import it.unipi.lsmsdb.stocksim.lib.database.mongoDB.MongoServer;
import it.unipi.lsmsdb.stocksim.lib.database.mongoDB.StocksimCollection;
import it.unipi.lsmsdb.stocksim.lib.yfinance.YFAssetProfile;
import it.unipi.lsmsdb.stocksim.lib.yfinance.YFHistoricalData;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.Arrays;

import static com.mongodb.client.model.Filters.eq;

/**
 * SotckSim Client DB Manager.
 *
 * @author Marco Pinna, Rambod Rahmani, Yuri Mazzuoli.
 */
public class DBManager {
    // Cassandra DB Factory
    private final CassandraDBFactory cassandraDBFactory = CassandraDBFactory.create();

    // Cassandra DB instance
    private CassandraDB cassandraDB;

    // Mongo DB Factory
    private final MongoDBFactory mongoDBFactory = MongoDBFactory.create();

    // Mongo DB shared instance
    private MongoDB mongoDB;

    /**
     * @return Cassandra DB shared instance;
     */
    private CassandraDB getCassandraDB() {
        if (cassandraDB == null) {
            final ArrayList<String> hostnames = new ArrayList<String>(Arrays.asList("172.16.3.94", "172.16.3.95", "172.16.3.96"));
            final ArrayList<Integer> ports = new ArrayList<Integer>(Arrays.asList(9042, 9042, 9042));
            cassandraDB = cassandraDBFactory.getCassandraDB(hostnames, ports, "datacenter1");
            cassandraDB.connect();
        }

        return cassandraDB;
    }

    /**
     * Disconnects from Cassandra DB and sets reference to null.
     */
    private void disconnectCassandraDB() {
        cassandraDB = getCassandraDB().disconnect();
    }

    /**
     * @return Mongo DB shared instance;
     */
    private MongoDB getMongoDB() {
        if (mongoDB == null) {
            final MongoServer mongoServer1 = new MongoServer("172.16.3.94", 27017);
            final MongoServer mongoServer2 = new MongoServer("172.16.3.95", 27017);
            final MongoServer mongoServer3 = new MongoServer("172.16.3.96", 27017);
            final ArrayList<MongoServer> servers = new ArrayList<>(Arrays.asList(mongoServer1, mongoServer2, mongoServer3));

            mongoDB = mongoDBFactory.getMongoDB(servers, "stocksim");
            mongoDB.connect();
        }

        return mongoDB;
    }

    /**
     * Disconnects from Mongo DB and sets reference to null.
     */
    private void disconnectMongoDB() {
        mongoDB = getMongoDB().disconnect();
    }

    /**
     * Executes an admin login.
     *
     * @param admin admin to be logged in.
     *
     * @return true if the login is successful, false otherwise.
     */
    public boolean adminLogin(final Admin admin) {
        boolean ret = true;

        // get password hash
        final String hashedPwd = ClientUtil.SHA256Hash(admin.getPassword());

        // retrieve admin collection from mongodb
        final MongoCollection<Document> admins = getMongoDB().getCollection(StocksimCollection.ADMINS.getCollectionName());

        // get info for admin
        final Bson usernameFilter = eq("username", admin.getUsername());
        final Bson passwordFilter = eq("password", hashedPwd);
        final Bson loginFilter = Filters.and(usernameFilter, passwordFilter);
        final Document adminDocument = getMongoDB().findOne(loginFilter, admins);

        // set fields retrieved from db if present, otherwise login failed
        if (adminDocument != null) {
            admin.setName(adminDocument.getString("name"));
            admin.setSurname(adminDocument.getString("surname"));
        } else {
            ret = false;
        }

        // disconnect from mongodb
        disconnectMongoDB();

        return ret;
    }

    /**
     * Checks if historical and summary data is available for the given symbol.
     *
     * @param symbol the ticker symbol to be searched for.
     *
     * @return true if historical and summary data is available in the db,
     *         false otherwise.
     */
    public boolean checkTickerExists(final String symbol) throws CQLSessionException {
        boolean ret = true;

        // query symbol available historical data
        final ResultSet resultSet = getCassandraDB().query(CassandraQueryBuilder.getTickerQuery(symbol));

        // find summary data in mongodb
        final MongoCollection<Document> mongoDBStocks = getMongoDB().getCollection(StocksimCollection.STOCKS.getCollectionName());
        final Document stock = getMongoDB().findOne(Filters.eq("symbol", symbol), mongoDBStocks);

        // check if historical and summary data was found
        ret = (resultSet != null && stock != null);

        return ret;
    }

    /**
     * Creates an asset profile in mongodb for the given {@link YFAssetProfile}.
     *
     * @param yfAssetProfile the {@link YFAssetProfile} to be isnerted.
     *
     * @return true if the asset profile is added correctly, false otherwise.
     */
    public boolean createAssetProfile(final YFAssetProfile yfAssetProfile) {
        boolean ret = true;

        // retrieve stocks collection
        final MongoCollection<Document> stocks = getMongoDB().getCollection(StocksimCollection.STOCKS.getCollectionName());

        // create new location document: only add available fields
        final Document locationDocument = new Document();
        if (ClientUtil.isValidString(yfAssetProfile.getState()) && !yfAssetProfile.getState().equals("null")) {
            locationDocument.append("state", yfAssetProfile.getState());
        }
        if (ClientUtil.isValidString(yfAssetProfile.getCity()) && !yfAssetProfile.getCity().equals("null")) {
            locationDocument.append("city", yfAssetProfile.getCity());
        }
        if (ClientUtil.isValidString(yfAssetProfile.getCountry()) && !yfAssetProfile.getCountry().equals("null")) {
            locationDocument.append("country", yfAssetProfile.getCountry());
        }
        if (ClientUtil.isValidString(yfAssetProfile.getPhone()) && !yfAssetProfile.getPhone().equals("null")) {
            locationDocument.append("phone", yfAssetProfile.getPhone());
        }
        if (ClientUtil.isValidString(yfAssetProfile.getAddress()) && !yfAssetProfile.getAddress().equals("null")) {
            locationDocument.append("address", yfAssetProfile.getAddress());
        }

        // create new asset document: only add available fields
        final Document assetDocument = new Document("_id", new ObjectId());
        if (ClientUtil.isValidString(yfAssetProfile.getCurrency()) && !yfAssetProfile.getCurrency().equals("null")) {
            assetDocument.append("currency", yfAssetProfile.getCurrency());
        }
        if (ClientUtil.isValidString(yfAssetProfile.getShortName()) && !yfAssetProfile.getShortName().equals("null")) {
            assetDocument.append("shortName", yfAssetProfile.getShortName());
        }
        if (ClientUtil.isValidString(yfAssetProfile.getLongName()) && !yfAssetProfile.getLongName().equals("null")) {
            assetDocument.append("longName", yfAssetProfile.getLongName());
        }
        if (ClientUtil.isValidString(yfAssetProfile.getExchangeTimezoneName()) && !yfAssetProfile.getExchangeTimezoneName().equals("null")) {
            assetDocument.append("exchangeTimezoneName", yfAssetProfile.getExchangeTimezoneName());
        }
        if (ClientUtil.isValidString(yfAssetProfile.getExchangeTimezoneShortName()) && !yfAssetProfile.getExchangeTimezoneShortName().equals("null")) {
            assetDocument.append("exchangeTimezoneShortName", yfAssetProfile.getExchangeTimezoneShortName());
        }
        if (ClientUtil.isValidString(yfAssetProfile.getQuoteType()) && !yfAssetProfile.getQuoteType().equals("null")) {
            assetDocument.append("quoteType", yfAssetProfile.getQuoteType());
        }
        if (ClientUtil.isValidString(yfAssetProfile.getSymbol()) && !yfAssetProfile.getSymbol().equals("null")) {
            assetDocument.append("symbol", yfAssetProfile.getSymbol());
        }
        if (ClientUtil.isValidString(yfAssetProfile.getMarket()) && !yfAssetProfile.getMarket().equals("null")) {
            assetDocument.append("market", yfAssetProfile.getMarket());
        }
        if (ClientUtil.isValidString(yfAssetProfile.getLogoURL()) && !yfAssetProfile.getLogoURL().equals("null")) {
            assetDocument.append("logoURL", yfAssetProfile.getLogoURL());
        }
        if (yfAssetProfile.getMarketCap() > 0) {
            assetDocument.append("marketCap", yfAssetProfile.getMarketCap());
        }
        if (yfAssetProfile.getTrailingPE() > 0) {
            assetDocument.append("trailingPE", yfAssetProfile.getTrailingPE());
        }
        if (ClientUtil.isValidString(yfAssetProfile.getSector()) && !yfAssetProfile.getSector().equals("null")) {
            assetDocument.append("sector", yfAssetProfile.getSector());
        }
        if (ClientUtil.isValidString(yfAssetProfile.getWebsite()) && !yfAssetProfile.getWebsite().equals("null")) {
            assetDocument.append("website", yfAssetProfile.getWebsite());
        }
        if (ClientUtil.isValidString(yfAssetProfile.getIndustry()) && !yfAssetProfile.getIndustry().equals("null")) {
            assetDocument.append("industry", yfAssetProfile.getIndustry());
        }
        if (ClientUtil.isValidString(yfAssetProfile.getLongBusinessSummary()) && !yfAssetProfile.getLongBusinessSummary().equals("null")) {
            assetDocument.append("longBusinessSummary", yfAssetProfile.getLongBusinessSummary());
        }
        assetDocument.append("location", locationDocument);

        // insert the new admin document in the collection
        ret = getMongoDB().insertOne(assetDocument, stocks);

        // disconnect from mongodb
        disconnectMongoDB();

        return ret;
    }

    /**
     * Updates historical data for the given symbol.
     *
     * @param symbol the symbol to be updated.
     * @param yfHistoricalData the new historical data.
     *
     * @return true if the historical data is updated without errors, false otherwise.
     *
     * @throws CQLSessionException
     */
    public boolean updateHistoricalData(final String symbol, final ArrayList<YFHistoricalData> yfHistoricalData) throws CQLSessionException {
        boolean ret = true;

        // update historical data
        for (final YFHistoricalData historicalData : yfHistoricalData) {
            final PreparedStatement preparedStatement = getCassandraDB().prepareStatement(CassandraQueryBuilder.getUpdateInsertQuery());
            final BoundStatement bounded = preparedStatement.bind(symbol, historicalData.getDate(), (float) historicalData.getAdjClose(),
                    (float) historicalData.getClose(), (float) historicalData.getHigh(), (float) historicalData.getLow(),
                    (float) historicalData.getOpen(), (float) historicalData.getVolume());
            final ResultSet updateResultSet = getCassandraDB().execute(bounded);
        }

        // disconnect from cassandra DB
        disconnectCassandraDB();

        return ret;
    }

    /**
     * Registers an admin account for the given {@link Admin}.
     *
     * @param admin the {@link Admin} to be registered.
     *
     * @return true if the admin is added to the database without errors,
     *         false otherwise.
     */
    public boolean createAdminAccount(final Admin admin) {
        boolean ret = true;

        // retrieve admins collection
        final MongoCollection<Document> admins = getMongoDB().getCollection(StocksimCollection.ADMINS.getCollectionName());

        // create new admin document
        final Document adminDocument = new Document("_id", new ObjectId());
        adminDocument.append("username", admin.getUsername())
                .append("password", ClientUtil.SHA256Hash(admin.getPassword()))
                .append("name", admin.getName())
                .append("surname", admin.getSurname());

        // insert the new admin document in the collection
        ret = getMongoDB().insertOne(adminDocument, admins);

        // disconnect from mongodb
        disconnectMongoDB();

        return ret;
    }

    /**
     * Deletes the admin account with the given username and password.
     *
     * @param username admin account username;
     * @param password admin account password.
     *
     * @return true if the admin account is found and deleted, false otherwise.
     */
    public boolean deleteAdminAccount(final String username, final String password) {
        boolean ret = false;

        // retrieve admins collection
        final MongoCollection<Document> admins = getMongoDB().getCollection(StocksimCollection.ADMINS.getCollectionName());

        // get password hash
        final String hashedPwd = ClientUtil.SHA256Hash(password);

        // filter to find the required admin
        final Bson usernameFilter = eq("username", username);
        final Bson passwordFilter = eq("password", hashedPwd);
        final Bson loginFilter = Filters.and(usernameFilter, passwordFilter);

        // try to delete admin credentials from the database
        ret = getMongoDB().deleteOne(loginFilter, admins);

        // disconnect from mongodb
        disconnectMongoDB();

        return ret;
    }

    /**
     * Deletes the user account with the given email.
     *
     * @param email admin account email.
     *
     * @return true if the user account is found and deleted, false otherwise.
     */
    public boolean deleteUserAccount(final String email) {
        boolean ret = false;

        // retrieve admins collection
        final MongoCollection<Document> users = getMongoDB().getCollection(StocksimCollection.USERS.getCollectionName());

        // filter to find the required admin
        final Bson emailFilter = eq("email", email);

        // try to delete admin credentials from the database
        ret = getMongoDB().deleteOne(emailFilter, users);

        // disconnect from mongo db
        disconnectMongoDB();

        return ret;
    }

    /**
     * Checks if the given user account already exists.
     *
     * @param user the user to be checked.
     *
     * @return true if the given user account already exists,
     *         false otherwise.
     */
    private boolean checkUserExists(final User user) {
        boolean ret = false;

        // retrieve user collection from mongodb
        final MongoCollection<Document> users = getMongoDB().getCollection(StocksimCollection.USERS.getCollectionName());

        // check user with the same username of email already exists
        final Bson usernameFilter = eq("username", user.getUsername());
        final Bson emailFilter = eq("email", user.getEmail());
        final Bson checkFilter = Filters.or(usernameFilter, emailFilter);
        final Document userDocument = getMongoDB().findOne(checkFilter, users);

        // check if at least one user was found
        if (userDocument != null) {
            ret = true;
        }

        return ret;
    }

    /**
     * Executes a user sign up.
     *
     * @param user user to be signed up.
     *
     * @return true if the sign up is successful, false otherwise.
     */
    public boolean userRegister(final User user) {
        boolean ret = true;

        // check if a user with the given credentials already exists
        ret = checkUserExists(user);
        if (ret) {
            return false;
        }

        // get password hash
        final String hashedPwd = ClientUtil.SHA256Hash(user.getPassword());

        // retrieve admin collection from mongodb
        final MongoCollection<Document> users = getMongoDB().getCollection(StocksimCollection.USERS.getCollectionName());

        // create new user document for mongo db
        final Document userDocument = new Document("_id", new ObjectId());
        userDocument.append("name", user.getName());
        userDocument.append("surname", user.getSurname());
        userDocument.append("email", user.getEmail());
        userDocument.append("username", user.getUsername());
        userDocument.append("password", hashedPwd);

        // insert the new admin document in the collection
        ret = getMongoDB().insertOne(userDocument, users);

        // disconnect from mongodb
        disconnectMongoDB();

        return ret;
    }

    /**
     * Executes a user login.
     *
     * @param user user to be logged in.
     *
     * @return true if the login is successful, false otherwise.
     */
    public boolean userLogin(final User user) {
        boolean ret = true;

        // get password hash
        final String hashedPwd = ClientUtil.SHA256Hash(user.getPassword());

        // retrieve user collection from mongodb
        final MongoCollection<Document> users = getMongoDB().getCollection(StocksimCollection.USERS.getCollectionName());

        // get info for user
        final Bson usernameFilter = eq("username", user.getUsername());
        final Bson passwordFilter = eq("password", hashedPwd);
        final Bson loginFilter = Filters.and(usernameFilter, passwordFilter);
        final Document userDocument = getMongoDB().findOne(loginFilter, users);

        // set fields retrieved from db if present, otherwise login failed
        if (userDocument != null) {
            user.setName(userDocument.getString("name"));
            user.setSurname(userDocument.getString("surname"));
        } else {
            ret = false;
        }

        // disconnect from mongodb
        disconnectMongoDB();

        return ret;
    }

    /**
     * Searches both Cassandra DB and Mongo DB for the given ticker symbol.
     *
     * @param symbol the ticker symbol to be searched for.
     *
     * @return the retrieved {@link Stock}, null otherwise.
     */
    public Stock searchStock(final String symbol) throws CQLSessionException {
        if (checkTickerExists(symbol)) {
            // find summary data in mongodb
            final MongoCollection<Document> mongoDBStocks = getMongoDB().getCollection(StocksimCollection.STOCKS.getCollectionName());
            final Document stockDocument = getMongoDB().findOne(Filters.eq("symbol", symbol), mongoDBStocks);
            return new Stock(stockDocument);
        }

        return null;
    }
}
