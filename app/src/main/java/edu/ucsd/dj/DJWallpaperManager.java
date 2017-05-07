package edu.ucsd.dj;

import android.app.WallpaperManager;
import android.content.Context;
import android.graphics.Bitmap;

import java.io.IOException;

/**
 * Created by jakesutton on 5/2/17.
 */

public class DJWallpaperManager {

    WallpaperManager manager;

    /**
     * TODO
     *
     * @param context
     */
    public DJWallpaperManager(Context context) {
        manager = WallpaperManager.getInstance(context);
    }

    /**
     * TODO
     *
     * @param photo
     */
    public void setWallpaper(Photo photo) {
        // get bitmap
        Bitmap bm = photo.getBitmap();

        // adjust bounds
        bm = Bitmap.createScaledBitmap(bm, 300, 300, false);

        // set wallpaper
        try {
            manager.setBitmap(bm);
        } catch (IOException e) {
            // TODO log some kind of exception
            e.printStackTrace();
        }
    }


}
