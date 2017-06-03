package edu.ucsd.dj.interfaces;

/**
 * Created by jakesutton on 6/1/17.
 */

public interface ISettingsSubject {
    void notifyObservers();
    void addObserver(ISettingsObserver o);
    void removeObserver(ISettingsObserver o);
}
