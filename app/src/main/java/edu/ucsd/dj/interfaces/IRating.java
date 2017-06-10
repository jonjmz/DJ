package edu.ucsd.dj.interfaces;

import edu.ucsd.dj.interfaces.models.IAddressable;
import edu.ucsd.dj.interfaces.models.IPhoto;
import edu.ucsd.dj.interfaces.observers.IRatingObserver;
import edu.ucsd.dj.interfaces.observers.IRatingSubject;
import edu.ucsd.dj.interfaces.observers.ISettingsObserver;

/**
 * Created by nguyen on 5/13/2017.
 * Interface for setting address with current location
 */

public interface IRating extends ISettingsObserver, IRatingSubject {
    double rate(IPhoto photo);
    void setCurrentLocation(IAddressable location);
    public IAddressable getCurrentLocation();
}
