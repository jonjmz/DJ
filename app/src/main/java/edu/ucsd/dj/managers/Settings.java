package edu.ucsd.dj.managers;

import android.util.Log;

import java.util.LinkedList;
import java.util.List;
import java.util.Timer;

import edu.ucsd.dj.interfaces.ISettingsObserver;
import edu.ucsd.dj.interfaces.ISettingsSubject;

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

    private static final Settings ourInstance = new Settings();

    public static Settings getInstance() {
        return ourInstance;
    }

    private Settings() {
        observers = new LinkedList<>();
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
    }

    public void setViewingFriendsAlbum(boolean viewingFriendsAlbum) {
        this.viewingFriendsAlbum = viewingFriendsAlbum;
    }

    public long getRefreshRateMillis() {
        return refreshRate * MILLIS_PER_MINUTE;
    }

    public void setRefreshRateMinutes(int refreshRate) {
        Settings.refreshRate = refreshRate;
    }

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
