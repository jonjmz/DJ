package edu.ucsd.dj.models;

import android.support.annotation.NonNull;

import com.google.firebase.database.Exclude;


import com.google.firebase.database.Exclude;

import java.io.File;
import java.io.Serializable;
import java.util.HashMap;

import edu.ucsd.dj.interfaces.models.IPhoto;
import edu.ucsd.dj.interfaces.models.IUser;

/**
 * Photo class to represent a photo
 * Created by jonathanjimenez on 5/1/17.
 */
public class Photo implements IPhoto, Comparable, Serializable {

    private double score;        // Used to order photos with precalulated scores
    private boolean hasKarma;    // Used to keep track of karma

    private String pathname;     // Reference to image in album
    private Event info;          // Store photo Exif data for score calculations

    private String uid;

    private String customLocation;
    private boolean hasCustomLocation;

    public Photo(){
        pathname = "";
        info = new Event();
        customLocation = "";

    }
    public Photo(String reference, IUser user) {
        info = new Event();
        this.pathname = reference;
        this.uid = user.getUserId() + "-" + getName();
        customLocation = "";

    }

    public String getCustomLocation() {
        return customLocation;
    }

    public void setCustomLocation(String customLocation) {
        this.customLocation = customLocation;
    }


    @Override @Exclude
    public String getName(){ return getPathname().substring(getPathname().lastIndexOf("/")+1);}

    @Exclude
    public Event getInfo() { return info; }

    public String getPathname(){ return pathname; }
    public void setPathname(String pathname) { this.pathname = pathname; }
    @Exclude
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
        return new File(pathname).getName().equals(new File(((Photo) o).pathname).getName());
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

    @Override
    public boolean getHasValidCoordinates() {
        return info.getHasValidCoordinates();
    }

    @Override
    public boolean hasValidDate() {
        return info.hasValidDate();
    }

    @Override
    public void setHasValidDate(boolean hvd) {
        info.setHasValidDate(hvd);
    }

    @Override
    public void setHasValidCoordinates(boolean hvc) {
        info.setHasValidCoordinates(hvc);
    }

    @Override
    public long getDateTime() {
        return info.getDateTime();
    }

    @Override
    public double getLatitude() {
        return info.getLatitude();
    }

    @Override
    public void setDateTime(long dateTime) {
        info.setDateTime(dateTime);
    }

    @Override
    public double getLongitude() {
        return info.getLongitude();
    }

    @Override
    public void setLatitude(double lat) {
        info.setLatitude(lat);
    }

    @Override
    public void setLongitude(double lng) {
        info.setLongitude(lng);
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public boolean isHasKarma() {
        return hasKarma;
    }

    public boolean isHasCustomLocation() {
        return hasCustomLocation;
    }

    public void setInfo(Event info) {
        this.info = info;
    }

    public void setHasCustomLocation(boolean hasCustomLocation) {
        this.hasCustomLocation = hasCustomLocation;
    }
}
