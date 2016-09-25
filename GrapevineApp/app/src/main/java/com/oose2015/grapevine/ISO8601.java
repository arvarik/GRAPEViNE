package com.oose2015.grapevine;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Date;

/**
 * Helper class for handling a most common subset of ISO 8601 strings
 * (in the following format: "2008-03-01T13:00:00+01:00"). It supports
 * parsing the "Z" timezone, but many other less-used features are
 * missing.
 */
public final class ISO8601 {
    private static SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");

    /** Transform Calendar to ISO 8601 string. */
    public static String fromCalendar(Calendar calendar) {
        Date date = calendar.getTime();
        String formatted = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
                .format(date);
        return formatted.substring(0, 22) + ":" + formatted.substring(22);
    }

    /** Get current date and time formatted as ISO 8601 string. */
    public static String now() {
        return fromCalendar(GregorianCalendar.getInstance());
    }

    /** Transform ISO 8601 string to Calendar. */
    public static GregorianCalendar toCalendar(final String iso8601string)
            throws ParseException {
        GregorianCalendar calendar = new GregorianCalendar();

        String s = iso8601string.replace("Z", "+00:00");
        try {
            s = s.substring(0, 22) + s.substring(23);  // to get rid of the ":"
        } catch (IndexOutOfBoundsException e) {
            throw new ParseException("Invalid length", 0);
        }
        Date date = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").parse(s);
        calendar.setTime(date);
        return calendar;
    }

    /**
     * Converts time string to Time object
     * @param timeString string to convert from
     * @return GregorianCalendar object
     */
    public static GregorianCalendar stringToTime(String timeString) {
        try {
            GregorianCalendar c = new GregorianCalendar();
            c.setTime(timeFormat.parse(timeString));
            return c;
        } catch (ParseException p) {}
        return null;
    }

    /**
     * Converts time calendar object to Time string
     * @param calendar calendar object to convert from
     * @return String object of calendar
     */
    public static String calendarToTimeString(Calendar calendar) {
        return timeFormat.format(calendar.getTime());
    }
}