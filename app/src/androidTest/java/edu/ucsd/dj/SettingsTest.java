package edu.ucsd.dj;

import org.junit.Test;

import edu.ucsd.dj.managers.Settings;
import edu.ucsd.dj.models.mocks.MockSettingsObserver;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by jakesutton on 6/9/17.
 */

public class SettingsTest {

    @Test
    public void testObserver() {
        Settings s = Settings.getInstance();
        MockSettingsObserver o = new MockSettingsObserver();

        s.addObserver(o);

        o.setUpdated(false);
        s.notifyObservers();
        assertTrue(o.wasUpdated());
    }

    @Test
    public void testRemoveObserver() {
        Settings s = Settings.getInstance();
        MockSettingsObserver o = new MockSettingsObserver();

        s.addObserver(o);

        o.reset();
        s.notifyObservers();
        assertTrue(o.wasUpdated());

        s.removeObserver(o);

        o.reset();
        s.notifyObservers();
        assertFalse(o.wasUpdated());
    }


    @Test
    public void testPropagateSettings() {

        Settings s = Settings.getInstance();
        MockSettingsObserver o = new MockSettingsObserver();

        o.reset();
        s.addObserver(o);

        s.setConsiderProximity(true);
        assertEquals(o.prox(), true);

        o.reset();

        s.setConsiderRecency(true);
        assertEquals(o.recent(), true);

        o.reset();

        s.setConsiderTOD(true);
        assertEquals(o.tod(), true);


    }
}
