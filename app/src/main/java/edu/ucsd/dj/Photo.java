package edu.ucsd.dj;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;

import java.io.Serializable;
import java.util.HashMap;

/**
 * Created by jonathanjimenez on 5/1/17.
 */

public class Photo implements Comparable, Serializable {
    private double score;       // Used to order photos with precalulated scores
    private boolean hasKarma;   // Used to keep track of karma
    private boolean releasable; // Check if it is releasable, only defualt photo is not
    private boolean released;   // Check if it is released
    private boolean karmaable;  // Check if it is krama-able, only defualt photo is not
    private int dateTaken;      // Stores date image was taken
    private String pathname;    // Reference to image in album

    public Photo() {
        this.releasable = false;
        this.karmaable = false;
        this.pathname = "Default Location";
    }

    public Photo(String reference, int dateTaken) {
        this.dateTaken = dateTaken;
        this.releasable = true;
        this.karmaable = true;
        this.pathname = reference;
    }

    /**
     * Calculates score for this photo at this time/location with these settings.
     * Used to prepare photo for sorting by photo set. Implemented as distance function
     * in up to four dimensions
     */
    public void calculateScore() {
        long scoreSquared = 0;
        // If considering recency, add distance to photo
        if (true) {
            // Get current time to compare with.
            long  now = System.currentTimeMillis();
            // Calculates the ratio of the actual age over the possible age.
            double ratio = (now - dateTaken) / dateTaken;
            scoreSquared += Math.pow(ratio, 2);
        }
        // TODO: If considering time of day, add distance to photo
        if (false) {
            scoreSquared += Math.pow(0, 2);
        }
        // TODO: If considering location, add distance to photo
        if (false) {
            scoreSquared += Math.pow(0, 2);
        }
        // TODO: If considering karma, add distance to photo
        if (false) {
            if (!hasKarma) scoreSquared += Math.pow(1, 2);
        }
        // Score Calculations Here
        score = (long) Math.sqrt((double) scoreSquared);
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

    public int getDateTaken() { return dateTaken; }

    public double getScore(){ return score; }
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
