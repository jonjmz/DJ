package edu.ucsd.dj;

import android.location.Address;
import android.util.Log;

/**
 * Responsible for providing labels for photos that have valid location data.
 *
 * Created by Jake Sutton on 5/9/17.
 */
public class PhotoLabeler {

    /**
     * Returns string corresponding to the geographic location described
     * by the data saved in the photo object 'photo'.
     *
     * @param photo
     * @return Written location of this image.
     */
    public String label(Photo photo) {

        Log.i("PhotoLabeler", "Attempting to get label components for " + photo.getPathname());

        // Default to 'Location Unknown'
        String result = "Location Unknown";
        Address address = photo.getAddress();

        // Save relevant pieces of information.
        String place = address.getAddressLine(0);
        String city = address.getLocality();
        String state = address.getAdminArea();
        String country = address.getCountryName();

        // If the photo has valid location data...
        if (photo.hasValidCoordinates()) {


            Log.i("PhotoLabeler", "Success. Attempting to build label for " + photo.getPathname());

            // Order and concatenate information in order of specificity.
            if (country != null && !country.isEmpty()) result = country;
            if (state != null && !state.isEmpty()) result = state + ", " + result;
            if (city != null && !city.isEmpty()) result = city + ", " + result;
            if (place != null && !place.isEmpty()) result = place + ", " + result;
        }

        Log.i("PhotoLabeler", "Success. Returning label for " + photo.getPathname() + " -> " +
            result);

        return result;
    }
}
