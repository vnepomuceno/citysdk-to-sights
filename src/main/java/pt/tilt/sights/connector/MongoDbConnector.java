package pt.tilt.sights.connector;

import citysdk.tourism.client.poi.base.POIBaseType;
import citysdk.tourism.client.poi.base.POITermType;
import citysdk.tourism.client.poi.single.PointOfInterest;
import com.mongodb.*;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.BsonArray;
import org.bson.Document;
import org.bson.conversions.Bson;
import pt.tilt.sights.utils.ErrorHandler;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

/**
 * @author Valter Nepomuceno
 * @since 25th July 2017
 * @version 1.0
 */
public class MongoDbConnector {

    /** File path for config.properties */
    private static final String CONFIG_PATH = "./src/main/java/pt/tilt/sights/config.properties";

    /** Point Of Interest card address prefix */
    private static final String POI_CARD_ADDRESS_PREFIX = "ADR;WORK";

    /** Point Of Interest card phone prefix */
    private static final String POI_CARD_PHONE_PREFIX = "TEL;WORK:";

    /** Point Of Interest card email prefix */
    private static final String POI_CARD_EMAIL_PREFIX = "EMAIL;INTERNET:";

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
    public static void storeSightObject(PointOfInterest poi, CitySdkConnector.City city,
                                        CitySdkConnector.Country country) {

        /* Accesses and populates POI author data */
        Document author = new Document();
        author.append("source", poi.getAuthor().getValue());
        author.append("createdAt", poi.getAuthor().getCreated().toString());

        /* Accesses and populates POI general data */
        Document sight = new Document();
        if (poi.getLabel().size() > 0) {
            ArrayList<Document> labels = new ArrayList<Document>();
            for (POITermType termType : poi.getLabel()) {
                Document label = new Document();
                label.append("term", termType.getTerm());
                label.append("value", termType.getValue());
                label.append("lang", termType.getLang());
                label.append("createdAt", termType.getCreated().toString());
                label.append("updatedAt", termType.getUpdated().toString());
                labels.add(label);
            }
            sight.append("labels", labels);
        }

        /* Accesses and populates POI description data */
        if (poi.getDescription().size() > 0) {
            ArrayList<Document> descriptions = new ArrayList<Document>();
            for (POIBaseType baseType : poi.getDescription()) {
                Document description = new Document();
                description.append("value", baseType.getValue());
                description.append("lang", poi.getDescription().get(0).getLang());
                description.append("createdAt", poi.getDescription().get(0).getCreated().toString());
                description.append("updatedAt", poi.getDescription().get(0).getUpdated().toString());
                descriptions.add(description);
            }
            sight.append("descriptions", descriptions);
        }

        /* Accesses and populates POI links data */
        if (poi.getLink().size() > 0) {
            ArrayList<Document> images = new ArrayList<Document>();
            ArrayList<Document> websites = new ArrayList<Document>();
            for (POITermType termType : poi.getLink()) {
                Document linkDocument = new Document();
                linkDocument.append("type", termType.getType());
                linkDocument.append("href", termType.getHref());
                linkDocument.append("createdAt", termType.getCreated().toString());
                linkDocument.append("updatedAt", termType.getUpdated().toString());
                if (termType.getTerm().equals("related")) {
                    images.add(linkDocument);
                } else if (termType.getTerm().equals("source")) {
                    websites.add(linkDocument);
                }
            }
            if (images.size() > 0)
                sight.append("images", images);
            if (websites.size() > 0)
                sight.append("websites", websites);
        }

        /* Accesses and populates POI schedules data */
        ArrayList<Document> schedules = new ArrayList<Document>();
        if (poi.getTime().size() > 0) {
            for (POITermType termType : poi.getTime()) {
                Document scheduleDocument = new Document();
                scheduleDocument.append(termType.getTerm(), termType.getValue());
                scheduleDocument.append("createdAt", termType.getCreated());
                scheduleDocument.append("updatedAt", termType.getUpdated());
                schedules.add(scheduleDocument);
            }
        }

        /* Parses CitySDK POI addresses, phone numbers and emails */
        String poiAddressCard = poi.getLocation().getAddress().getValue();
        String phoneNumber = "", fullAddress = "", emailAddress = "", facebookPage = "";
        String[] poiAddressCardSplit = poiAddressCard.split("\r\n");
        for (String cardSplit : poiAddressCardSplit) {
            if (cardSplit.startsWith(POI_CARD_PHONE_PREFIX)) {
                phoneNumber = cardSplit.substring(POI_CARD_PHONE_PREFIX.length());
            } else if (cardSplit.startsWith(POI_CARD_EMAIL_PREFIX)) {
                emailAddress = cardSplit.substring(POI_CARD_EMAIL_PREFIX.length());
            } else if (cardSplit.startsWith(POI_CARD_ADDRESS_PREFIX)) {
                String[] addressSplit = cardSplit.split(";");
                for (String split : addressSplit) {
                    if (!split.equals("") && !split.startsWith("ADR") && !split.startsWith("WORK")) {
                        if (split.contains("\n")) {
                            fullAddress = split.split("\n")[0];
                            break;
                        } else {
                            fullAddress = split;
                            break;
                        }
                    }
                }
            }
        }

        /* Parses CitySDK POI facebook pages and email addresses */
        for (POITermType poiLink : poi.getLink()) {
            if (poiLink.getType().startsWith("text")) {
                if (poiLink.getHref().contains("facebook")) {
                    facebookPage = poiLink.getHref();
                } else if (poiLink.getHref().contains("@")) {
                    emailAddress = poiLink.getHref();
                }
            }
        }

        /* Accesses and populates POI contact data */
        Document contact = new Document();
        if (!phoneNumber.isEmpty())
            contact.append("phoneNumber", phoneNumber);
        if (!emailAddress.isEmpty())
            contact.append("emailAddress", emailAddress);
        if (!facebookPage.isEmpty())
            contact.append("facebook", facebookPage);
        if (!phoneNumber.isEmpty() || !facebookPage.isEmpty() || !emailAddress.isEmpty())
            sight.append("contact", contact);

        /* Accesses and populates POI address data */
        Document location = new Document();
        if (!fullAddress.isEmpty())
            location.append("address", fullAddress);

        /* Accesses and populates POI location data */
        location.append("city", city.value);
        location.append("country", country.value);
        if (poi.getLocation().getPoint().size() > 0)
            location.append("coordinates", poi.getLocation().getPoint().get(0).getPoint().getPosList());
        location.append("createdAt", poi.getLocation().getCreated().toString());
        location.append("updatedAt", poi.getLocation().getUpdated().toString());

        /* Accesses and populates POI schedule data */
        if (schedules.size() > 0)
            sight.append("schedules", schedules);

        /* Accesses and populates remaining POI data */
        sight.append("citySdkId", poi.getId());
        sight.append("base", poi.getBase());
        sight.append("location", location);
        sight.append("author", author);
        sight.append("createdAt", poi.getCreated().toString());
        sight.append("updatedAt", poi.getUpdated().toString());

        /* Stores sight document in MongoDB collection */
        MongoCollection mongoCollection = mongoDatabase.getCollection(collectionName);
        mongoCollection.insertOne(sight);
    }

}
