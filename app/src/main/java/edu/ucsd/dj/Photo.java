package edu.ucsd.dj;

import android.support.annotation.NonNull;

import java.io.Serializable;
import java.util.HashMap;

/**
 * Created by jonathanjimenez on 5/1/17.
 */

public class Photo implements Comparable, Serializable {
    long score;          // Used to order photos with precalulated scores
    boolean hasKarma;   // Used to keep track of karma
    boolean releasable; // Check if it is releasable, only defualt photo is not
    boolean karmaable;  // Check if it is krama-able, only defualt photo is not
    Photo previous;     // Used to return to previous photo
    String reference;   // Reference to image in album

    public Photo() {
        super();
        this.releasable = true;
        this.karmaable = true;
    }

    public Photo(boolean isDefault) {
        super();
        this.releasable = false;
        this.karmaable = false;
        this.reference = "Default Location";
    }

    /**
     * Calculates score for this photo at this time/location with these settings.
     * Used to prepare photo for sorting by photo set.
     */
    public void calculateScore() {
        // Score is distance formula in up to 4 dimensions
        long scoreSquared = 0;
        // TODO: If considering recency, add distance to photo
        if (true) {
            scoreSquared += 0;
        }
        // TODO: If considering time of day, add distance to photo
        if (true) {
            scoreSquared += 0;
        }
        // TODO: If considering location, add distance to photo
        if (true) {
            scoreSquared += 0;
        }
        // TODO: If considering karma, add distance to photo
        if (true) {
            scoreSquared += Math.pow(20000, 2); // half circumference of earth in kilimeters
        }
        // Score Calculations Here
        score = (long) Math.sqrt((double) scoreSquared);
    }

    public void setAsWallpaper() {
        // TODO: Set this photo as wallpaper
    }

    public Photo getPrevious() {
        return previous;
    }

    public void setPrevious(Photo previous) {
        this.previous = previous;
    }

    public boolean hasPrevious() {
        return previous != null;
    }

    public boolean hasKarma() {
        return hasKarma();
    }

    public boolean isKarmable() {
        return karmaable;
    }

    public boolean isReleasable() {
        return releasable;
    }

    public void giveKarma() {
        hasKarma = true;
    }

    public void removeKarma() {
        hasKarma = false;
    }

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
        return reference.equals(((Photo) o).reference);
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
        return reference.hashCode();
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
}
