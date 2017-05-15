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
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import edu.ucsd.dj.DJWallpaper;
import edu.ucsd.dj.Photo;
import edu.ucsd.dj.PhotoCollection;
import edu.ucsd.dj.R;
import edu.ucsd.dj.Settings;

public class MainActivity extends AppCompatActivity{
    private static final int READ_STORAGE_PERMISSION = 123;
    private static final int SET_WALLPAPER_PERMISSION = 69;
    private static final int ACCESS_FINE_PERMISSION = 420;

    private Switch proximitySwitch;
    private Switch timeOfDaySwitch;
    private Switch recencySwitch;
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

        proximitySwitch = (Switch) findViewById(R.id.proximity);
        timeOfDaySwitch = (Switch) findViewById(R.id.timeOfDay);
        recencySwitch = (Switch) findViewById(R.id.recency);
        refreshRateBar = (SeekBar) findViewById(R.id.refresh);

        proximitySwitch.setChecked(Settings.isConsideringProximity());
        timeOfDaySwitch.setChecked(Settings.isConsideringTOD());
        recencySwitch.setChecked(Settings.isConsideringRecency());

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
