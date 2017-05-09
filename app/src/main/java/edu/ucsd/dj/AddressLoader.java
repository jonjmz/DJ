package edu.ucsd.dj;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.util.Log;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 * Responsible for loading Human readable location information from the
 * latitude and longitude coordinates saved in a photo object.
 *
 * Created by Jake Sutton on 5/9/17.
 */
public class AddressLoader {

    Geocoder coder;

    /**
     * Creates an AddressLoader object, taking the current application context, and setting the
     * locale to the phone's default locale.
     */
    public AddressLoader(Context context) {
        coder = new Geocoder( context, Locale.getDefault() );
    }

    /**
     * Uses builtin Geocoder to get an address object corresponding to the location this
     * photo's latitude and longitude.
     *
     * @param photo
     */
    public void loadAddressFor( Photo photo ) {

        // Don't do anything if the photo doesn't have valid location information.
        if (!photo.hasValidCoordinates()) {

            Log.i("AddressLoader", "Attempted to load location data from a " +
                    "photo with invalid coordinates.");

            return;
        }

        Log.i("AddressLoader", "Attempting to hit Geocoder for Address data.");

        List<Address> addresses;
        try {
            addresses = coder.getFromLocation(photo.getLatitude(), photo.getLongitude(), 1);

            Address chosenLocation;
            if (!addresses.isEmpty()) {

                Log.i("AddressLoader", "Geocoder returned at least one result.");

                chosenLocation = addresses.get(0);
            } else {
                Log.i("AddressLoader", "Geocoder returned no results, setting default location.");

                chosenLocation = new Address(Locale.getDefault());
                chosenLocation.setLatitude(photo.getLatitude());
                chosenLocation.setLongitude(photo.getLongitude());
            }

            photo.setAddress( chosenLocation );

        } catch (IOException e) {

            Log.i("AddressLoader", "Exception occurred during Geocoder hit.");
            e.printStackTrace();
        }
    }

}
