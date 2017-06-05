package edu.ucsd.dj.interfaces;

/**
 * Created by jakesutton on 6/4/17.
 */

public interface IRatingSubject {
    void addObserver(IRatingObserver o);
    void removeObserver(IRatingObserver o);
    void ratingStrategyChanged();
}
