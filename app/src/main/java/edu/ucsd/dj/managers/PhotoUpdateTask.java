package edu.ucsd.dj.managers;

import android.os.Handler;
import android.util.Log;

import java.util.TimerTask;

import edu.ucsd.dj.others.PhotoCollection;

/**
 * Timer based that to updateLocation the wallpaper
 * Created by Jake Sutton on 5/14/17.
 */

public class PhotoUpdateTask implements Runnable {
    Handler handler;

    public PhotoUpdateTask() {
        handler = new Handler();
        handler.postDelayed(this, 0);
    }

    public void reset() {
        handler.removeCallbacks(this);
        handler.postDelayed(this, 0);
    }

    /**
     * Run the procedure that updates all the photo and
     * set a new wallpaper for the phone. This is ran on a timer
     */
    @Override
    public void run() {
        PhotoCollection collection = PhotoCollection.getInstance();
        if (collection.isEmpty())
            DJWallpaper.getInstance().setDefault();
        else
            collection.next();

        Log.i(this.getClass().toString(), "Periodic updateLocation task completed successfully.");

        handler.postDelayed(this, Settings.getInstance().getRefreshRateMillis());
    }
}
