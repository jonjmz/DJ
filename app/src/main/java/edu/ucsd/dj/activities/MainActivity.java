package edu.ucsd.dj.activities;

import android.Manifest;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.Switch;

import edu.ucsd.dj.interfaces.IRating;
import edu.ucsd.dj.interfaces.LocationTrackerSubject;
import edu.ucsd.dj.others.LocationService;
import edu.ucsd.dj.others.PhotoCollection;
import edu.ucsd.dj.R;
import edu.ucsd.dj.managers.DJWallpaper;
import edu.ucsd.dj.managers.Settings;
import edu.ucsd.dj.strategies.RatingStrategy;

/**
 * to define the main activity home (settings) page
 */
public class MainActivity extends AppCompatActivity{
    private static final int READ_STORAGE_PERMISSION = 123;
    private static final int SET_WALLPAPER_PERMISSION = 69;
    private static final int ACCESS_FINE_PERMISSION = 420;

    private Switch proximitySwitch;
    private Switch timeOfDaySwitch;
    private Switch recencySwitch;
    private Switch myAlbumSwitch;
    private Switch friendsAlbumSwitch;
    private SeekBar refreshRateBar;

    @Override
    protected void onStart() {
        super.onStart();
    }


    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_main);
        askPermission(Manifest.permission.READ_EXTERNAL_STORAGE, READ_STORAGE_PERMISSION);
        askPermission(Manifest.permission.SET_WALLPAPER, SET_WALLPAPER_PERMISSION);
        askPermission(Manifest.permission.ACCESS_FINE_LOCATION, ACCESS_FINE_PERMISSION);

//        if (WidgetProvider.locationService == null) {
//            WidgetProvider.locationService = new LocationService();
//            WidgetProvider.locationService.setCurrentLocation(null);
//            WidgetProvider.locationService.connect();
//            Settings.initTimer();
//        }

        PhotoCollection.getInstance().update();

        proximitySwitch = (Switch) findViewById(R.id.proximity);
        timeOfDaySwitch = (Switch) findViewById(R.id.timeOfDay);
        recencySwitch = (Switch) findViewById(R.id.recency);
        myAlbumSwitch = (Switch) findViewById(R.id.myAlbum);
        friendsAlbumSwitch = (Switch) findViewById(R.id.friendsAlbum);
        refreshRateBar = (SeekBar) findViewById(R.id.refresh);

        proximitySwitch.setChecked(Settings.isConsideringProximity());
        timeOfDaySwitch.setChecked(Settings.isConsideringTOD());
        recencySwitch.setChecked(Settings.isConsideringRecency());
        myAlbumSwitch.setChecked(Settings.isViewingMyAlbum());
        friendsAlbumSwitch.setChecked(Settings.isViewingFriendsAlbum());

        proximitySwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            /**
             * method to set Proximity settings for the display of pics
             * sorts PhotoCollection too
             * @param buttonView the button that is either checked or not
             * @param isChecked the boolean value from buttonView
             */
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                    Settings.setConsiderProximity(true);
                else
                    Settings.setConsiderProximity(false);
                IRating newRating = createRating(Settings.isConsideringRecency(),
                        Settings.isConsideringTOD(),
                        Settings.isConsideringProximity());
                PhotoCollection.getInstance().setRatingStrategy(newRating);

                DJWallpaper.getInstance().set(PhotoCollection.getInstance().current());
            }
        });

        timeOfDaySwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                    Settings.setConsiderTOD(true);
                else
                    Settings.setConsiderTOD(false);
                IRating newRating = createRating(Settings.isConsideringRecency(),
                        Settings.isConsideringTOD(),
                        Settings.isConsideringProximity());
                PhotoCollection.getInstance().setRatingStrategy(newRating);
                DJWallpaper.getInstance().set(PhotoCollection.getInstance().current());
            }
        });

        recencySwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                    Settings.setConsiderRecency(true);
                else
                    Settings.setConsiderRecency(false);
                IRating newRating = createRating(Settings.isConsideringRecency(),
                        Settings.isConsideringTOD(),
                        Settings.isConsideringProximity());
                PhotoCollection.getInstance().setRatingStrategy(newRating);
                DJWallpaper.getInstance().set(PhotoCollection.getInstance().current());
            }
        });

        myAlbumSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                    Settings.setViewingMyAlbum(true);
                else
                    Settings.setViewingMyAlbum(false);
                IRating newRating = createRating(Settings.isConsideringRecency(),
                        Settings.isConsideringTOD(),
                        Settings.isConsideringProximity());
                PhotoCollection.getInstance().setRatingStrategy(newRating);
                DJWallpaper.getInstance().set(PhotoCollection.getInstance().current());
            }
        });

        friendsAlbumSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                    Settings.setViewingFriendsAlbum(true);
                else
                    Settings.setViewingFriendsAlbum(false);
                IRating newRating = createRating(Settings.isConsideringRecency(),
                        Settings.isConsideringTOD(),
                        Settings.isConsideringProximity());
                PhotoCollection.getInstance().setRatingStrategy(newRating);
                DJWallpaper.getInstance().set(PhotoCollection.getInstance().current());
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
                Settings.setRefreshRateMinutes(20 + seekBar.getProgress());
                Settings.initTimer();
            }
        });

        Log.i(this.getClass().toString() + ":onCreate()", "MainActivity listeners configured.");
    }

    private void askPermission(String permission, int requestCode){

        if (ContextCompat.checkSelfPermission(this, permission) !=
                PackageManager.PERMISSION_GRANTED) {


            Log.i(this.getClass().toString(), permission + " granted. Congrats DJ!");

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

    private static IRating createRating(boolean recency, boolean tod, boolean proximity){
        return new RatingStrategy(recency, tod, proximity);
    }
}
