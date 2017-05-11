package edu.ucsd.dj;

/**
 * Created by jonathanjimenez on 5/9/17.
 */

public final class Settings {
    private static boolean considerProximity;
    private static boolean considerTOD;
    private static boolean considerRecency;
    //private static boolean usingCustomAlbum;

    public Settings() {
        considerProximity = true;
        considerTOD = true;
        considerRecency = true;
        //usingCustomAlbum = false;
    }

    public static boolean isConsideringProximity() { return considerProximity; }

    public static boolean isConsideringTOD() {
        return considerTOD;
    }

    public static boolean isConsideringRecency() {
        return considerRecency;
    }

    //public static boolean isUsingCustomAlbum() { return considerProximity; }


    public static void setConsiderProximity(boolean considerProximity) {
        Settings.considerProximity = considerProximity;
    }

    public static void setConsiderTOD(boolean considerTOD) {
        Settings.considerTOD = considerTOD;
    }

    public static void setConsiderRecency(boolean considerRecency) {
        Settings.considerRecency = considerRecency;
    }

    /*
    public static void setUsingCustomAlbum(boolean considerProximity) {
        Settings.considerProximity = considerProximity;
    }
    */
}
