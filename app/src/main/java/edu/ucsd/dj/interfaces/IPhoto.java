package edu.ucsd.dj.interfaces;

/**
 * Created by jakesutton on 6/4/17.
 */

public interface IPhoto extends IEvent, IKarma {

    String getName();
    @Override
    boolean hasKarma();

    @Override
    void setHasKarma(boolean karma);

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

    IEvent getInfo();

    String getPathname();
}
