package edu.ucsd.dj.managers;

import java.util.Timer;

/**
 * Created by jonathanjimenez on 5/9/17.
 */
public final class Settings {
    private static final int MILLIS_PER_MINUTE = 60000;
    private static boolean considerProximity = true;
    private static boolean considerTOD = true;
    private static boolean considerRecency = true;

    private static Timer timer;

    private static int refreshRate = 60;

    public static boolean isConsideringProximity() { return considerProximity; }

    public static boolean isConsideringTOD() {
        return considerTOD;
    }

    public static boolean isConsideringRecency() {
        return considerRecency;
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

    public static long getRefreshRateMillis() {
        return refreshRate * MILLIS_PER_MINUTE;
    }

    public static void setRefreshRateMinutes(int refreshRate) {
        Settings.refreshRate = refreshRate;
    }

    public static Timer getTimer() {
        return timer;
    }

    public static void initTimer(){
        timer = new Timer();
        timer.schedule(new PhotoUpdateTask(), 0, getRefreshRateMillis());
    }
}
