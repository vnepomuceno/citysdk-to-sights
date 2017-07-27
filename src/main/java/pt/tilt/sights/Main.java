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
        MongoDbConnector mongoDbConnector = new MongoDbConnector();
        CitySdkConnector citySdkConnector = new CitySdkConnector();

        citySdkConnector.sendHttpGet(CitySdkConnector.LISBON_ENDPOINT_URI);
        citySdkConnector.sendHttpGet(CitySdkConnector.AMSTERDAM_ENDPOINT_URI);
        citySdkConnector.sendHttpGet(CitySdkConnector.HELSINKI_ENDPOINT_URI);
        citySdkConnector.sendHttpGet(CitySdkConnector.ROME_ENDPOINT_URI);
        citySdkConnector.sendHttpGet(CitySdkConnector.LAMIA_ENDPOINT_URI);
    }

}
