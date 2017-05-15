package edu.ucsd.dj;

import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import edu.ucsd.dj.interfaces.IRating;

/**
 * Created by nguyen on 5/14/2017.
 */

public class LocationProvider implements GoogleApiClient.ConnectionCallbacks
        , GoogleApiClient.OnConnectionFailedListener, LocationListener {
    private static final int UPDATE_INTERVAL = 5000;
    private static final int FASTEST_INTERVAL = 2000;
    private static final float MIN_DISTANCE = 152;

    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;

    public LocationProvider(){
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

    }

    public void connect(){
        Log.i("Location update info: ", "Enable connection");
        mGoogleApiClient.connect();
    }
    public void disconnect(){
        Log.i("Location update info: ", "Disable connection");
        mGoogleApiClient.disconnect();
    }

    @Override
    public void onConnected(Bundle connectionHint) {
        //TODO SET THIS TO PERIODIC later

        try{
            Location mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                    mGoogleApiClient);
            boolean isLocationNull = mLastLocation == null;
            Log.i("Requesting location", "Is it null? The answer is: " + isLocationNull);
            if (isLocationNull)
                startLocationUpdates();

            else
                setCurrentLocation(mLastLocation);
        }
        catch(SecurityException e){
            e.printStackTrace();
        }
    }

    protected void startLocationUpdates() {

        Log.i("LocationProvider ", "Requesting location update");
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
    @Override
    public void onLocationChanged(Location location) {
        float distance[] = new float[1];
        Location currentLocation = getCurrentLocation();
        Location.distanceBetween(currentLocation.getLatitude(),
                currentLocation.getLongitude(), location.getLatitude(), location.getLongitude(),
                distance);

        if(distance[0] > MIN_DISTANCE){
            Log.i("Location changed. ", "New latitude: " + location.getLatitude() + "  New longitude: "
                    + location.getLongitude());
            setCurrentLocation(location);

        }
        //LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        //Using Location here
    }
    public void setCurrentLocation(Location location){
        IRating rating = new RatingStrategy(Settings.isConsideringRecency(),
                Settings.isConsideringTOD(), Settings.isConsideringProximity());
        rating.setCurrentLocation(location);
    }
    public Location getCurrentLocation(){
        IRating rating = new RatingStrategy(Settings.isConsideringRecency(),
                Settings.isConsideringTOD(), Settings.isConsideringProximity());
        return rating.getCurrentLocation();
    }
}
