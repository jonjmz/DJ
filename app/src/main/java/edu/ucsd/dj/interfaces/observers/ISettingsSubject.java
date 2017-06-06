package edu.ucsd.dj.interfaces.observers;

/**
 * Created by Jake Sutton on 6/1/17.
 */

public interface ISettingsSubject {
    void notifyObservers();
    void addObserver(ISettingsObserver o);
    void removeObserver(ISettingsObserver o);
}
