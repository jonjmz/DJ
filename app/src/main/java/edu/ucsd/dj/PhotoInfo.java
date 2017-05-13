package edu.ucsd.dj;

import android.location.Address;

import java.util.Calendar;
import java.util.TimeZone;

/**
 * Created by jonathanjimenez on 5/9/17.
 */
public class PhotoInfo {
    private long dateTaken;                // Stores date image was taken
    private PhotoInfo.TimeOfDay timeOfDay; // Stores the time of day
    private Address address;
    private boolean hasValidCoordinates;
    private double latitude;
    private double longitude;

    public PhotoInfo(long dateTaken) {
        this.timeOfDay = PhotoInfo.TimeOfDay.getTimeOfDay(dateTaken);
        this.dateTaken = dateTaken;
    }

    public long getDateTaken() { return dateTaken; }

    public TimeOfDay getTimeOfDay() { return timeOfDay; }

    public Address getAddress() { return address; }
    public void setAddress(Address loc) { address = loc; }

    public boolean hasValidCoordinates() { return hasValidCoordinates; }
    public void setHasValidCoordinates(boolean hasValidCoordinates) {
        this.hasValidCoordinates = hasValidCoordinates;
    }

    public double getLatitude() { return latitude; }
    public void setLatitude(double latitude) { this.latitude = latitude; }

    public double getLongitude() { return longitude; }
    public void setLongitude(double longitude) { this.longitude = longitude; }

    enum TimeOfDay{
        Night, Morning, Afternoon, Evening;

        static TimeOfDay getTimeOfDay(long miliseconds) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeZone(TimeZone.getTimeZone("PST"));
            calendar.setTimeInMillis(miliseconds);
            int hour = calendar.get(Calendar.HOUR_OF_DAY);
            if (hour < 6){
                return Night;
            } else if (hour < 12){
                return Morning;
            } else if (hour < 18){
                return Afternoon;
            } else {
                return Evening;
            }
        }
    }
}
