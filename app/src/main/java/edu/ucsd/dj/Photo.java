package edu.ucsd.dj;

import android.location.Location;
import android.location.LocationManager;
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

    private boolean hasKarma;    // Used to keep track of karma

    public Photo(String reference, long dateTaken) {
        this.info = new PhotoInfo(dateTaken);
        this.pathname = reference;
    }

    /**
     * Calculates score for this photo at this time/generateAddress with these settings.
     * Used to prepare photo for sorting by photo set. Implemented as distance function
     * in up to four dimensions
     */


    public PhotoInfo getInfo() { return info; }

    public String getPathname(){ return pathname; }

    public double getScore(){ return score; }
    public void setScore(double score) { this.score = score; }
    public boolean hasKarma() { return hasKarma; }

    public void setHasKarma(boolean karma) { this.hasKarma = karma; }


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
}
