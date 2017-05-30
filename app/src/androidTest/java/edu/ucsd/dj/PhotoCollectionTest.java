package edu.ucsd.dj;

import android.content.Context;
import android.support.test.InstrumentationRegistry;

import org.junit.Test;

import edu.ucsd.dj.models.Photo;
import edu.ucsd.dj.others.PhotoCollection;

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
        // This test requires manipulation of current time
    }

    @Test
    public void prioritizeTimeOfDay(){
        // This test requires manipulation of current time
    }

    @Test
    public void testNext() throws Exception {
        PhotoCollection collection = PhotoCollection.getInstance();
        collection.update();
        Photo last = collection.current();
        Photo current = collection.next();
        assert(!current.equals(last));
    }

    @Test
    public void testPrevious() throws Exception {
        PhotoCollection collection = PhotoCollection.getInstance();
        collection.update();
        Photo current = collection.current();
        collection.next();
        Photo previous = collection.previous();
        assert(current.equals(previous));
    }
}
