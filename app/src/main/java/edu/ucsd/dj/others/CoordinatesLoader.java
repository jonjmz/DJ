package edu.ucsd.dj.others;

import android.media.ExifInterface;
import android.util.Log;

import java.io.IOException;

import edu.ucsd.dj.interfaces.IAddressable;

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
    public void loadCoordinatesFor(String pathname, IAddressable info) {

        Log.i("CoordinatesLoader", "Attempting to load coordinates for " + pathname);

        if (pathname.endsWith(".png") || pathname.endsWith(".gif")) return;

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
        String laRef = exifInterface.getAttribute((ExifInterface.TAG_GPS_LATITUDE_REF));
        String lg = exifInterface.getAttribute(ExifInterface.TAG_GPS_LONGITUDE);
        String lgRef = exifInterface.getAttribute((ExifInterface.TAG_GPS_LONGITUDE_REF));

        // If those strings are non-null
        if (la != null && lg != null) {

            Log.i("CoordinatesLoader", "Success: Setting location data for " + pathname);
            info.setLatitude(format(la, laRef));
            info.setLongitude(format(lg, lgRef));

            // Configure the photo object to reflect this data.
            info.setHasValidCoordinates(true);
        } else {

            Log.i("CoordinatesLoader", "Failure: Failed to get location data for " +
                    pathname + ". Setting hasValidCoordinates to false.");

            // Declare information missing for this photo.
            info.setHasValidCoordinates(false);
        }
    }

    /**
     * Includes standard formatting for coordinates
     * @param location Location of the photo
     * @param hemisphere The current hemisphere
     * @return the correct format
     */
    static double format(String location, String hemisphere){
        Double value;
        // Using DMS
        if (location.contains("/")){
            String[] bytes = location.split(",");
            String[] dBytes = bytes[0].split("/");
            String[] mBytes = bytes[1].split("/");
            String[] sBytes = bytes[2].split("/");
            double degrees = Integer.parseInt(dBytes[0]) / Integer.parseInt(dBytes[1]);
            double minutes = Integer.parseInt(mBytes[0]) / Integer.parseInt(mBytes[1]);
            double seconds = Integer.parseInt(sBytes[0]) / Integer.parseInt(sBytes[1]);

            value = degrees + minutes / 60 + seconds / (60 * 60);
        }
        // Using decimal
        else {
            value = Double.parseDouble(location);
        }
        // Fix sign based on hemisphere
        if(hemisphere.equals("N") || hemisphere.equals("E")){
            return value;
        } else {
            return 0 - value;
        }
    }

}
