package edu.ucsd.dj.interfaces;

import edu.ucsd.dj.models.Event;

/**
 * Created by nguyen on 5/13/2017.
 */

public interface IRating extends ISettingsObserver, IRatingSubject {

    double rate(Event info, boolean karma);
    void setCurrentLocation(IAddressable location);

    @Override // From SettingsObserver
    void update();

    @Override // From RatingSubject
    void addObserver(IRatingObserver o);

    @Override // From RatingSubject
    void removeObserver(IRatingObserver o);

    @Override // From RatingSubject
    void ratingStrategyChanged();
}
