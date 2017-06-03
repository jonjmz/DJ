package edu.ucsd.dj.others;

import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import java.util.ArrayList;

import edu.ucsd.dj.interfaces.IAddressable;
import edu.ucsd.dj.interfaces.ILocationTrackerObserver;
import edu.ucsd.dj.interfaces.LocationTrackerSubject;
import edu.ucsd.dj.managers.DJPhoto;
import edu.ucsd.dj.models.Event;

/**
 * Tracks the current location and updates if the distance is significant using
 * googleAPI
 * Created by nguyen on 5/14/2017.
 */

public class LocationService implements GoogleApiClient.ConnectionCallbacks
        , GoogleApiClient.OnConnectionFailedListener, LocationListener,
        LocationTrackerSubject{
    private static final int UPDATE_INTERVAL = 5000;
    private static final int FASTEST_INTERVAL = 2000;
    private static final float MIN_DISTANCE = 152;

    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private IAddressable loc;

    private ArrayList<ILocationTrackerObserver> obs;

    @Override
    public void addObserver(ILocationTrackerObserver o) {
        obs.add(o);
    }

    @Override
    public void removeObserver(ILocationTrackerObserver o) {
        obs.remove(o);
    }

    /**
     * Default constructor, init Google Api
     */
    public LocationService(){

        // Create an instance of GoogleAPIClient.

        mGoogleApiClient = new GoogleApiClient.Builder(DJPhoto.getAppContext())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        mLocationRequest = new LocationRequest();
        // Use high accuracy
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        // Set the interval to 5 seconds
        mLocationRequest.setInterval(UPDATE_INTERVAL);
        // Set the fastest updateLocation interval to 1 second
        mLocationRequest.setFastestInterval(FASTEST_INTERVAL);
        //Log.i("Location info: ", "Update interval: " + UPDATE_INTERVAL + "Fastest interval: "
        //+ FASTEST_INTERVAL);

        //TODO make a different adapter for location for better encapsulation
        Location defaultLoc = new Location("");
        loc = new Event();
        loc.setLatitude(defaultLoc.getLatitude());
        loc.setLongitude(defaultLoc.getLongitude());
        connect();
        obs = new ArrayList<>();
    }

    /**
     * Establish connection
     */
    public void connect(){
        //Log.i("Location info: ", "Enable connection");
        mGoogleApiClient.connect();
    }

    /**
     * Disable connection
     */
    public void disconnect(){
        Log.i("Location info: ", "Disable connection");
        mGoogleApiClient.disconnect();
    }

    @Override
    public void onConnected(Bundle connectionHint) {

        try{
            LocationServices.FusedLocationApi.requestLocationUpdates(
                    mGoogleApiClient, mLocationRequest, this);
        }
        catch(SecurityException e){
            e.printStackTrace();
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    /**
     * Listener for on location changed
     * @param location the new location
     */
    @Override
    public void onLocationChanged(Location location) {

        float distance[] = new float[1];
        double lon = location.getLongitude();
        double lat = location.getLatitude();

        //Function to calculate distance between 2 coordinates
        Location.distanceBetween(loc.getLatitude(),
                loc.getLongitude(), lon, lat,
                distance);

        if(distance[0] > MIN_DISTANCE){
            Log.i("Location changed. ", "New latitude: " + lat + "  New longitude: "
                    + lon);
            loc.setLongitude(lon);
            loc.setLatitude(lat);
            updateCurrentLocation();
        }
    }
    @Override
    public void updateCurrentLocation() {
        for(ILocationTrackerObserver o: obs){
            o.updateLocation(loc);
        }
    }

}
