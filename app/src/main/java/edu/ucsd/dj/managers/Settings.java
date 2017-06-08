package edu.ucsd.dj.managers;

import android.os.Environment;
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
    private final int MILLIS_PER_MINUTE = 60000;
    private boolean considerProximity = true;
    private boolean considerTOD = true;
    private boolean considerRecency = true;

    private boolean viewingMyAlbum  = true;
    private boolean viewingFriendsAlbum = true;

    private List<ISettingsObserver> observers;

    private static Timer timer;

    private static int refreshRate = 60;

    public String DCIM_LOCATION;
    public String MAIN_LOCATION;
    public String FRIENDS_LOCATION;
    public String COPIED_LOCATION;

    private static final Settings ourInstance = new Settings();


    public static Settings getInstance() {
        return ourInstance;
    }

    private Settings() {
        observers = new LinkedList<>();

        DCIM_LOCATION = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM)+File.separator;
        String base = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)+ File.separator;
        MAIN_LOCATION = base + "DejaPhotoMain" + File.separator;
        FRIENDS_LOCATION = base + "DejaPhotoFriends" + File.separator;
        COPIED_LOCATION = base + "DejaPhotoCopied" + File.separator;

        new File(MAIN_LOCATION).mkdir();
        new File(FRIENDS_LOCATION).mkdir();
        new File(COPIED_LOCATION).mkdir();
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
        return refreshRate * MILLIS_PER_MINUTE;
    }

    public void setRefreshRateMinutes(int refreshRate) { Settings.refreshRate = refreshRate; }

    /**
     * Initialize a timer to run the updateLocation procedure task
     */
    public void initTimer(){
        Log.i("Running timer: ", "timer is being initialized");
        timer = new Timer();
        timer.schedule(new PhotoUpdateTask(), 0, getRefreshRateMillis());
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
