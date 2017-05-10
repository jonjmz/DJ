package edu.ucsd.dj;

import android.media.ExifInterface;
import android.util.Log;

import java.io.IOException;

/**
 * Responsible for loading location from the file system for this photo.
 *
 * Created by Jake Sutton on 5/9/17.
 */
public class CoordinatesLoader {
    ExifInterface exifInterface;

    /**
     * If possible, meaning if the exif data for this photo is non-null,
     * sets the photo's latitude and longitude to whatever is saved in the
     * exif data.
     *
     * @param photo - the same photo object with exif location data saved.
     */
    public void loadCoordinatesFor(Photo photo) {

        Log.i("CoordinatesLoader", "Attempting to load coordinates for " + photo.getPathname());

        try {
            exifInterface = new ExifInterface(photo.getPathname());
        } catch (IOException e) {
            e.printStackTrace();
            Log.i("CoordinatesLoader", "Error: Opening ExifInterface failed for " +
                    photo.getPathname());

            return;
        }

        Log.i("CoordinatesLoader", "Success: Opening ExifInterface succeeded for " +
                photo.getPathname());

        // Get the actual location data! Comes as strings...
        String la = exifInterface.getAttribute(ExifInterface.TAG_GPS_LATITUDE);
        String lg = exifInterface.getAttribute(ExifInterface.TAG_GPS_LONGITUDE);

        // If those strings are non-null
        if (la != null && lg != null) {

            Log.i("CoordinatesLoader", "Success: Setting location data for " +
                    photo.getPathname());

            // Configure the photo object to reflect this data.
            photo.getInfo().setHasValidCoordinates(true);
            photo.getInfo().setLatitude(Double.parseDouble(la));
            photo.getInfo().setLongitude(Double.parseDouble(lg));
        } else {

            Log.i("CoordinatesLoader", "Failure: Failed to get location data for " +
                    photo.getPathname() + ". Setting hasValidCoordinates to false.");

            // Declare information missing for this photo.
            photo.getInfo().setHasValidCoordinates(false);
        }

    }
}
