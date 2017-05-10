package edu.ucsd.dj;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import android.location.Address;
import android.support.annotation.NonNull;

import java.io.Serializable;
import java.util.HashMap;

/**
 * Created by jonathanjimenez on 5/1/17.
 */

public class Photo implements Comparable, Serializable {
    private double score;        // Used to order photos with precalulated scores
    private String pathname;    // Reference to image in album
    private PhotoInfo info; // Store photo Exif data for score calculations

    private boolean locked; // Applies to default photo, keeps it from being released or karmaed
    private boolean released;    // Check if it is released
    private boolean hasKarma;    // Used to keep track of karma

    public Photo() {
        this.locked = true;
        this.pathname = "android.resource://"+BuildConfig.APPLICATION_ID+"/" + R.drawable.dejaphotodefault;
    }

    public Photo(String reference, long dateTaken) {
        // TODO - Log something in here.
        this.info = new PhotoInfo(dateTaken);
        this.locked = false;
        this.pathname = reference;
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
        if (Settings.isConsideringRecency()) {
            // Get current time to compare with.
            // Calculates the ratio of the actual age over the possible age.
            double ratio = (now - info.getDateTaken()) / (double)now;
            scoreSquared += Math.pow(ratio, 2);
        }
        // If considering time of day, add distance to photo
        if (Settings.isConsideringTOD()) {
            if(info.getTimeOfDay() != PhotoInfo.TimeOfDay.getTimeOfDay(now))
                scoreSquared += 1;
        }
        // If considering address
        if (Settings.isConsideringProximity()) {
            scoreSquared += Math.pow(0, 2);
        }
        // We always consider karma
        if (!hasKarma) scoreSquared += 1;

        // Score Calculations Here
        score = Math.sqrt(scoreSquared);
    }

    public PhotoInfo getInfo() { return info; }

    public String getPathname(){
        return pathname;
    }

    public double getScore(){ return score; }

    public boolean hasKarma() { return hasKarma; }

    public boolean isKarmable() { return !locked; }

    public void giveKarma() { hasKarma = true; }

    public void removeKarma() { hasKarma = false; }

    public boolean isReleasable() { return !locked; }

    public boolean isReleased() { return released; }

    public void release() { released = true; }


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
}
