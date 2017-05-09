package edu.ucsd.dj;

import android.content.Context;
import android.support.test.InstrumentationRegistry;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by Gus on 5/9/2017.
 */

public class PhotoTest {
    @Test
    public void useAppContext() throws Exception {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        assertEquals("edu.ucsd.dj", appContext.getPackageName());
    }

    @Test
    public void testDefaultConstructor() throws Exception {
        Photo a = new Photo();
        assertEquals(a.isReleasable(), false);
        assertEquals(a.getPathname(), "Default Location");
        assertEquals(a.isKarmable(), false);
    }

    @Test
    public void testConstructor() throws Exception {
        Photo a = new Photo("reference", 420);
        assertEquals(a.getPathname(), "reference");
        assertEquals(a.getDateTaken(), 420);
        assertEquals(a.isReleasable(), true);
        assertEquals(a.isKarmable(), true);
    }

    @Test
    public void testCalculateScore() throws Exception {
        //TODO need help pls
    }

    @Test
    public void testEquals() throws Exception {
        Photo a = new Photo("reference", 420);
        Photo b = new Photo("reference", 69);
        Photo c = new Photo("", 69);
        Photo d = new Photo("", 322);
        assertTrue(a.equals(b));
        assertTrue(c.equals(d));
        assertFalse(a.equals(c));
        assertFalse(b.equals(d));

    }

    @Test
    public void testHashCode() throws Exception {
        Photo a = new Photo("reference", 420);
        Photo b = new Photo("reference", 69);
        Photo c = new Photo("", 69);
        Photo d = new Photo("", 322);
        assertEquals(a.hashCode(), b.hashCode());
        assertEquals(c.hashCode(), d.hashCode());
        assertNotEquals(a.hashCode(), c.hashCode());
        assertNotEquals(b.hashCode(), d.hashCode());

    }

    @Test
    public void testCompareTo() throws Exception {
        Photo a = new Photo("reference", 10000);
        Photo b = new Photo("reference", 209000);
        Photo c = new Photo("", 4000000);

        a.calculateScore();
        b.calculateScore();
        c.calculateScore();

        assertEquals(a.compareTo(b), 1);
        assertEquals(b.compareTo(a), -1);
        assertEquals(a.compareTo(c), 1);
        assertEquals(c.compareTo(b), -1);
        assertEquals(c.compareTo(c), 0);


    }


}
