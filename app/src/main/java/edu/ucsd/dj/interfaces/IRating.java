package edu.ucsd.dj.interfaces;

import android.location.Location;
import edu.ucsd.dj.models.Event;

/**
 * Created by nguyen on 5/13/2017.
 */

public interface IRating extends ISettingsObserver {
    double rate(Event info, boolean karma);
    void setCurrentLocation(IAddressable location);

    @Override
    void update();
}
