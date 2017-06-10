package edu.ucsd.dj.managers;

import android.os.Environment;
import android.os.Handler;
import android.util.Log;

import java.io.File;
import java.util.LinkedList;
import java.util.List;
import java.util.Timer;

import edu.ucsd.dj.interfaces.observers.ISettingsObserver;
import edu.ucsd.dj.interfaces.observers.ISettingsSubject;

/**
 * Setting class that holds all configurations
 * Created by jonathanjimenez on 5/9/17.
 */
public final class Settings implements ISettingsSubject {
    private final int MILLIS_PER_SECONDS = 1000;
    private boolean considerProximity = true;
    private boolean considerTOD = true;
    private boolean considerRecency = true;

    private boolean viewingMyAlbum  = true;
    private boolean viewingFriendsAlbum = true;

    private List<ISettingsObserver> observers;

    private static PhotoUpdateTask timer;

    private static int refreshRate = 60*60; // In seconds

    public String DCIM_LOCATION;
    public String MAIN_LOCATION;
    public String FRIENDS_LOCATION;
    public String CAMERA_LOCATION;

    private static final Settings ourInstance = new Settings();


    public static Settings getInstance() {
        return ourInstance;
    }

    private Settings() {
        observers = new LinkedList<>();

        DCIM_LOCATION = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM)+File.separator;
        String base = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)+ File.separator;
        MAIN_LOCATION = base + "DejaPhotoCopied" + File.separator;
        FRIENDS_LOCATION = base + "DejaPhotoFriends" + File.separator;
        CAMERA_LOCATION = base + "DejaPhoto" + File.separator;

        new File(MAIN_LOCATION).mkdir();
        new File(FRIENDS_LOCATION).mkdir();
        new File(CAMERA_LOCATION).mkdir();
    }

    public boolean isConsideringProximity() {
        return considerProximity;
    }

    public boolean isConsideringTOD() {
        return considerTOD;
    }

    public boolean isConsideringRecency() {
        return considerRecency;
    }

    public boolean isViewingMyAlbum() {
        return viewingMyAlbum ;
    }

    public boolean isViewingFriendsAlbum() {
        return viewingFriendsAlbum;
    }

    public void setConsiderProximity(boolean considerProximity) {
        this.considerProximity = considerProximity;

        notifyObservers();
    }

    public void setConsiderTOD(boolean considerTOD) {
        this.considerTOD = considerTOD;

        notifyObservers();
    }

    public void setConsiderRecency(boolean considerRecency) {
        this.considerRecency = considerRecency;

        notifyObservers();
    }

    public void setViewingMyAlbum (boolean viewingMyAlbum ) {
        this.viewingMyAlbum = viewingMyAlbum ;
        notifyObservers();

    }

    public void setViewingFriendsAlbum(boolean viewingFriendsAlbum) {
        this.viewingFriendsAlbum = viewingFriendsAlbum;
        notifyObservers();

    }

    public long getRefreshRateMillis() {
        return refreshRate * MILLIS_PER_SECONDS;
    }

    public void setRefreshRatePercent(int refreshPercent) {
        int max_value = 60 * 60; // At most on one hour
        int min_value = 10; // at least 10 seconds
        // Get percent of max value that we are at
        int refresh = (int)(refreshPercent * (max_value / 100.0));
        if (refresh < min_value)
            refresh = min_value;
        Settings.refreshRate = refresh;
        timer.reset();
    }

    /**
     * Initialize a timer to run the updateLocation procedure task
     */
    public void initTimer(){
        Log.i("Running timer: ", "timer is being initialized");
        timer = new PhotoUpdateTask();
    }

    @Override
    public void notifyObservers() {
        for (ISettingsObserver o: observers) {
            o.update();
        }
    }

    @Override
    public void addObserver(ISettingsObserver o) {
        observers.add(o);
    }

    @Override
    public void removeObserver(ISettingsObserver o) {
        observers.remove(o);
    }
}
