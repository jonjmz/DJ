package edu.ucsd.dj.strategies;

import android.location.Location;
import android.util.Log;

import java.util.GregorianCalendar;

import edu.ucsd.dj.interfaces.IRating;
import edu.ucsd.dj.managers.Settings;
import edu.ucsd.dj.models.Event;

/**
 * Created by nguyen on 5/13/2017.
 */
public class RatingStrategy implements IRating {
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
    public double rate(Event info, boolean karma) {
        long  now = new GregorianCalendar().getTimeInMillis();
        double scoreSquared = 0;
        // If considering recency, add distance to photo
        if (recency) {
            // Get current time to compare with.
            // Calculates the ratio of the actual age over the possible age.
            double ratio = (now - info.getDateTime()) / (double)now;
            scoreSquared += Math.pow(ratio, 2);
        }
        // If considering time of day, add distance to photo
        if (Settings.isConsideringTOD()) {
            if(info.timeOfDay() != info.currentTimeOfDay()) {
                scoreSquared += 1;
            }
        }
        // If considering generateAddress
        if (Settings.isConsideringProximity()) {
            double distance = calculateDistance(info.getLatitude(), info.getLongitude());
            // 20000000 approximately half the circumference of the earth in meters
            scoreSquared += Math.pow(distance / 20000000, 2);
        }
        // We always consider karma
        if (!karma) scoreSquared += 1;

        // Score Calculations Here
        return Math.sqrt(scoreSquared);
    }

    private float calculateDistance(double latitude, double longitude){
        float distance[] = new float[1];

        Location.distanceBetween(currentLocation.getLatitude(),
                currentLocation.getLongitude(), latitude, longitude,
                distance);
        return distance[0];
    }
    public void setCurrentLocation(Location location) {

        if(location != null) {
            currentLocation = location;
            Log.i(getClass().toString(), "Setting location: Latitude: "
                    + currentLocation.getLatitude()
                    + "Longitude: " + currentLocation.getLatitude());
        }
        else {
            currentLocation = new Location("");
            Log.i(getClass().toString(), "Setting location to default location.");
        }

    }
    public Location getCurrentLocation(){
        return currentLocation;
    }
}
