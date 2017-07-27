package pt.tilt.sights.connector;

import pt.tilt.sights.utils.ErrorHandler;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * @author Valter Nepomuceno
 * @since 25th July 2017
 * @version 1.0
 */
public class CitySdkConnector {

    /** CitySDK API endpoint for the city of Lisbon */
    public static final String LISBON_ENDPOINT_URI = "http://tourism.citysdk.cm-lisboa.pt/resources";

    /** CitySDK API endpoint for the city of Amsterdam */
    public static final String AMSTERDAM_ENDPOINT_URI= "http://citysdk.dmci.hva.nl/CitySDK/resources";

    /** CitySDK API endpoint for the city of Helsinki */
    public static final String HELSINKI_ENDPOINT_URI = "http://api.tourism.helsinki.citysdk.eu/CitySDK/resources";

    /** CitySDK API endpoint for the city of Lamia */
    public static final String LAMIA_ENDPOINT_URI = "http://tourism.citysdk.lamia-city.gr/resources";

    /** CitySDK API endpoint for the city of Rome */
    public static final String ROME_ENDPOINT_URI = "http://citysdk.inroma.roma.it/CitySDK/resources";

    /** HTTP method to make the request to CitySDK API */
    private final String httpMethod = "GET";

    /**
     *
     * @param endpointUri
     */
    public void sendHttpGet(String endpointUri) {
        try {
            URL url = new URL(endpointUri);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod(httpMethod);
            int httpResponseCode = connection.getResponseCode();
            System.out.println(httpResponseCode);
        } catch (MalformedURLException mue) {
            ErrorHandler.printExceptionMessage(mue);
        } catch (IOException ioe) {
            ErrorHandler.printExceptionMessage(ioe);
        }
    }

}
