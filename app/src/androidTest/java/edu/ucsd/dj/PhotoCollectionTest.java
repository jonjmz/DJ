package edu.ucsd.dj;

import android.content.Context;
import android.support.test.InstrumentationRegistry;

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
        Context appContext = InstrumentationRegistry.getTargetContext();

        assertEquals("edu.ucsd.dj", appContext.getPackageName());
    }

    @Test
    public void testConstructor() throws Exception {
        PhotoCollection collection = new PhotoCollection();
        assertNotEquals(collection, collection.getInstance());
    }

    @Test
    public void testUpdate() throws Exception {

    }

    @Test
    public void testRelease() throws Exception {

    }

    @Test
    public void testNext() throws Exception {

    }

    @Test
    public void testPrevious() throws Exception {

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
