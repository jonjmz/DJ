package edu.ucsd.dj.managers;

import android.util.Log;

import java.util.Timer;

/**
 * Setting class that holds all configurations
 * Created by jonathanjimenez on 5/9/17.
 */
public final class Settings {
    private static final int MILLIS_PER_MINUTE = 60000;
    private static boolean considerProximity = true;
    private static boolean considerTOD = true;
    private static boolean considerRecency = true;
    private static boolean viewingMyAlbum  = true;
    private static boolean viewingFriendsAlbum = true;


    private static Timer timer;

    private static int refreshRate = 60;

    public static boolean isConsideringProximity() {
        return considerProximity;
    }

    public static boolean isConsideringTOD() {
        return considerTOD;
    }

    public static boolean isConsideringRecency() {
        return considerRecency;
    }

    public static boolean isViewingMyAlbum() {
        return viewingMyAlbum ;
    }

    public static boolean isViewingFriendsAlbum() {
        return viewingFriendsAlbum;
    }

    public static void setConsiderProximity(boolean considerProximity) {
        Settings.considerProximity = considerProximity;
    }

    public static void setConsiderTOD(boolean considerTOD) {
        Settings.considerTOD = considerTOD;
    }

    public static void setConsiderRecency(boolean considerRecency) {
        Settings.considerRecency = considerRecency;
    }

    public static void setViewingMyAlbum (boolean viewingMyAlbum ) {
        Settings.viewingMyAlbum = viewingMyAlbum ;
    }

    public static void setViewingFriendsAlbum(boolean viewingFriendsAlbum) {
        Settings.viewingFriendsAlbum = viewingFriendsAlbum;
    }

    public static long getRefreshRateMillis() {
        return refreshRate * MILLIS_PER_MINUTE;
    }

    public static void setRefreshRateMinutes(int refreshRate) {
        Settings.refreshRate = refreshRate;
    }

    /**
     * Initialize a timer to run the updateLocation procedure task
     */
    public static void initTimer(){
        Log.i("Running timer: ", "timer is being initialized");
        timer = new Timer();
        timer.schedule(new PhotoUpdateTask(), 0, getRefreshRateMillis());
    }
}
