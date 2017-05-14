package edu.ucsd.dj.activities;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.Switch;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import edu.ucsd.dj.DJWallpaper;
import edu.ucsd.dj.Photo;
import edu.ucsd.dj.PhotoCollection;
import edu.ucsd.dj.R;
import edu.ucsd.dj.RatingStrategy;
import edu.ucsd.dj.Settings;
import edu.ucsd.dj.interfaces.IRating;

//public class MainActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks
//    , GoogleApiClient.OnConnectionFailedListener, LocationListener{
public class MainActivity extends AppCompatActivity{
    private static final int READ_STORAGE_PERMISSION = 123;
    private static final int SET_WALLPAPER_PERMISSION = 69;
    private static final int ACCESS_FINE_PERMISSION = 420;

//    private static final int UPDATE_INTERVAL = 30000;
//    private static final int FASTEST_INTERVAL = 10000;

    private Switch proximitySwitch;
    private Switch timeOfDaySwitch;
    private Switch recencySwitch;
    private SeekBar refreshRateBar;

//    private GoogleApiClient mGoogleApiClient;
//    private LocationRequest mLocationRequest;

    @Override
    protected void onStart() {
        super.onStart();
        //mGoogleApiClient.connect();
//        PhotoCollection collection = PhotoCollection.getInstance();
//
//        if (collection.isEmpty()) {
//            DJWallpaper.getInstance().setDefault();
//        } else {
//            Photo photo = collection.current();
//            DJWallpaper.getInstance().set(photo);
//        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        //mGoogleApiClient.disconnect();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_main);
        askPermission(Manifest.permission.READ_EXTERNAL_STORAGE, READ_STORAGE_PERMISSION);
        askPermission(Manifest.permission.SET_WALLPAPER, SET_WALLPAPER_PERMISSION);
        askPermission(Manifest.permission.ACCESS_FINE_LOCATION, ACCESS_FINE_PERMISSION);
        //askPermission(Manifest.permission.ACCESS_COARSE_LOCATION, ACCESS_COARSE_PERMISSION);


        //Creating an instance of google play
        // some kind of location class, adds overlay, returns bitmap

    // Create an instance of GoogleAPIClient.
//        mGoogleApiClient = new GoogleApiClient.Builder(this)
//                    .addConnectionCallbacks(this)
//                    .addOnConnectionFailedListener(this)
//                    .addApi(LocationServices.API)
//                    .build();
//
//        mLocationRequest = new LocationRequest();
//        // Use high accuracy
//        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
//        // Set the update interval to 5 seconds
//        mLocationRequest.setInterval(UPDATE_INTERVAL);
//        // Set the fastest update interval to 1 second
//        mLocationRequest.setFastestInterval(FASTEST_INTERVAL);


        proximitySwitch = (Switch) findViewById(R.id.proximity);
        timeOfDaySwitch = (Switch) findViewById(R.id.timeOfDay);
        recencySwitch = (Switch) findViewById(R.id.recency);
        refreshRateBar = (SeekBar) findViewById(R.id.refresh);
        // customAlbumSwitch = (Switch) findViewById(R.id.customAlbum);

        proximitySwitch.setChecked(Settings.isConsideringProximity());
        timeOfDaySwitch.setChecked(Settings.isConsideringTOD());
        recencySwitch.setChecked(Settings.isConsideringRecency());
        // customAlbumSwitch.setChecked(false);

        proximitySwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                    Settings.setConsiderProximity(true);
                else
                    Settings.setConsiderProximity(false);
                PhotoCollection.getInstance().sort();
            }
        });

        timeOfDaySwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                    Settings.setConsiderTOD(true);
                else
                    Settings.setConsiderTOD(false);
                PhotoCollection.getInstance().sort();
            }
        });

        recencySwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                    Settings.setConsiderRecency(true);
                else
                    Settings.setConsiderRecency(false);
                PhotoCollection.getInstance().sort();
            }
        });

        refreshRateBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // nothing
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                Settings.setRefreshRateMinutes(1 + seekBar.getProgress());
            }
        });


        /*
        customAlbumSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                    settings.setUsingCustomAlbum(true);
                else
                    settings.setUsingCustomAlbum(false);
            }
        });
        */
        //TODO handle exception better

        //setLocation(null);
        //PhotoCollection.getInstance().update();

    }
    private void askPermission(String permission, int requestCode){
        if (ContextCompat.checkSelfPermission(this,
                permission)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    permission)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(this,
                        new String[]{permission},
                        requestCode);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case READ_STORAGE_PERMISSION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }
            case SET_WALLPAPER_PERMISSION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

//    @Override
//    public void onConnected(Bundle connectionHint) {
//        //TODO SET THIS TO PERIODIC later
//
//        try{
//            Location mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
//                    mGoogleApiClient);
//            if (mLastLocation == null)
//                startLocationUpdates();
//
//            else
//                setLocation(mLastLocation);
//        }
//        catch(SecurityException e){
//            e.printStackTrace();
//        }
//    }
//
//    protected void startLocationUpdates() {
//        try{
//            LocationServices.FusedLocationApi.requestLocationUpdates(
//                    mGoogleApiClient, mLocationRequest, this);
//        }
//        catch(SecurityException e){
//            e.printStackTrace();
//        }
//    }
//
//    @Override
//    public void onConnectionSuspended(int i) {
//
//    }
//
//    @Override
//    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
//
//    }
//    @Override
//    public void onLocationChanged(Location location) {
//        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
//        //Using Location here
//        setLocation(location);
//    }
//    public void setLocation(Location location){
//        IRating rating = new RatingStrategy(Settings.isConsideringRecency(),
//                Settings.isConsideringTOD(), Settings.isConsideringProximity());
//        rating.setCurrentLocation(location);
//
//    }

}
