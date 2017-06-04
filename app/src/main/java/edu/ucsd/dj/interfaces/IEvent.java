package edu.ucsd.dj.interfaces;

/**
 * Created by jakesutton on 6/4/17.
 */

public interface IEvent extends IAddressable, IDateTimeable {

    @Override
    boolean hasValidCoordinates();

    @Override
    boolean hasValidDate();

    @Override
    void setHasValidDate(boolean hvc);

    @Override
    void setHasValidCoordinates(boolean hvc);

    @Override
    long getDateTime();

    @Override
    double getLatitude();

    @Override
    void setDateTime(long dateTime);

    @Override
    double getLongitude();

    @Override
    void setLatitude(double lat);

    @Override
    void setLongitude(double lng);
}
