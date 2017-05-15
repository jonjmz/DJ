package edu.ucsd.dj.interfaces;

import android.location.Location;
import edu.ucsd.dj.Event;

/**
 * Created by nguyen on 5/13/2017.
 */

public interface IRating {
    double rate(Event info, boolean karma);
    //TODO Refactor this into IAddressable
    void setCurrentLocation(Location location);
    Location getCurrentLocation();
}
