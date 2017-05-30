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

import edu.ucsd.dj.interfaces.IAddressable;
import edu.ucsd.dj.interfaces.ILocationService;
import edu.ucsd.dj.interfaces.IRating;
import edu.ucsd.dj.interfaces.LocationTrackerSubject;
import edu.ucsd.dj.managers.DJPhoto;
import edu.ucsd.dj.managers.DJWallpaper;
import edu.ucsd.dj.managers.Settings;
import edu.ucsd.dj.models.Event;
import edu.ucsd.dj.strategies.RatingStrategy;

/**
 * Tracks the current location and updates if the distance is significant using
 * googleAPI
 * Created by nguyen on 5/14/2017.
 */

public class LocationService implements GoogleApiClient.ConnectionCallbacks
        , GoogleApiClient.OnConnectionFailedListener, LocationListener, ILocationService,
        LocationTrackerSubject {
    private static final int UPDATE_INTERVAL = 5000;
    private static final int FASTEST_INTERVAL = 2000;
    private static final float MIN_DISTANCE = 152;

    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private IAddressable loc;

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
        // Set the update interval to 5 seconds
        mLocationRequest.setInterval(UPDATE_INTERVAL);
        // Set the fastest update interval to 1 second
        mLocationRequest.setFastestInterval(FASTEST_INTERVAL);
        Log.i("Location update info: ", "Update interval: " + UPDATE_INTERVAL + "Fastest interval: "
        + FASTEST_INTERVAL);

        Location defaultLoc = new Location("");
        loc = new Event();
        loc.setLatitude(defaultLoc.getLatitude());
        loc.setLongitude(defaultLoc.getLongitude());
    }

    /**
     * Establish connection
     */
    public void connect(){
        Log.i("Location update info: ", "Enable connection");
        mGoogleApiClient.connect();
    }

    /**
     * Disable connection
     */
    public void disconnect(){
        Log.i("Location update info: ", "Disable connection");
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
        Location currentLocation = new Location(loc.getLatitude(), loc.getLongitude());
        Location.distanceBetween(currentLocation.getLatitude(),
                currentLocation.getLongitude(), location.getLatitude(), location.getLongitude(),
                distance);

        if(distance[0] > MIN_DISTANCE){
            Log.i("Location changed. ", "New latitude: " + location.getLatitude() + "  New longitude: "
                    + location.getLongitude());
            setCurrentLocation(location);
            PhotoCollection.getInstance().update();
            DJWallpaper.getInstance().set(PhotoCollection.getInstance().current());

        }
        //LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        //Using Location here
    }
    public IAddressable getCurrentLocation(){
        return loc;
    }

    @Override
    public void updateCurrentLocation() {

    }

}
