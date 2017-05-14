package edu.ucsd.dj;

import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

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
    private static final int UPDATE_INTERVAL = 30000;
    private static final int FASTEST_INTERVAL = 10000;

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

    }

    public void connect(){
        mGoogleApiClient.connect();
    }
    public void disconnect(){
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnected(Bundle connectionHint) {
        //TODO SET THIS TO PERIODIC later

        try{
            Location mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                    mGoogleApiClient);
            if (mLastLocation == null)
                startLocationUpdates();

            else
                setLocation(mLastLocation);
        }
        catch(SecurityException e){
            e.printStackTrace();
        }
    }

    protected void startLocationUpdates() {
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
        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        //Using Location here
        setLocation(location);
    }
    public void setLocation(Location location){
        IRating rating = new RatingStrategy(Settings.isConsideringRecency(),
                Settings.isConsideringTOD(), Settings.isConsideringProximity());
        rating.setCurrentLocation(location);

    }
}
