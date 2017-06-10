package edu.ucsd.dj.interfaces.observers;

/**
 * Created by Jake Sutton on 6/4/17.
 */

public interface IRatingSubject {
    void addObserver(IRatingObserver o);
    void removeObserver(IRatingObserver o);
    void ratingStrategyChanged();
}
