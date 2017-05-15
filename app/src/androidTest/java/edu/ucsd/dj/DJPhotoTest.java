package edu.ucsd.dj;

import android.content.Context;
import android.support.test.InstrumentationRegistry;

import org.junit.Test;

import edu.ucsd.dj.managers.DJPhoto;

import static org.junit.Assert.assertEquals;

/**
 * Created by nguyen on 5/14/2017.
 */

public class DJPhotoTest {

    @Test
    public void useAppContext() throws Exception {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        assertEquals("edu.ucsd.dj", appContext.getPackageName());
    }
    @Test
    public void testGetAppContext(){
        Context appContext = InstrumentationRegistry.getTargetContext();
        assertEquals(DJPhoto.getAppContext(), appContext);

    }


}
