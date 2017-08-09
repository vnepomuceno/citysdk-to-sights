package pt.tilt.sights.connector;

import citysdk.tourism.client.poi.single.PointOfInterest;
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
    private static Properties properties = new Properties();

    /** MongoDB hostname */
    private static String hostname;

    /** MongoDB port */
    private static int port;

    /** MongoDB username */
    private static String username;

    /** MongoDB password */
    private static char[] password;

    /** MongoDB database */
    private static String database;

    /** MongoDB environment */
    private static String environment;

    /** MongoDB database collection */
    private static String collectionName;

    /** MongoDB database object */
    private static MongoDatabase mongoDatabase;

    /**
     * Public constructor for MongoDB connector.
     */
    public static void init() {
        loadProperties();
        connectToDatabase();
    }

    /**
     * Loads MongoDB configuration variables from 'config.properties' file.
     */
    public static void loadProperties() {
        try {
            InputStream input = new FileInputStream(CONFIG_PATH);
            properties.load(input);
        } catch (IOException e) {
            e.printStackTrace();
        }

        environment = properties.getProperty("environment");
        collectionName = properties.getProperty("collection");

        // SANDBOX Environment
        if (environment.equals("SBX")) {
            hostname = properties.getProperty("mongoDB_SBX_hostname");
            port = Integer.parseInt(properties.getProperty("mongoDB_SBX_port"));
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
    public static void connectToDatabase() {
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

    /**
     * Parses Point of Interest object and stores it in MongoDB database.
     * @param poi Point of Interest object.
     * @param city City enum.
     */
    public static void storeSightObject(PointOfInterest poi, CitySdkConnector.City city) {
        Document location = new Document();
        location.append("city", city.value);
        if (poi.getLocation().getPoint().size() > 0)
            location.append("coordinates", poi.getLocation().getPoint().get(0).getPoint().getPosList());
        location.append("createdAt", poi.getLocation().getCreated().toString());
        location.append("updatedAt", poi.getLocation().getUpdated().toString());

        Document author = new Document();
        author.append("source", poi.getAuthor().getValue());
        author.append("createdAt", poi.getAuthor().getCreated().toString());

        Document sight = new Document();
        if (poi.getLabel().size() > 0)
            sight.append("name", poi.getLabel().get(0).getValue());
        sight.append("citySdkId", poi.getId());
        sight.append("base", poi.getBase());
        sight.append("location", location);
        sight.append("author", author);
        sight.append("createdAt", poi.getCreated().toString());
        sight.append("updatedAt", poi.getUpdated().toString());

        MongoCollection mongoCollection = mongoDatabase.getCollection(collectionName);
        mongoCollection.insertOne(sight);
    }

}
