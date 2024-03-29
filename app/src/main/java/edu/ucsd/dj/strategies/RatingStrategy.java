package edu.ucsd.dj.strategies;

import android.location.Location;
import android.util.Log;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.LinkedList;
import java.util.List;

import edu.ucsd.dj.interfaces.models.IAddressable;
import edu.ucsd.dj.interfaces.models.IPhoto;
import edu.ucsd.dj.interfaces.IRating;
import edu.ucsd.dj.interfaces.observers.IRatingObserver;
import edu.ucsd.dj.managers.Settings;
import edu.ucsd.dj.models.Event;

/**
 * Responsible to assign a rating for each photo using the
 * specified settings and current location
 * Created by nguyen on 5/13/2017.
 */
public class RatingStrategy implements IRating {

    private final double DEFAULT_LATITUDE = 0, DEFAULT_LONGITUDE = 0;

    private boolean consideringRecency;
    private boolean consideringTOD;
    private boolean consideringProximity;

    private IAddressable currentLocation;
    private List<IRatingObserver> observers;

    private Calendar calendar;

    public RatingStrategy(boolean recency, boolean tod, boolean proximity, Calendar calendar){
        this.consideringRecency = recency;
        this.consideringTOD = tod;
        this.consideringProximity = proximity;
        this.calendar = calendar;

        currentLocation = new Event();
        currentLocation.setLatitude(DEFAULT_LATITUDE);
        currentLocation.setLongitude(DEFAULT_LONGITUDE);

        observers = new LinkedList<>();
    }

    public IAddressable getCurrentLocation() {
        return currentLocation;
    }

    /**
     * Calculates score for this photo at this time/generateAddress with these settings.
     * Used to prepare photo for sorting by photo set. Implemented as distance function
     * in up to four dimensions

     * @param photo The photo
     * @return the score for the photo
     */
    @Override
    public double rate(IPhoto photo) {

        long  now = calendar.getTimeInMillis();
        double scoreSquared = 0;
        // If considering consideringRecency, add distance to photo
        if (consideringRecency) {
            // Get current time to compare with.
            // Calculates the ratio of the actual age over the possible age.
            double ratio = (now - photo.getDateTime()) / (double)now;
            scoreSquared += ratio;
        }

        // If considering time of day, add distance to photo
        if (consideringTOD) {
            if(photo.getTimeOfDay() != Event.timeOfDayFromCalendar(calendar)) {
                scoreSquared += 1;
            }
        }

        // If considering generateAddress
        if (consideringProximity) {
            double distance = calculateDistance(photo.getLatitude(), photo.getLongitude());
            // 20000000 approximately half the circumference of the earth in meters
            scoreSquared += distance / 20000000;
        }

        // We always consider karma
        if (photo.getKarma() != 0) scoreSquared += 1;

        // Score Calculations Here
        return Math.sqrt(scoreSquared);
    }

    /**
     * Calculate the distance between 2 location
     * @param latitude latitude of the location
     * @param longitude longitude of the location
     * @return the distance in meters between 2 location
     */
    private float calculateDistance(double latitude, double longitude){
        float distance[] = new float[1];

        Location.distanceBetween(currentLocation.getLatitude(),
                currentLocation.getLongitude(), latitude, longitude,
                distance);
        return distance[0];
    }

    public void setCurrentLocation(IAddressable location) {
        currentLocation = location;
        Log.i(getClass().toString(), "Setting location: Latitude: " + currentLocation.getLatitude()
                + "Longitude: " + currentLocation.getLatitude());
    }

    @Override
    public void update() {
        this.consideringRecency = Settings.getInstance().isConsideringRecency();
        this.consideringTOD = Settings.getInstance().isConsideringTOD();
        this.consideringProximity = Settings.getInstance().isConsideringProximity();

        ratingStrategyChanged();
    }

    @Override
    public void addObserver(IRatingObserver o) {
        observers.add(o);
    }

    @Override
    public void removeObserver(IRatingObserver o) {
        observers.remove(o);
    }

    @Override
    public void ratingStrategyChanged() {
        for (IRatingObserver o: observers) {
            o.updateRatingChange();
        }
    }
}
