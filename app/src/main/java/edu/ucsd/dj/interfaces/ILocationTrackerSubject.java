package edu.ucsd.dj.interfaces;

/**
 * Created by Gus on 5/29/2017.
 */

public interface ILocationTrackerSubject {
    void updateCurrentLocation();
    void addObserver(ILocationTrackerObserver o);
    void removeObserver(ILocationTrackerObserver o);

}
