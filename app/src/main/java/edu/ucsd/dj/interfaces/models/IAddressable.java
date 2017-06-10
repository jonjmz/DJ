package edu.ucsd.dj.interfaces.models;

/**
 * Created by jakesutton on 5/13/17.
 * Interface for address data on picture
 */
public interface IAddressable {

    boolean getHasValidCoordinates();
    void setHasValidCoordinates(boolean hvc);

    double getLatitude();
    double getLongitude();

    void setLatitude(double lat);
    void setLongitude(double lng);
}
