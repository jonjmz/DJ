package edu.ucsd.dj;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.util.Log;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import edu.ucsd.dj.interfaces.IAddressable;
import edu.ucsd.dj.interfaces.ILabelAddressable;

/**
 * Responsible for loading Human readable location information from the
 * latitude and longitude coordinates saved in a photo object.
 *
 * Created by Jake Sutton on 5/9/17.
 */
public class AddressLabelStrategy implements ILabelAddressable {

    private Geocoder coder;

    /**
     * Creates an AddressLabelStrategy object, taking the current application context, and setting the
     * locale to the phone's default locale.
     */
    public AddressLabelStrategy(Context context) {
        coder = new Geocoder( context, Locale.getDefault() );
    }

    /**
     * Returns string corresponding to the geographic location described
     * by the data saved in the photo object 'photo'.
     *
     * @return Written location of this image.
     */
    public String getLabel(IAddressable info) {

        Address address = generateAddress(info);

        // Default to 'Location Unknown'
        String result = "Location Unknown";

        // Save relevant pieces of information.
        String place = address.getAddressLine(0);
        String city = address.getLocality();
        String state = address.getAdminArea();
        String country = address.getCountryName();

        // If the photo has valid location data...

        // Order and concatenate information in order of specificity.
        if (country != null && !country.isEmpty()) result = country;
        if (state != null && !state.isEmpty()) result = state + ", " + result;
        if (city != null && !city.isEmpty()) result = city + ", " + result;
        if (place != null && !place.isEmpty()) result = place + ", " + result;

        return result;
    }

    /**
     * Uses builtin Geocoder to get an generateAddress object corresponding to the location this
     * photo's latitude and longitude.
     *
     * @param info
     */
    public Address generateAddress(IAddressable info) {

        Log.i("AddressLabelStrategy", "Attempting to hit Geocoder for Address data.");

        List<Address> addresses;
        Address chosenLocation;

        try {
            addresses = coder.getFromLocation(info.getLatitude(), info.getLongitude(), 1);

            if (!addresses.isEmpty()) {

                Log.i("AddressLabelStrategy", "Geocoder returned at least one result.");

                chosenLocation = addresses.get(0);
            } else {
                Log.i("AddressLabelStrategy", "Geocoder returned no results, setting default location.");

                chosenLocation = new Address(Locale.getDefault());
                chosenLocation.setLatitude(info.getLatitude());
                chosenLocation.setLongitude(info.getLongitude());
            }

        } catch (IOException e) {

            Log.i("AddressLabelStrategy", "Exception occurred during Geocoder hit.");
            e.printStackTrace();

            // TODO not good to do this here
            chosenLocation = new Address(Locale.getDefault());
        }

        return chosenLocation;
    }
}
