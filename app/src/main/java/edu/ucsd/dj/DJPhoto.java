package edu.ucsd.dj;

import android.app.Application;
import android.content.Context;

/**
 * Created by jakesutton on 5/14/17.
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
