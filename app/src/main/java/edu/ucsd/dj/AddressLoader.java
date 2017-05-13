package edu.ucsd.dj;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.util.Log;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import edu.ucsd.dj.interfaces.Addressable;

/**
 * Responsible for loading Human readable location information from the
 * latitude and longitude coordinates saved in a photo object.
 *
 * Created by Jake Sutton on 5/9/17.
 */
public class AddressLoader {

    private Geocoder coder;

    /**
     * Creates an AddressLoader object, taking the current application context, and setting the
     * locale to the phone's default locale.
     */
    public AddressLoader(Context context) {
        coder = new Geocoder( context, Locale.getDefault() );
    }

    /**
     * Uses builtin Geocoder to get an generateAddress object corresponding to the location this
     * photo's latitude and longitude.
     *
     * @param info
     */
    public Address generateAddress(Addressable info ) {

        Log.i("AddressLoader", "Attempting to hit Geocoder for Address data.");

        List<Address> addresses;
        Address chosenLocation;

        try {
            addresses = coder.getFromLocation(info.getLatitude(), info.getLongitude(), 1);

            if (!addresses.isEmpty()) {

                Log.i("AddressLoader", "Geocoder returned at least one result.");

                chosenLocation = addresses.get(0);
            } else {
                Log.i("AddressLoader", "Geocoder returned no results, setting default location.");

                chosenLocation = new Address(Locale.getDefault());
                chosenLocation.setLatitude(info.getLatitude());
                chosenLocation.setLongitude(info.getLongitude());
            }



        } catch (IOException e) {

            Log.i("AddressLoader", "Exception occurred during Geocoder hit.");
            e.printStackTrace();

            // TODO not good to do this here
            chosenLocation = new Address(Locale.getDefault());
        }

        return chosenLocation;
    }
}
