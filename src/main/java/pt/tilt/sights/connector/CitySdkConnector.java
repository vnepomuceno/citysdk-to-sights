package pt.tilt.sights.connector;

import citysdk.tourism.client.exceptions.*;
import citysdk.tourism.client.poi.lists.ListPointOfInterest;
import citysdk.tourism.client.poi.single.PointOfInterest;
import citysdk.tourism.client.requests.Parameter;
import citysdk.tourism.client.requests.ParameterList;
import citysdk.tourism.client.requests.TourismClient;
import citysdk.tourism.client.requests.TourismClientFactory;
import citysdk.tourism.client.terms.ParameterTerms;
import pt.tilt.sights.utils.ErrorHandler;

import java.io.IOException;
import java.util.List;

/**
 * @author Valter Nepomuceno
 * @since 25th July 2017
 * @version 1.0
 */
public class CitySdkConnector {

    /** Sight city enum */
    public enum City {
        LISBON ("Lisbon"),
        AMSTERDAM ("Amsterdam"),
        HELSINKI ("Helsinki"),
        LAMIA ("Lamia"),
        ROME ("Rome");

        public String value;

        City (String value) {
            this.value = value;
        }
    }

    /** CitySDK Tourism client */
    private TourismClient tourismClient;

    /** Sight city */
    private City city;

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

    /**
     * City SDK public constructor.
     * @param endpointUri CitySDK endpoint URI.
     */
    public CitySdkConnector(String endpointUri) {
        try {
            if (endpointUri.equals(LISBON_ENDPOINT_URI))
                city = City.LISBON;
            else if (endpointUri.equals(AMSTERDAM_ENDPOINT_URI))
                city = City.AMSTERDAM;
            else if (endpointUri.equals(HELSINKI_ENDPOINT_URI))
                city = City.HELSINKI;
            else if (endpointUri.equals(LAMIA_ENDPOINT_URI))
                city = City.LAMIA;
            else if (endpointUri.equals(ROME_ENDPOINT_URI))
                city = City.ROME;

            tourismClient = TourismClientFactory.getInstance().getClient(endpointUri);
            tourismClient.useVersion("1.0");
        } catch (IOException ioe) {
            ErrorHandler.printExceptionMessage(ioe);
        } catch (UnknownErrorException uee) {
            ErrorHandler.printExceptionMessage(uee);
        } catch (ServerErrorException see) {
            ErrorHandler.printExceptionMessage(see);
        }
    }

    /** Queries CitySDK Tourism client for sights and stores them in MongoDB database. */
    public void listSights() {
        ParameterList paramList = new ParameterList();

        try {
            paramList.add(new Parameter(ParameterTerms.CATEGORY, "Miradouros"));
            paramList.add(new Parameter(ParameterTerms.LIMIT, 50));

            ListPointOfInterest response = tourismClient.getPois(paramList);
            List<PointOfInterest> listPointsOfInterest = response.getPois();

            for (PointOfInterest poi : listPointsOfInterest)
                MongoDbConnector.storeSightObject(poi, city);
        } catch (InvalidParameterException ipe) {
            ErrorHandler.printExceptionMessage(ipe);
        } catch (InvalidValueException ive) {
            ErrorHandler.printExceptionMessage(ive);
        } catch (IOException ioe) {
            ErrorHandler.printExceptionMessage(ioe);
        } catch (ResourceNotAllowedException rnae) {
            ErrorHandler.printExceptionMessage(rnae);
        } catch (UnknownErrorException uee) {
            ErrorHandler.printExceptionMessage(uee);
        } catch (VersionNotAvailableException vnae) {
            ErrorHandler.printExceptionMessage(vnae);
        } catch (ServerErrorException see) {
            ErrorHandler.printExceptionMessage(see);
        }
    }

}
