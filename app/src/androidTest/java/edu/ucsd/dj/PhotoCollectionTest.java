package edu.ucsd.dj;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.util.Log;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

/**
 * Created by Gus on 5/9/2017.
 */

public class PhotoCollectionTest {
    @Test
    public void useAppContext() throws Exception {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext().getApplicationContext();

        assertEquals("edu.ucsd.dj", appContext.getPackageName());
    }

    @Test
    public void testConstructor() throws Exception {
        PhotoCollection collection = new PhotoCollection();
        assertNotEquals(collection, collection.getInstance());
    }

    @Test
    public void prioritizeRecent(){
        PhotoCollection collection = PhotoCollection.getInstance();
        Settings.setConsiderProximity(false);
        Settings.setConsiderRecency(true);
        Settings.setConsiderTOD(false);
        collection.sort();
        // Most recent photo should be from 2017
        assertEquals(true, collection.current().getPathname().contains("2017"));
    }

    @Test
    public void prioritizeTimeOfDay(){
        PhotoCollection collection = PhotoCollection.getInstance();
        Settings.setConsiderProximity(false);
        Settings.setConsiderRecency(false);
        Settings.setConsiderTOD(true);
        collection.sort();
        Event.TimeOfDay tod = Event.currentTimeOfDay();
        Log.v("What?", "" + tod);
        Log.v("What?", collection.current().getPathname());

        // First photo should be the same time of day
        assertEquals(true, collection.current().getPathname().contains(tod.toString().toLowerCase()));
    }

    @Test
    public void testUpdate() throws Exception {

    }

    @Test
    public void testRelease() throws Exception {

    }

    @Test
    public void testNext() throws Exception {
        PhotoCollection collection = PhotoCollection.getInstance();
        Context appContext = InstrumentationRegistry.getTargetContext().getApplicationContext();
        collection.update(appContext);
        Photo last = collection.current();
        Photo current = collection.next();
        assert(!current.equals(last));
    }

    @Test
    public void testPrevious() throws Exception {
        PhotoCollection collection = PhotoCollection.getInstance();
        Context appContext = InstrumentationRegistry.getTargetContext().getApplicationContext();
        collection.update(appContext);
        Photo current = collection.current();
        collection.next();
        Photo previous = collection.previous();
        assert(current.equals(previous));
    }

    @Test
    public void testCurrent() throws Exception {

    }

    @Test
    public void testHasPrevious() throws Exception {

    }
    
    @Test
    public void testSaveToFile() throws Exception {

    }
}
