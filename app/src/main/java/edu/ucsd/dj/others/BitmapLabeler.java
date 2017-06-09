package edu.ucsd.dj.others;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;

import edu.ucsd.dj.interfaces.ILabelKarma;
import edu.ucsd.dj.interfaces.models.IPhoto;
import edu.ucsd.dj.managers.DJPhoto;

/**
 * Responsible for providing labels for photos that have valid location data.
 *
 * Labels photo
 * Created by Jake Sutton on 5/9/17.
 */

public class BitmapLabeler implements ILabelKarma {
    BitmapFactory.Options options;

    /**
     * Constructor, configures options so that bitmaps returned by this class'
     * BitmapFactory are mutable.
     */
    public BitmapLabeler(){
        options = new BitmapFactory.Options();
        options.inMutable = true;
        options.inScaled = true;
    }

    /**
     * Returns the Bitmap represenation of this image with a location
     * getLabel drawn onto it.
     *
     * @return Bitmap representing the photo with a location getLabel.
     */
    public Bitmap label(Bitmap bitmap, String text, Context context){

        Log.i("Label: ", text);
        if (text.equals("")) return bitmap;

        int height = context.getResources().getDisplayMetrics().heightPixels;

        bitmap = resize(bitmap);

        Canvas canvas = new Canvas(bitmap);

        Paint paint = new Paint();
        paint.setColor(Color.WHITE);
        paint.setTextSize(35);
        paint.setShadowLayer(6.0f, 0.0f, 0.0f, Color.BLACK);
        canvas.drawText(text, 40, (height/4) * 3, paint);

        return bitmap;
    }

    public static Bitmap resize(Bitmap bitmap){
        Context context = DJPhoto.getAppContext();
        int width = context.getResources().getDisplayMetrics().widthPixels;
        int height = context.getResources().getDisplayMetrics().heightPixels;
        bitmap = bitmap.createScaledBitmap( bitmap, width, height, false );
        return bitmap;
    }

    @Override
    public Bitmap createBitmapWithKarmaLabel(Bitmap bitmap, IPhoto photo) {

        Log.i("Label: ", "Karma: " + photo.getKarma());

        int width = DJPhoto.getAppContext().getResources().getDisplayMetrics().widthPixels;
        int height = DJPhoto.getAppContext().getResources().getDisplayMetrics().heightPixels;

        bitmap = bitmap.createScaledBitmap( bitmap, width, height, false );
        Canvas canvas = new Canvas(bitmap);

        Paint paint = new Paint();
        paint.setColor(Color.WHITE);
        paint.setTextSize(35);
        paint.setShadowLayer(6.0f, 0.0f, 0.0f, Color.BLACK);
        String label = "Karma: " + photo.getKarma();
        canvas.drawText(label, 500, (height/3) * 3 - 10, paint);

        return bitmap;
    }
}
