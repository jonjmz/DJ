package edu.ucsd.dj;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.location.Address;

/**
 * Responsible for providing labels for photos that have valid location data.
 *
 * Created by Jake Sutton on 5/9/17.
 */
public class PhotoLabeler {
    BitmapFactory.Options options;

    /**
     * Constructor, configures options so that bitmaps returned by this class'
     * BitmapFactory are mutable.
     */
    public PhotoLabeler(){
        options = new BitmapFactory.Options();
        options.inMutable = true;
        options.inScaled = true;
    }

    /**
     * Returns the Bitmap represenation of this image with a location
     * label drawn onto it.
     *
     * @return Bitmap representing the photo with a location label.
     */
    public Bitmap label(Bitmap bitmap, String text, Context context){

        if (text.equals("")) return bitmap;

        int width = context.getResources().getDisplayMetrics().widthPixels;
        int height = context.getResources().getDisplayMetrics().heightPixels;

        bitmap = bitmap.createScaledBitmap( bitmap, width, height, false );
        Canvas canvas = new Canvas(bitmap);

        Paint paint = new Paint();
        paint.setColor(Color.WHITE);
        paint.setTextSize(35);
        paint.setShadowLayer(6.0f, 0.0f, 0.0f, Color.BLACK);
        canvas.drawText(text, 40, (height/4) * 3, paint);

        return bitmap;
    }

    /**
     * Returns string corresponding to the geographic location described
     * by the data saved in the photo object 'photo'.
     *
     * @return Written location of this image.
     */
    public String generateLabel(Address address) {

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

}
