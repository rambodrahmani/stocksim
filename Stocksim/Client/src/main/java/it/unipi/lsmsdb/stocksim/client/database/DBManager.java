package it.unipi.lsmsdb.stocksim.client.database;

import com.datastax.oss.driver.api.core.cql.BoundStatement;
import com.datastax.oss.driver.api.core.cql.PreparedStatement;
import com.datastax.oss.driver.api.core.cql.ResultSet;
import com.datastax.oss.driver.api.core.cql.Row;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.result.UpdateResult;
import it.unipi.lsmsdb.stocksim.client.admin.Admin;
import it.unipi.lsmsdb.stocksim.client.app.ClientUtil;
import it.unipi.lsmsdb.stocksim.client.charting.HistoricalData;
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

import java.sql.Date;
import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Updates.pull;
import static com.mongodb.client.model.Updates.push;

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

    // set after user login
    private String username;

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
        final ResultSet resultSet = getCassandraDB().query(CassandraQueryBuilder.getTickerExistenceQuery(symbol));

        // find summary data in mongodb
        final MongoCollection<Document> mongoDBStocks = getMongoDB().getCollection(StocksimCollection.STOCKS.getCollectionName());
        final Document stock = getMongoDB().findOne(Filters.eq("symbol", symbol), mongoDBStocks);

        // disconnect from databaes
        disconnectMongoDB();
        disconnectCassandraDB();

        // check if historical and summary data was found
        ret = (resultSet != null && stock != null);

        return ret;
    }

    /**
     * Creates an asset profile in mongodb for the given {@link YFAssetProfile}.
     *
     * @param yfAssetProfile the {@link YFAssetProfile} to be inserted.
     *
     * @return true if the asset profile is added correctly, false otherwise.
     */
    public boolean createAssetProfile(final YFAssetProfile yfAssetProfile) {
        boolean ret = true;

        // retrieve stocks collection
        final MongoCollection<Document> stocks = getMongoDB().getCollection(StocksimCollection.STOCKS.getCollectionName());

        // get MongoDB document
        final Document assetDocument = yfAssetProfile.getAssetDocument();

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

        // disconnect from mongodb
        disconnectMongoDB();

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
    public boolean userLogin(final User user) throws CQLSessionException {
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
            user.setEmail(userDocument.getString("email"));

            // portfolios fetch runnable
            final Runnable portfoliosRunnable = () -> {
                try {
                    // fetch user portfolios array
                    final ArrayList<Portfolio> userPortfolios = new ArrayList<>();
                    final List<Document> portfolios = userDocument.getList("portfolios", Document.class);

                    // check if the user has any portfolio
                    if (portfolios != null) {
                        for (final Document portfolio : portfolios) {
                            final String name = portfolio.getString("name");
                            final List<String> tickers = portfolio.getList("tickers", String.class);
                            final ArrayList<Stock> stocks = new ArrayList<>();
                            for (final String symbol : tickers) {
                                final Stock stock = searchStock(symbol);
                                stocks.add(stock);
                            }
                            final Portfolio userPortfolio = new Portfolio(name, stocks);
                            userPortfolios.add(userPortfolio);
                        }
                    }

                    // set logged in user portfolios
                    user.setPortfolios(userPortfolios);
                } catch (final CQLSessionException e) {
                    e.printStackTrace();
                }
            };

            // start portfolios fetch thread
            final Thread portfoliosThread = new Thread(portfoliosRunnable, "Fetch Login Portfolios");
            portfoliosThread.start();

            // set user username for mongodb
            username = user.getUsername();
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

        // disconnect from mongodb
        disconnectMongoDB();

        return null;
    }

    /**
     * Retrieves the {@link HistoricalData} for the given stock symbol, using the given
     * period with given days granularity.
     *
     * @param symbol the ticker symbol to be searched;
     * @param startDate start date for the period;
     * @param endDate end date for the period;
     * @param granularity days granularity.
     *
     * @return the retrieved {@link HistoricalData}, might be empty.
     */
    public HistoricalData getHistoricalData(final String symbol, final LocalDate startDate, final LocalDate endDate, final int granularity) throws CQLSessionException {
        final HistoricalData historicalData = new HistoricalData();

        final ResultSet resultSet = getCassandraDB().query(
                "select PeriodParam(" + granularity + ", date, " +
                        "open, close, high, low, volume,adj_close) " +
                        "as Period from stocksim.tickers where date < '" + endDate + "' " +
                        "and date > '" + startDate + "' and symbol='" + symbol + "';"
        );

        for (final Row row : resultSet) {
            final Map<LocalDate, Map> data = row.getMap("Period", LocalDate.class, Map.class);

            if (data == null) {
                continue;
            }

            final Set<LocalDate> keySet = data.keySet();
            for (LocalDate finalDate : keySet) {
                // every date identify OHLC data for one candle
                final Map<String, Float> candle = data.get(finalDate);
                if (candle == null) {
                    continue;
                }

                historicalData.append(Date.from(finalDate.atStartOfDay(ZoneId.systemDefault()).toInstant()),
                        candle.get("open"),
                        candle.get("high"),
                        candle.get("low"),
                        candle.get("close"),
                        candle.get("volume"));
            }
        }

        // disconnect from cassandra db
        disconnectCassandraDB();

        return historicalData;
    }

    /**
     * Checks if a user portfolio with the given name already exists.
     *
     * @param name user portfolio name.
     *
     * @return true if a portfolio with the given name exists, false otherwise.
     */
    private boolean portfolioExists(final String name) {
        boolean ret = false;

        // retrieve user collection from mongodb
        final MongoCollection<Document> users = getMongoDB().getCollection(StocksimCollection.USERS.getCollectionName());

        // get info for user
        final Bson usernameFilter = eq("username", this.username);
        final Document userDocument = getMongoDB().findOne(usernameFilter, users);

        // set fields retrieved from db if present, otherwise login failed
        if (userDocument != null) {
            // fetch user portfolios array
            final List<Document> portfolios = userDocument.getList("portfolios", Document.class);

            // check if the user has any portfolio
            if (portfolios != null) {
                // find portfolio with the same name
                for (final Document portfolio : portfolios) {
                    if (portfolio.getString("name").equals(name)) {
                        ret = true;
                        break;
                    }
                }
            }
        }

        // disconnect from mongodb
        disconnectMongoDB();

        return ret;
    }

    /**
     * Inserts the given {@link Portfolio} in the user portfolios.
     *
     * @param portfolio the {@link Portfolio} to be added.
     *
     * @return true if the {@link Portfolio} was added without errors, false otherwise.
     */
    private boolean addPortfolio(final Portfolio portfolio) {
        boolean ret = true;

        // retrieve users collection from mongodb
        final MongoCollection<Document> users = getMongoDB().getCollection(StocksimCollection.USERS.getCollectionName());

        // update user portfolios array
        final Bson usernameFilter = eq("username", this.username);
        final Bson change = push("portfolios", portfolio.getDocument());
        final UpdateResult updateResult = users.updateOne(usernameFilter, change);

        // check update was executed correctly
        ret = updateResult.getMatchedCount() == 1 && updateResult.wasAcknowledged();

        return ret;
    }

    /**
     * Creates a new user portfolio with the given parameters.
     *
     * @param name user portfolio name;
     * @param symbols user portfolio stock symbols.
     *
     * @return the newly created {@link Portfolio}, null otherwise.
     *
     * @throws CQLSessionException
     */
    public Portfolio createPortfolio(final String name, final String[] symbols) throws CQLSessionException {
        // first check if a user portfolio with same name already exists
        if (portfolioExists(name)) {
            return null;
        }

        // fetch stock data from mongodb
        final ArrayList<Stock> stocks = new ArrayList<>();
        for (final String symbol : symbols) {
            final Stock stock = searchStock(symbol);
            if (stock == null) {
                return null;
            } else {
                stocks.add(stock);
            }
        }

        // create new user portfolio
        final Portfolio portfolio = new Portfolio(name, stocks);

        // add user portfolio to mongodb
        if (!addPortfolio(portfolio)) {
            // return null if mongodb insertion fails
            return null;
        }

        return portfolio;
    }

    /**
     * Removes the given {@link Portfolio} from the user portfolios.
     *
     * @param name the name of the {@link Portfolio} to be removed.
     *
     * @return true if the portfolio was removed without errors, false otherwise.
     */
    private boolean removePortfolio(final String name) {
        boolean ret = true;

        // retrieve users collection from mongodb
        final MongoCollection<Document> users = getMongoDB().getCollection(StocksimCollection.USERS.getCollectionName());

        // update user portfolios array
        final Bson usernameFilter = eq("username", this.username);
        final Bson change = pull("portfolios", new Document("name", name));
        final UpdateResult updateResult = users.updateOne(usernameFilter, change);

        // check update was executed correctly
        ret = updateResult.getMatchedCount() == 1 && updateResult.wasAcknowledged();

        return ret;
    }

    /**
     * Deletes the user portfolio with the given name.
     *
     * @param name user portfolio name;
     */
    public boolean deletePortfolio(final String name) {
        boolean ret = true;
        // first check if a user portfolio with same name already exists
        if (portfolioExists(name)) {
            ret = removePortfolio(name);
        } else {
            ret = false;
        }

        return ret;
    }
}
