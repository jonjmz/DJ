package edu.ucsd.dj;

/**
 * Created by jonathanjimenez on 5/9/17.
 */

public final class Settings {
    private static boolean considerRecency;
    private static boolean considerTOD;
    private static boolean considerProximity;

    public Settings() {
        considerRecency = true;
        considerTOD = true;
        considerProximity = true;
    }

    public static boolean isConsideringRecency() {
        return considerRecency;
    }

    public static boolean isConsideringTOD() {
        return considerTOD;
    }

    public static boolean isConsideringProximity() {
        return considerProximity;
    }

    public static void setConsiderRecency(boolean considerRecency) {
        Settings.considerRecency = considerRecency;
    }

    public static void setConsiderTOD(boolean considerTOD) {
        Settings.considerTOD = considerTOD;
    }

    public static void setConsiderProximity(boolean considerProximity) {
        Settings.considerProximity = considerProximity;
    }
}
