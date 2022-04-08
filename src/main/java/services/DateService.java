package services;

import models.Event;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.Calendar;

public class DateService {

    public static Event convertTechMemeDatesToStandardDates(Event event, String date) {
        String[] dashSplit = date.split("-");

        DateTimeFormatter dateTimeFormatterIn = DateTimeFormat.forPattern("MMM dd");
        DateTimeFormatter dateTimeFormatterOut = DateTimeFormat.forPattern("YYYY-MM-dd");

        long millis = dateTimeFormatterIn.parseMillis(dashSplit[0].trim());
        DateTime temp = new DateTime(millis).withYear(Calendar.getInstance().get(Calendar.YEAR));
        String startDate = dateTimeFormatterOut.print(temp.getMillis());

        if (dashSplit.length < 2) {
            return new Event(event.getWebsite(), event.getTitle(), startDate, null, event.getLocation());
        }

        String second;

        if (dashSplit[1].length() < 3) {
            second = dashSplit[0].substring(0, 3).trim() + " " + dashSplit[1].trim();
        } else {
            second = dashSplit[1].trim();
        }

        millis = dateTimeFormatterIn.parseMillis(second.trim());
        temp = new DateTime(millis).withYear(Calendar.getInstance().get(Calendar.YEAR));
        String endDate = dateTimeFormatterOut.print(temp.getMillis());

        return new Event(event.getWebsite(), event.getTitle(), startDate, endDate, event.getLocation());
    }
}
