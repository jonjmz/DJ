package edu.ucsd.dj;

import android.app.WallpaperManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.IOException;

/**
 * Created by Jake Sutton on 5/13/17.
 */
public class DJWallpaper {
    private static final DJWallpaper ourInstance = new DJWallpaper();
    private BitmapLabeler labeler;
    private  AddressLabelStrategy labelStrategy;
    private Bitmap defaultPhoto;

    public static DJWallpaper getInstance() {
        return ourInstance;
    }

    private DJWallpaper() {
        labeler = new BitmapLabeler();
    }

    public void set(Photo photo, Context context) {

        if (photo == null) setDefault(context);

        try {
            labelStrategy = new AddressLabelStrategy(context);

            String label = "";
            if (photo.getInfo().hasValidCoordinates()) {
                label = labelStrategy.getLabel(photo.getInfo());
            }

            Bitmap bitmap = BitmapFactory.decodeFile(photo.getPathname());
            Bitmap newBackground = labeler.label( bitmap, label, context );

            WallpaperManager.getInstance(context).setBitmap( newBackground );

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setDefault(Context context) {
        try {
            defaultPhoto = BitmapFactory.decodeResource(context.getResources(),
                    R.drawable.dejaphotodefault);
            WallpaperManager.getInstance(context).setBitmap( defaultPhoto );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
