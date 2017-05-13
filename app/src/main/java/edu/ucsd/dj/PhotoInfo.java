package edu.ucsd.dj;

import android.util.Log;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import edu.ucsd.dj.interfaces.IAddressable;

/**
 * Created by jonathanjimenez on 5/9/17.
 */
public class PhotoInfo implements IAddressable {
    private long dateTaken;                // Stores date image was taken
    private PhotoInfo.TimeOfDay timeOfDay; // Stores the time of day
    private boolean hasValidCoordinates;
    private double latitude;
    private double longitude;

    public PhotoInfo(long dateTaken) {
        this.timeOfDay = PhotoInfo.TimeOfDay.getTimeOfDay(dateTaken);
        this.dateTaken = dateTaken;
    }

    public long getDateTaken() { return dateTaken; }

    public TimeOfDay getTimeOfDay() { return timeOfDay; }

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
            calendar.setTimeInMillis(miliseconds);
            calendar.setTimeZone(TimeZone.getTimeZone("PST"));
            int hour = calendar.get(Calendar.HOUR_OF_DAY);
            return(todFromHour(hour));
        }
        static TimeOfDay getCurrent() {
            Calendar calendar = Calendar.getInstance();
            int hour = calendar.get(Calendar.HOUR_OF_DAY);
            return todFromHour(hour);
        }
        static TimeOfDay todFromHour(int hour) {
            TimeOfDay tod;
            if (hour < 6){
                tod = Night;
            } else if (hour < 12){
                tod = Morning;
            } else if (hour < 18){
                tod = Afternoon;
            } else {
                tod = Evening;
            }
            return tod;
        }
    }
}
