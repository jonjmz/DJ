package edu.ucsd.dj.managers;

import android.app.Application;
import android.content.Context;
import android.util.Log;

/**
 * Photo class that holds the current context
 * Created by Jake Sutton on 5/14/17.
 */
public class DJPhoto extends Application {

    private static Context context;

    /**
     * When the app runs, this is called
     */
    public void onCreate() {
        super.onCreate();
        DJPhoto.context = getApplicationContext();
    }

    /**
     * Get current context for the app for future uses
     * @return current context
     */
    public static Context getAppContext() {
        Log.i("Current context: ", DJPhoto.context.toString());
        return DJPhoto.context;
    }
}
