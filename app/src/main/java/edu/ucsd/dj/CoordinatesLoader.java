package edu.ucsd.dj;

import android.media.ExifInterface;

import java.io.IOException;

/**
 * Created by jakesutton on 5/9/17.
 */
public class CoordinatesLoader {
    ExifInterface exifInterface;

    /**
     * TODO - Need to write description here before committing.
     *
     * @param photo
     */
    public void loadCoordinatesFor(Photo photo) {

        try {
            exifInterface = new ExifInterface(photo.getPathname());
        } catch (IOException e) {
            e.printStackTrace();

            // TODO log some kind of exception

            return;
        }

        try {
            exifInterface = new ExifInterface(photo.getPathname());
        } catch (IOException e) {
            e.printStackTrace();
        }

        String la = exifInterface.getAttribute(ExifInterface.TAG_GPS_LATITUDE);
        String lg = exifInterface.getAttribute(ExifInterface.TAG_GPS_LONGITUDE);

        // Checking for duplicate
        if (la != null && lg != null) {
            photo.setHasValidCoordinates(true);
            photo.setLatitude(Double.parseDouble(la));
            photo.setLongitude(Double.parseDouble(lg));
        } else {
            photo.setHasValidCoordinates(false);
        }

    }
}
