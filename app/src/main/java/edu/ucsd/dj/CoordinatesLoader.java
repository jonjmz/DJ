package edu.ucsd.dj;

import android.media.ExifInterface;
import android.util.Log;

import java.io.IOException;

import edu.ucsd.dj.interfaces.Addressable;

/**
 * Responsible for loading location from the file system for this photo.
 *
 * Created by Jake Sutton on 5/9/17.
 */
public class CoordinatesLoader {
    private ExifInterface exifInterface;

    /**
     * If possible, meaning if the exif data for this photo is non-null,
     * sets the photo's latitude and longitude to whatever is saved in the
     * exif data.
     */
    public void loadCoordinatesFor(String pathname, Addressable info) {

        Log.i("CoordinatesLoader", "Attempting to load coordinates for " + pathname);

        try {
            exifInterface = new ExifInterface(pathname);
        } catch (IOException e) {
            e.printStackTrace();
            Log.i("CoordinatesLoader", "Error: Opening ExifInterface failed for " + pathname);

            return;
        }

        Log.i("CoordinatesLoader", "Success: Opening ExifInterface succeeded for " + pathname);

        // Get the actual location data! Comes as strings...
        String la = exifInterface.getAttribute(ExifInterface.TAG_GPS_LATITUDE);
        String lg = exifInterface.getAttribute(ExifInterface.TAG_GPS_LONGITUDE);

        // If those strings are non-null
        if (la != null && lg != null) {

            Log.i("CoordinatesLoader", "Success: Setting location data for " + pathname);

            // Configure the photo object to reflect this data.
            info.setHasValidCoordinates(true);
            info.setLatitude(Double.parseDouble(la));
            info.setLongitude(Double.parseDouble(lg));
        } else {

            Log.i("CoordinatesLoader", "Failure: Failed to get location data for " +
                    pathname + ". Setting hasValidCoordinates to false.");

            // Declare information missing for this photo.
            info.setHasValidCoordinates(false);
        }
    }
}
