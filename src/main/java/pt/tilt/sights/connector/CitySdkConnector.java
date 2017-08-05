package pt.tilt.sights.connector;

import citysdk.tourism.client.exceptions.*;
import citysdk.tourism.client.poi.lists.ListPOIS;
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

    private TourismClient tourismClient;

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
     *
     * @param endpointUri
     */
    public CitySdkConnector(String endpointUri) {
        try {
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

    public void listSights() {
        ParameterList paramList = new ParameterList();

        try {
            paramList.add(new Parameter(ParameterTerms.CATEGORY, "Miradouros"));
            paramList.add(new Parameter(ParameterTerms.LIMIT, 50));

            ListPointOfInterest response = tourismClient.getPois(paramList);
            List<PointOfInterest> listPois = response.getPois();
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
