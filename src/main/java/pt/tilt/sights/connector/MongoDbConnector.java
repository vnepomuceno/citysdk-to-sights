package pt.tilt.sights.connector;

import com.mongodb.*;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import pt.tilt.sights.utils.ErrorHandler;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Properties;

/**
 * @author Valter Nepomuceno
 * @since 25th July 2017
 * @version 1.0
 */
public class MongoDbConnector {

    /** File path for config.properties */
    private static final String CONFIG_PATH = "./src/main/java/pt/tilt/sights/config.properties";

    /** Properties object loaded from config.properties file */
    private Properties properties = new Properties();

    /** MongoDB hostname */
    private String hostname;

    /** MongoDB port */
    private int port;

    /** MongoDB username */
    private String username;

    /** MongoDB password */
    private char[] password;

    /** MongoDB database */
    private String database;

    /** MongoDB environment */
    private String environment;

    /** MongoDB database object */
    private MongoDatabase mongoDatabase;

    /**
     * Public constructor for MongoDB connector.
     */
    public MongoDbConnector() {
        loadProperties();
        connectToDatabase();
    }

    /**
     * Loads MongoDB configuration variables from 'config.properties' file.
     */
    public void loadProperties() {
        try {
            InputStream input = new FileInputStream(CONFIG_PATH);
            properties.load(input);
        } catch (IOException e) {
            e.printStackTrace();
        }

        environment = properties.getProperty("environment");

        // SANDBOX Environment
        if (environment.equals("SBX")) {
            hostname = properties.getProperty("mongoDB_SBX_hostname");
            port = Integer.parseInt(properties.getProperty("mongoDB_SBX_port"));
            username = properties.getProperty("mongoDB_SBX_username");
            password = properties.getProperty("mongoDB_SBX_password").toCharArray();
            database = properties.getProperty("mongoDB_SBX_database");
        }
        // PRODUCTION Environment
        else if (environment.equals("PRD")) {
            hostname = properties.getProperty("mongoDB_PRD_hostname");
            port = Integer.parseInt(properties.getProperty("mongoDB_PRD_port"));
            username = properties.getProperty("mongoDB_PRD_username");
            password = properties.getProperty("mongoDB_PRD_password").toCharArray();
            database = properties.getProperty("mongoDB_PRD_database");
        }
    }

    /**
     * Establishes a connection to MongoDB and instantiates MongoDatabase object.
     */
    public void connectToDatabase() {
        try {
            MongoClient mongoClient;

            if (environment.equals("SBX"))
                mongoClient = new MongoClient(hostname, port);
            else {
                MongoCredential mongoCredential = MongoCredential.createCredential(username, database, password);
                mongoClient = new MongoClient(new ServerAddress(hostname, port), Arrays.asList(mongoCredential));
            }

            mongoDatabase = mongoClient.getDatabase(database);
        } catch (Exception e) {
            ErrorHandler.printExceptionMessage(e);
        }
    }



}
