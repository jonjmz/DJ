package edu.ucsd.dj.managers;

import android.util.Log;

import java.util.TimerTask;

import edu.ucsd.dj.PhotoCollection;

/**
 * Timer based that to update the wallpaper
 * Created by Jake Sutton on 5/14/17.
 */

public class PhotoUpdateTask extends TimerTask {

    /**
     * Run the procedure that updates all the photo and
     * set a new wallpaper for the phone. This is ran on a timer
     */
    @Override
    public void run() {
        PhotoCollection collection = PhotoCollection.getInstance();
        collection.update();

        DJWallpaper.getInstance().set( collection.current() );

        Log.i(this.getClass().toString(), "Periodic update task completed successfully.");
    }
}
