package edu.ucsd.dj.managers;

import android.app.Application;
import android.content.Context;

/**
 * Created by Jake Sutton on 5/14/17.
 */
public class DJPhoto extends Application {

    private static Context context;

    public void onCreate() {
        super.onCreate();
        DJPhoto.context = getApplicationContext();
    }

    public static Context getAppContext() {
        return DJPhoto.context;
    }
}
