package edu.ucsd.dj.models;

import android.location.Location;

import java.util.Calendar;
import java.util.GregorianCalendar;

import edu.ucsd.dj.interfaces.models.IEvent;

import static edu.ucsd.dj.models.Event.TimeOfDay.None;

/**
 * Event class that holds the information of a photo
 * Created by jonathanjimenez on 5/9/17.
 */
public class Event implements IEvent {

    public enum TimeOfDay{ Night, Morning, Afternoon, Evening, None}

    private long date;
    private boolean hasValidCoordinates;
    private boolean hasValidDate;
    private Location loc;
    private TimeOfDay tod;

    //private double latitude;
    //private double longitude;

    public Event() {
        loc = new Location("");
        date = 0;
        hasValidCoordinates = false;
        hasValidDate = false;
        tod = None;
    }

    public void setTimeOfDay(TimeOfDay tod) {
        this.tod = tod;
    }

    public TimeOfDay getTimeOfDay(){
        return tod;
    }
    // IAddressable
    public boolean getHasValidCoordinates() { return hasValidCoordinates; }
    public void setHasValidCoordinates(boolean hasValidCoordinates) {
        this.hasValidCoordinates = hasValidCoordinates;
    }
    public double getLatitude() { return loc.getLatitude(); }
    public void setLatitude(double latitude) { loc.setLatitude(latitude); }
    public double getLongitude() { return loc.getLongitude(); }
    public void setLongitude(double longitude) { loc.setLongitude(longitude); }

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
        Calendar calendar = new GregorianCalendar();
        calendar.setTimeInMillis(date);
        tod =  timeOfDayFromCalendar(calendar);
    }

    public long getDateTime() { return date; }

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
    static public TimeOfDay timeOfDayFromCalendar(Calendar calendar) {
        return todFromHour(calendar.get(Calendar.HOUR_OF_DAY));
    }
}
