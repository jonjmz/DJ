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

import java.util.LinkedList;
import java.util.List;

import edu.ucsd.dj.R;

import edu.ucsd.dj.interfaces.IRating;
import edu.ucsd.dj.interfaces.IRemotePhotoStore;
import edu.ucsd.dj.interfaces.models.IFriendList;
import edu.ucsd.dj.interfaces.models.IUser;
import edu.ucsd.dj.models.DJFriends;
import edu.ucsd.dj.models.DJPrimaryUser;
import edu.ucsd.dj.models.FirebaseDB;
import edu.ucsd.dj.strategies.RatingStrategy;

import edu.ucsd.dj.others.PhotoCollection;
import edu.ucsd.dj.managers.DJWallpaper;
import edu.ucsd.dj.managers.Settings;

/**
 * Main activity. The 'Settings' page.
 */
public class MainActivity extends AppCompatActivity{
    private static final int READ_STORAGE_PERMISSION = 123;
    private static final int SET_WALLPAPER_PERMISSION = 69;
    private static final int ACCESS_FINE_PERMISSION = 420;

    private Switch
            proximitySwitch,
            timeOfDaySwitch,
            recencySwitch,
            myAlbumSwitch,
            friendsAlbumSwitch;

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
        askPermission(Manifest.permission.READ_CONTACTS, READ_STORAGE_PERMISSION);
        askPermission(Manifest.permission.GET_ACCOUNTS, READ_STORAGE_PERMISSION);

        IRating rating = new RatingStrategy(
                Settings.getInstance().isConsideringRecency(),
                Settings.getInstance().isConsideringTOD(),
                Settings.getInstance().isConsideringProximity());

        Settings.getInstance().addObserver( rating );

        PhotoCollection collection = PhotoCollection.getInstance();
        collection.addObserver( DJWallpaper.getInstance() );
        collection.setRatingStrategy( rating );
        collection.update();

        final IUser primaryUser = new DJPrimaryUser();
        IRemotePhotoStore ps = new FirebaseDB();


        ps.getAllFriendsPhotos(new IFriendList() {
            @Override
            public List<IUser> getFriends() {
                LinkedList<IUser> result = new LinkedList();
                result.add(primaryUser);
                return result;
            }
        });
        ps.uploadPhotos( primaryUser, collection.getAlbum() );
        ps.getPhotos(primaryUser);

        proximitySwitch = (Switch) findViewById(R.id.proximity);
        timeOfDaySwitch = (Switch) findViewById(R.id.timeOfDay);
        recencySwitch = (Switch) findViewById(R.id.recency);
        myAlbumSwitch = (Switch) findViewById(R.id.myAlbum);
        friendsAlbumSwitch = (Switch) findViewById(R.id.friendsAlbum);
        refreshRateBar = (SeekBar) findViewById(R.id.refresh);

        proximitySwitch.setChecked(Settings.getInstance().isConsideringProximity());
        timeOfDaySwitch.setChecked(Settings.getInstance().isConsideringTOD());
        recencySwitch.setChecked(Settings.getInstance().isConsideringRecency());
        myAlbumSwitch.setChecked(Settings.getInstance().isViewingMyAlbum());
        friendsAlbumSwitch.setChecked(Settings.getInstance().isViewingFriendsAlbum());

        proximitySwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            /**
             * method to set Proximity settings for the display of pics
             * sorts PhotoCollection too
             * @param buttonView the button that is either checked or not
             * @param isChecked the boolean value from buttonView
             */
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Settings.getInstance().setConsiderProximity(isChecked);
            }
        });

        timeOfDaySwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Settings.getInstance().setConsiderTOD(isChecked);
            }
        });

        recencySwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Settings.getInstance().setConsiderRecency(isChecked);
            }
        });

        myAlbumSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Settings.getInstance().setViewingMyAlbum(isChecked);
            }
        });

        friendsAlbumSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Settings.getInstance().setViewingFriendsAlbum(isChecked);
            }
        });

        refreshRateBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                Settings.getInstance().setRefreshRateMinutes(20 + seekBar.getProgress());
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
}
