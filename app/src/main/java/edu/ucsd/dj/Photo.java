package edu.ucsd.dj;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import android.location.Address;
import android.support.annotation.NonNull;

import java.io.Serializable;
import java.util.Calendar;
import java.util.HashMap;
import java.util.TimeZone;

/**
 * Created by jonathanjimenez on 5/1/17.
 */

public class Photo implements Comparable, Serializable {
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
    private double score;        // Used to order photos with precalulated scores
    private boolean hasKarma;    // Used to keep track of karma
    private boolean releasable;  // Check if it is releasable, only defualt photo is not
    private boolean released;    // Check if it is released
    private boolean karmaable;   // Check if it is krama-able, only defualt photo is not
    private long dateTaken;      // Stores date image was taken
    private TimeOfDay timeOfDay; //Stores the time of day
    private String pathname;    // Reference to image in album
    private Address address;
    private boolean hasValidCoordinates;
    private double latitude;
    private double longitude;

    public Photo() {
        this.releasable = false;
        this.karmaable = false;
        this.pathname = "Default Location";
    }

    public Photo(String reference, long dateTaken) {
        // TODO - Log something in here.

        this.dateTaken = dateTaken;
        this.timeOfDay = TimeOfDay.getTimeOfDay(dateTaken);
        this.releasable = true;
        this.karmaable = true;
        this.pathname = reference;
        this.dateTaken = dateTaken;
    }


    /**
     * Calculates score for this photo at this time/address with these settings.
     * Used to prepare photo for sorting by photo set. Implemented as distance function
     * in up to four dimensions
     */
    public void calculateScore() {
        long  now = System.currentTimeMillis();
        double scoreSquared = 0;
        // If considering recency, add distance to photo
        if (true) {
            // Get current time to compare with.
            // Calculates the ratio of the actual age over the possible age.
            double ratio = (now - dateTaken) / (double)now;
            scoreSquared += Math.pow(ratio, 2);
        }
        // If considering time of day, add distance to photo
        if (true) {
            if(timeOfDay != TimeOfDay.getTimeOfDay(now))
                scoreSquared += 1;
        }
        // TODO: If considering address, add distance to photo
        if (false) {
            scoreSquared += Math.pow(0, 2);
        }
        // TODO: If considering karma, add distance to photo
        if (false) {
            if (!hasKarma) scoreSquared += Math.pow(1, 2);
        }
        // Score Calculations Here
        score = Math.sqrt(scoreSquared);
    }

    private static double TimeOfDayDifference(Photo a, Photo b){
        return 0;
    }


    public boolean hasKarma() { return hasKarma; }

    public boolean isKarmable() { return karmaable; }

    public boolean isReleasable() {
        return releasable;
    }

    public boolean isReleased() {
        return released;
    }

    public void release() {
        released = true;
    }

    public void removeRelease() { released = false; }

    public void giveKarma() {
        hasKarma = true;
    }

    public void removeKarma() {
        hasKarma = false;
    }

    public long getDateTaken() { return dateTaken; }

    public double getScore(){ return score; }

    public Address getAddress() {return address; }
    public void setAddress(Address loc) { address = loc; }

    public boolean hasValidCoordinates() { return hasValidCoordinates; }

    public void setHasValidCoordinates(boolean hasValidCoordinates) {
        this.hasValidCoordinates = hasValidCoordinates;
    }


    public double getLatitude() { return latitude; }
    public void setLatitude(double latitude) { this.latitude = latitude; }

    public double getLongitude() { return longitude; }
    public void setLongitude(double longitude) { this.longitude = longitude; }


    /**
     * Indicates whether some other object is "equal to" this one.
     *
     * @param o the reference object with which to compare.
     * @return {@code true} if this object is the same as the obj
     * argument; {@code false} otherwise.
     * @see #hashCode()
     * @see HashMap
     */
    @Override
    public boolean equals(Object o) {
        return pathname.equals(((Photo) o).pathname);
    }

    /**
     * Returns a hash code value for the object.
     *
     * @return a hash code value for this object.
     * @see Object#equals(Object)
     * @see System#identityHashCode
     */
    @Override
    public int hashCode() {
        return pathname.hashCode();
    }

    /**
     * Compares this object with the specified object for order.  Returns a
     * negative integer, zero, or a positive integer as this object is less
     * than, equal to, or greater than the specified object.
     *
     * @param o the object to be compared.
     * @return a negative integer, zero, or a positive integer as this object
     * is less than, equal to, or greater than the specified object.
     * @throws NullPointerException if the specified object is null
     * @throws ClassCastException   if the specified object's type prevents it
     *                              from being compared to this object.
     */
    @Override
    public int compareTo(@NonNull Object o) {
        if (score < ((Photo) o).score) return -1;
        else if (score > ((Photo) o).score) return 1;
        else return 0;
    }

    /**
     * Returns this photo as a Bit Map.
     *
     * @return a Bit Map object.
     */
    public Bitmap getBitmap() {
        return BitmapFactory.decodeFile(pathname);
    }

    public String getPathname(){
        return pathname;
    }
}
