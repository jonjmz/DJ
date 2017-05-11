package edu.ucsd.dj;

import android.Manifest;
import android.app.WallpaperManager;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import java.io.IOException;


public class MainActivity extends AppCompatActivity {

    private static final int READ_STORAGE_PERMISSION = 123;
    private static final int SET_WALLPAPER_PERMISSION = 69;
    Settings settings;
    private Switch proximitySwitch;
    private Switch timeOfDaySwitch;
    private Switch recencySwitch;
//    private Switch customAlbumSwitch;

    @Override
    protected void onStart() {
        super.onStart();

        if(PhotoCollection.getInstance().next() == null){
            //TODO refactor later
            try {
                Bitmap defaultPhoto = BitmapFactory.decodeResource(getResources(),
                        R.drawable.dejaphotodefault);
                WallpaperManager.getInstance(this).setBitmap( defaultPhoto);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_main);
        askPermission(Manifest.permission.READ_EXTERNAL_STORAGE, READ_STORAGE_PERMISSION);
        askPermission(Manifest.permission.SET_WALLPAPER, SET_WALLPAPER_PERMISSION);

        PhotoCollection.getInstance().update(getApplicationContext());
        // some kind of location class, adds overlay, returns bitmap

        proximitySwitch = (Switch) findViewById(R.id.proximity);
        timeOfDaySwitch = (Switch) findViewById(R.id.timeOfDay);
        recencySwitch = (Switch) findViewById(R.id.recency);
//        customAlbumSwitch = (Switch) findViewById(R.id.customAlbum);

        proximitySwitch.setChecked(true); //Default is true.
        timeOfDaySwitch.setChecked(true); //Default is true.
        recencySwitch.setChecked(true); //Default is true.
//        customAlbumSwitch.setChecked(false); //Default is true.

        proximitySwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                    settings.setConsiderProximity(true);
                else
                    settings.setConsiderProximity(false);
            }
        });

        timeOfDaySwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                    settings.setConsiderTOD(true);
                else
                    settings.setConsiderTOD(false);
            }
        });

        recencySwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                    settings.setConsiderRecency(true);
                else
                    settings.setConsiderRecency(false);
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

}
