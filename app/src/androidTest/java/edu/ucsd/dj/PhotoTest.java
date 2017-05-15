package edu.ucsd.dj;

import android.content.Context;
import android.support.test.InstrumentationRegistry;

import org.junit.Test;

import edu.ucsd.dj.models.Event;
import edu.ucsd.dj.models.Photo;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by Gus on 5/9/2017.
 */

public class PhotoTest {
    @Test
    public void testConstructor() throws Exception {
        Photo a = new Photo("reference");
        assertEquals(a.getPathname(), "reference");
    }

    @Test
    public void testEquals() throws Exception {
        Photo a = new Photo("reference");
        Photo b = new Photo("reference");
        Photo c = new Photo("");
        Photo d = new Photo("");
        assertTrue(a.equals(b));
        assertTrue(c.equals(d));
        assertFalse(a.equals(c));
        assertFalse(b.equals(d));

    }

    @Test
    public void testHashCode() throws Exception {
        Photo a = new Photo("reference");
        Photo b = new Photo("reference");
        Photo c = new Photo("");
        Photo d = new Photo("");
        assertEquals(a.hashCode(), b.hashCode());
        assertEquals(c.hashCode(), d.hashCode());
        assertNotEquals(a.hashCode(), c.hashCode());
        assertNotEquals(b.hashCode(), d.hashCode());

    }

}
