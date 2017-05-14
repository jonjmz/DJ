package edu.ucsd.dj;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.util.Log;

import java.util.GregorianCalendar;

import edu.ucsd.dj.interfaces.IAddressable;
import edu.ucsd.dj.interfaces.IRating;

/**
 * Created by nguyen on 5/13/2017.
 */

public class RatingStrategy implements IRating {
    private LocationManager locationManager;
    boolean recency, tod, proximity;
    private static Location currentLocation;

    public RatingStrategy(boolean recency, boolean tod, boolean proximity){
        this.recency = recency;
        this.tod = tod;
        this.proximity = proximity;
    }

    /**
     * Calculates score for this photo at this time/generateAddress with these settings.
     * Used to prepare photo for sorting by photo set. Implemented as distance function
     * in up to four dimensions
     */
    @Override
    public double rate(PhotoInfo info, boolean karma) {
        long  now = new GregorianCalendar().getTimeInMillis();
        double scoreSquared = 0;
        // If considering recency, add distance to photo
        if (recency) {
            // Get current time to compare with.
            // Calculates the ratio of the actual age over the possible age.
            double ratio = (now - info.getDateTaken()) / (double)now;
            scoreSquared += Math.pow(ratio, 2);
        }
        // If considering time of day, add distance to photo
        if (Settings.isConsideringTOD()) {
            if(info.getTimeOfDay() != PhotoInfo.TimeOfDay.getCurrent()) {
                scoreSquared += 1;
            }
        }
        // If considering generateAddress
        if (Settings.isConsideringProximity()) {
            scoreSquared += Math.pow(calculateDistance(info.getLatitude(),
                    info.getLongitude()), 2);
        }
        // We always consider karma
        if (!karma) scoreSquared += 1;

        // Score Calculations Here
        return Math.sqrt(scoreSquared);

    }

    private float calculateDistance(double latitude, double longitude){
        float distance[] = new float[1];
        Log.i(getClass().toString(), "Latitude: " + currentLocation.getLatitude() );
        Log.i(getClass().toString(), "Longitude: " + currentLocation.getLatitude());

        Location.distanceBetween(currentLocation.getLatitude(),
                currentLocation.getLongitude(), latitude, longitude,
                distance);
        return distance[0];
    }
    public void setCurrentLocation(Location location) {
        if(location != null)
            currentLocation = location;
        else
            currentLocation = new Location("");
    }
}
