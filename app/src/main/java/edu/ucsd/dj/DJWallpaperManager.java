package edu.ucsd.dj;

import android.app.WallpaperManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.IOException;

/**
 * Created by Jake Sutton on 5/13/17.
 */
public class DJWallpaperManager {
    private static final DJWallpaperManager ourInstance = new DJWallpaperManager();

    public static DJWallpaperManager getInstance() {
        return ourInstance;
    }

    private DJWallpaperManager() {}

    public void set(Photo photo, Context context) {
        try {
            BitmapLabeler labeler = new BitmapLabeler();
            AddressLabelStrategy addressLabelMaker = new AddressLabelStrategy(context);

            String label = "";
            if (photo.getInfo().hasValidCoordinates()) {
                label = addressLabelMaker.getLabel(photo.getInfo());
            }

            Bitmap bitmap = BitmapFactory.decodeFile(photo.getPathname());
            Bitmap newBackground = labeler.label( bitmap, label, context );

            WallpaperManager.getInstance(context).setBitmap( newBackground );

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
