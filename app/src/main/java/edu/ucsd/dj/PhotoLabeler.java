package edu.ucsd.dj;

import android.app.WallpaperManager;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.location.Address;
import android.util.DisplayMetrics;
import android.util.Log;

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
     * @param photo
     * @return Bitmap representing the photo with a location label.
     */
    public Bitmap labeledBitmapFor(Photo photo, Context context){

        Bitmap bitmap = BitmapFactory.decodeFile(photo.getPathname(), options);
        String text = generateLabel(photo);
        int width = context.getResources().getDisplayMetrics().widthPixels;
        int height = context.getResources().getDisplayMetrics().heightPixels;

        bitmap = bitmap.createScaledBitmap( bitmap, width, height, false );
        Canvas canvas = new Canvas(bitmap);

        //TODO refactor paint class
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
     * @param photo
     * @return Written location of this image.
     */
    private String generateLabel(Photo photo) {

        Log.i("PhotoLabeler", "Attempting to get labeledBitmapFor components for " + photo.getPathname());

        // Default to 'Location Unknown'
        String result = "Location Unknown";

        if (photo.getInfo().hasValidCoordinates()) {

            Address address = photo.getInfo().getAddress();

            // Save relevant pieces of information.
            String place = address.getAddressLine(0);
            String city = address.getLocality();
            String state = address.getAdminArea();
            String country = address.getCountryName();

            // If the photo has valid location data...

            Log.i("PhotoLabeler", "Success. Attempting to build labeledBitmapFor for " + photo.getPathname());

            // Order and concatenate information in order of specificity.
            if (country != null && !country.isEmpty()) result = country;
            if (state != null && !state.isEmpty()) result = state + ", " + result;
            if (city != null && !city.isEmpty()) result = city + ", " + result;
            if (place != null && !place.isEmpty()) result = place + ", " + result;
        }

        return result;
    }

}
