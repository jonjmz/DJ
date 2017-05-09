package edu.ucsd.dj;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 * Created by jakesutton on 5/9/17.
 */

/**
 *
 */
public class AddressLoader {

    Geocoder coder;

    /**
     *
     */
    public AddressLoader(Context context) {
        coder = new Geocoder( context, Locale.getDefault() );
    }

    /**
     * TODO - Something needs to be added here.
     *
     * @param photo
     */
    public void loadAddressFor( Photo photo ) {

        // do nothing
        if (!photo.hasValidCoordinates()) {
            return;
        }

        List<Address> addresses;

        // Here 1 represent max location result to returned, by documents it
        // recommended 1 to 5
        try {
            addresses = coder.getFromLocation(photo.getLatitude(), photo.getLongitude(), 1);

            Address chosenLocation;
            if (!addresses.isEmpty()) chosenLocation = addresses.get(0);
            else chosenLocation = new Address(Locale.getDefault());

            photo.setAddress( chosenLocation );

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
