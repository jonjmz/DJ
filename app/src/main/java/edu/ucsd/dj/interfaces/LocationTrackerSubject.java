package edu.ucsd.dj.interfaces;

/**
 * Created by Gus on 5/29/2017.
 */

public interface LocationTrackerSubject {
    void updateCurrentLocation();
    void addObserver(LocationTrackerObserver o);
    void removeObserver(LocationTrackerObserver o);

}
