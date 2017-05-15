package edu.ucsd.dj.models;

import java.util.Calendar;
import java.util.TimeZone;

import edu.ucsd.dj.interfaces.IAddressable;

/**
 * Created by jonathanjimenez on 5/9/17.
 */
public class Event implements IAddressable {

    enum TimeOfDay{ Night, Morning, Afternoon, Evening}

    private long date;
    private boolean hasValidCoordinates;
    private double latitude;
    private double longitude;

    public Event(long dateTaken) {
        this.date = dateTaken;
    }

    public long getDate() { return date; }

    public boolean hasValidCoordinates() { return hasValidCoordinates; }
    public void setHasValidCoordinates(boolean hasValidCoordinates) {
        this.hasValidCoordinates = hasValidCoordinates;
    }

    public double getLatitude() { return latitude; }
    public void setLatitude(double latitude) { this.latitude = latitude; }

    public double getLongitude() { return longitude; }
    public void setLongitude(double longitude) { this.longitude = longitude; }

    public static Event.TimeOfDay currentTimeOfDay() {
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        return todFromHour(hour);
    }

    public static Event.TimeOfDay todFromHour(int hour) {
        TimeOfDay tod;
        if (hour < 6){
            tod = TimeOfDay.Night;
        } else if (hour < 12){
            tod = TimeOfDay.Morning;
        } else if (hour < 18){
            tod = TimeOfDay.Afternoon;
        } else {
            tod = TimeOfDay.Evening;
        }
        return tod;
    }

    public TimeOfDay timeOfDay() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(getDate());
        calendar.setTimeZone(TimeZone.getTimeZone("PST"));
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        return todFromHour(hour);
    }
}
