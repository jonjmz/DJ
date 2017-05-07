package edu.ucsd.dj;

import android.app.WallpaperManager;
import android.content.Context;
import android.graphics.Bitmap;

import java.io.IOException;

/**
 * @author Jake Sutton
 * @since 5/2/17
 *
 * The class handles settings wallpaper functionality of the application
 */

public class DJWallpaperManager {

    // Built in wallpaper manager
    WallpaperManager manager;

    /**
     * Constructor
     *
     * @param context takes in the current context of the application so
     *                to set the wallpaper
     */
    public DJWallpaperManager(Context context) {
        manager = WallpaperManager.getInstance(context);
    }

    /**
     * Set the wallpaper of the phone to a specified photo
     * Adjusts the wallpaper accordingly to fit on the screen
     *
     * @param photo Photo to be set as the wallpaper
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
