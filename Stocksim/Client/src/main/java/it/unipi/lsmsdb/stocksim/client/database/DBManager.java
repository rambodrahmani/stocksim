package it.unipi.lsmsdb.stocksim.client.database;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import it.unipi.lsmsdb.stocksim.client.admin.Admin;
import it.unipi.lsmsdb.stocksim.client.app.ClientUtil;
import it.unipi.lsmsdb.stocksim.client.user.User;
import it.unipi.lsmsdb.stocksim.database.cassandra.CassandraDB;
import it.unipi.lsmsdb.stocksim.database.cassandra.CassandraDBFactory;
import it.unipi.lsmsdb.stocksim.database.mongoDB.MongoDB;
import it.unipi.lsmsdb.stocksim.database.mongoDB.MongoDBFactory;
import it.unipi.lsmsdb.stocksim.database.mongoDB.MongoServer;
import it.unipi.lsmsdb.stocksim.database.mongoDB.StocksimCollection;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.Arrays;

import static com.mongodb.client.model.Filters.and;
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
     * Checks if historical data is available for the given symbol.
     *
     * @param symbol
     *
     * @return
     */
    public boolean checkTickerExists(final String symbol) {
        boolean ret = true;

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

        // retrieve admin collection from mongodb
        final MongoCollection<Document> admins = getMongoDB().getCollection(StocksimCollection.USERS.getCollectionName());

        // get info for admin
        final Bson usernameFilter = eq("username", user.getUsername());
        final Bson passwordFilter = eq("password", hashedPwd);
        final Bson loginFilter = Filters.and(usernameFilter, passwordFilter);
        final Document adminDocument = getMongoDB().findOne(loginFilter, admins);

        // set fields retrieved from db if present, otherwise login failed
        if (adminDocument != null) {
            user.setName(adminDocument.getString("name"));
            user.setSurname(adminDocument.getString("surname"));
        } else {
            ret = false;
        }

        // disconnect from mongodb
        disconnectMongoDB();

        return ret;
    }
}
