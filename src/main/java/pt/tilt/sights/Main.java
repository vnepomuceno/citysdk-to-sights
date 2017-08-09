package pt.tilt.sights;

import pt.tilt.sights.connector.CitySdkConnector;
import pt.tilt.sights.connector.MongoDbConnector;

/**
 * @author Valter Nepomuceno
 * @since 25th July 2017
 * @version 1.0
 */
public class Main {

    public static void main(String[] args) {
        MongoDbConnector.init();
        CitySdkConnector citySdkConnector = new CitySdkConnector(CitySdkConnector.LISBON_ENDPOINT_URI);
        citySdkConnector.listSights();
    }

}
