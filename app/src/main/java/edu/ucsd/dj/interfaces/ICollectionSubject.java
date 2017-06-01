package edu.ucsd.dj.interfaces;

/**
 * Created by Jake Sutton on 6/1/17.
 */
public interface ICollectionSubject {
    void notifyObservers();
    void addObserver(ICollectionObserver o);
    void removeObserver(ICollectionObserver o);
}
