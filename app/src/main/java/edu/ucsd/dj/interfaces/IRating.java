package edu.ucsd.dj.interfaces;

import android.content.Context;
import android.location.Location;

import edu.ucsd.dj.PhotoInfo;

/**
 * Created by nguyen on 5/13/2017.
 */

public interface IRating {
    double rate(PhotoInfo info, boolean karma);
    void setCurrentLocation(Location location);
}
