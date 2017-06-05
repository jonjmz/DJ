package edu.ucsd.dj.interfaces.observers;

import edu.ucsd.dj.interfaces.models.IAddressable;

/**
 * Created by Gus on 5/30/2017.
 */

public interface ILocationTrackerObserver {
     void updateLocation(IAddressable loc);
}
