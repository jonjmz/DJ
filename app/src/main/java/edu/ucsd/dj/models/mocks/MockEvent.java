package edu.ucsd.dj.models.mocks;

import edu.ucsd.dj.interfaces.models.IAddressable;
import edu.ucsd.dj.interfaces.models.IDateTimeable;

/**
 * Created by nguyen on 6/4/2017.
 * Class to create a mock event for unit testing
 */

public class MockEvent implements IDateTimeable, IAddressable {
    private double lat, lon;
    private long date;

    public MockEvent(){
        lat = 69;
        lon = 69;
        date = 666;
    }
    @Override
    public boolean getHasValidCoordinates() {
        return true;
    }

    @Override
    public void setHasValidCoordinates(boolean hvc) {

    }

    @Override
    public double getLatitude() {
        return lat;
    }

    @Override
    public double getLongitude() {
        return lon;
    }

    @Override
    public void setLatitude(double lat) {
        this.lat = lat;
    }

    @Override
    public void setLongitude(double lng) {
        this.lon = lng;
    }

    @Override
    public boolean hasValidDate() {
        return true;
    }

    @Override
    public void setHasValidDate(boolean hvc) {

    }

    @Override
    public long getDateTime() {
        return date;
    }

    @Override
    public void setDateTime(long dateTime) {
        this.date = dateTime;
    }
}
