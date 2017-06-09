package edu.ucsd.dj.interfaces.models;

/**
 * Created by Jake Sutton on 6/4/17.
 * Interface for photo attributes
 */

public interface IPhoto extends IEvent, IKarma {
    IEvent getInfo();
    String getName();
}
