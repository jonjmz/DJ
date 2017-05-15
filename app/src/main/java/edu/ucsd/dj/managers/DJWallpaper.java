package edu.ucsd.dj.managers;

import android.app.WallpaperManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.IOException;

import edu.ucsd.dj.strategies.AddressLabelStrategy;
import edu.ucsd.dj.BitmapLabeler;
import edu.ucsd.dj.R;
import edu.ucsd.dj.models.Photo;

/**
 * Created by Jake Sutton on 5/13/17.
 */
public class DJWallpaper {
    private static final DJWallpaper ourInstance = new DJWallpaper();
    private BitmapLabeler labeler;
    private AddressLabelStrategy labelStrategy;
    private Bitmap defaultPhoto;

    public static DJWallpaper getInstance() {
        return ourInstance;
    }

    private DJWallpaper() {
        labeler = new BitmapLabeler();
    }

    public void set(Photo photo) {

        Context context = DJPhoto.getAppContext();

        if (photo == null) {
            Log.i(this.getClass().toString(), "photo passed to set(Photo photo) is null.");
            setDefault();
            return;
        }

        try {
            labelStrategy = new AddressLabelStrategy(context);

            String label = "";
            if (photo.getInfo().hasValidCoordinates()) {
                label = labelStrategy.getLabel(photo.getInfo());
            }

            Bitmap bitmap = BitmapFactory.decodeFile(photo.getPathname());
            Bitmap newBackground = labeler.label( bitmap, label, context );

            WallpaperManager.getInstance(context).setBitmap( newBackground );

            Log.i(this.getClass().toString(), "wallpaper set successfully.");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setDefault() {
        try {
            Context context = DJPhoto.getAppContext();

            defaultPhoto = BitmapFactory.decodeResource(context.getResources(),
                    R.drawable.dejaphotodefault);
            WallpaperManager.getInstance(context).setBitmap( defaultPhoto );

            Log.i(this.getClass().toString(), "Default wallpaper set.");
        } catch (Exception e) {

            Log.i(this.getClass().toString(), "Error setting default wallpaper.");
            e.printStackTrace();
        }
    }
}
