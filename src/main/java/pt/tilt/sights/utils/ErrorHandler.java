package pt.tilt.sights.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author Valter Nepomuceno
 * @since 27th July 2017
 * @version 1.0
 */
public class ErrorHandler {

    /**
     * Formats an exception message to be printed in the logs.
     * @param e Exception to be formatted.
     * @return Formatted exception log message.
     */
    public static String getFormattedException(Exception e) {
        return "[" + getCurrentDateTime() + "] [Exception] "  + e.getMessage();
    }

    /**
     * Prints a formatted exception message in the logs.
     * @param e Exception whose formatted message is to be printed.
     */
    public static void printExceptionMessage(Exception e) {
        System.out.println(ErrorHandler.getFormattedException(e));
    }

    /**
     * Builds a date time in the format 2017/07/20 12:19:01
     * @return Formatted current date time.
     */
    private static String getCurrentDateTime() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();
        return dateFormat.format(date);
    }

}