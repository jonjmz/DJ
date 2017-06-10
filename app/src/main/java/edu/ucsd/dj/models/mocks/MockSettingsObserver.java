package edu.ucsd.dj.models.mocks;

import edu.ucsd.dj.interfaces.observers.ISettingsObserver;
import edu.ucsd.dj.managers.Settings;

/**
 * Created by jakesutton on 6/9/17.
 */

public class MockSettingsObserver implements ISettingsObserver {

    boolean updated;
    boolean recentSetting;
    boolean todSetting;
    boolean proxSetting;

    public void reset() {
        updated = recentSetting = todSetting = proxSetting = false;
    }

    public boolean recent() {
        return recentSetting;
    }

    public void setRecentSetting(boolean recentSetting) {
        this.recentSetting = recentSetting;
    }

    public boolean tod() {
        return todSetting;
    }

    public void setTodSetting(boolean setting) {
        this.todSetting = setting;
    }

    public boolean prox() {
        return proxSetting;
    }

    public void setProxSetting(boolean proxSetting) {
        this.proxSetting = proxSetting;
    }

    public boolean wasUpdated() {
        return updated;
    }

    public void setUpdated(boolean wasUpdated) {
        this.updated = wasUpdated;
    }

    @Override
    public void update() {
        updated = true;

        setProxSetting(Settings.getInstance().isConsideringProximity());
        setRecentSetting(Settings.getInstance().isConsideringRecency());
        setTodSetting(Settings.getInstance().isConsideringTOD());
    }
}
