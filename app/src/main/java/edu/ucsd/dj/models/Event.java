package edu.ucsd.dj.models;

import java.util.Calendar;
import java.util.TimeZone;

import edu.ucsd.dj.interfaces.IAddressable;
import edu.ucsd.dj.interfaces.IDateTimeable;

/**
 * Event class that holds the information of a photo
 * Created by jonathanjimenez on 5/9/17.
 */
public class Event implements IAddressable, IDateTimeable {

    public enum TimeOfDay{ Night, Morning, Afternoon, Evening}

    private long date;
    private boolean hasValidCoordinates;
    private boolean hasValidDate;
    private double latitude;
    private double longitude;

    public Event() {

    }

    public long getDateTime() { return date; }

    // IAddressable
    public boolean hasValidCoordinates() { return hasValidCoordinates; }
    public void setHasValidCoordinates(boolean hasValidCoordinates) {
        this.hasValidCoordinates = hasValidCoordinates;
    }
    public double getLatitude() { return latitude; }
    public void setLatitude(double latitude) { this.latitude = latitude; }
    public double getLongitude() { return longitude; }
    public void setLongitude(double longitude) { this.longitude = longitude; }

    /**
     * Get the currentTimeOfDay using an enum
     * @return enum that shows the current time of day
     */
    public static Event.TimeOfDay currentTimeOfDay() {
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        return todFromHour(hour);
    }

    // IDateTimeable
    @Override
    public boolean hasValidDate() {
        return hasValidDate;
    }

    @Override
    public void setHasValidDate(boolean hvd) {
        this.hasValidDate = hvd;
    }

    @Override
    public void setDateTime(long dateTime) {
        this.date = dateTime;
    }

    /**
     * Set the current time of day using hour
     * @param hour the current hour
     * @return TimeOfDay enum to represent the current TOD
     */
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

    /**
     * Get the current time of day
     * @return current time of day
     */
    public TimeOfDay timeOfDay() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(getDateTime());
        calendar.setTimeZone(TimeZone.getTimeZone("PST"));
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        return todFromHour(hour);
    }
}
