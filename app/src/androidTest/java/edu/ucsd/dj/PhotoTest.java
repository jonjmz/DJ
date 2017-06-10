package edu.ucsd.dj;

import android.content.Context;
import android.support.test.InstrumentationRegistry;

import org.junit.Test;

import edu.ucsd.dj.interfaces.models.IUser;
import edu.ucsd.dj.models.mocks.MockUser;
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
    public void testKarma() throws Exception {

        Photo p = new Photo("", new MockUser());

        assertEquals(p.getKarma(), 0);
        p.karma();
        assertEquals(p.getKarma(), 1);
        p.karma();
        assertEquals(p.getKarma(), 0);
    }

    @Test
    public void testUID() {

        IUser u = new MockUser();
        Photo p = new Photo("", u);

        assertEquals(p.getUid(), u.getUserId() + "-" + p.getName());
    }

    @Test
    public void useAppContext() throws Exception {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();
        assertEquals("edu.ucsd.dj", appContext.getPackageName());
    }

    @Test
    public void testConstructor() throws Exception {
        Photo a = new Photo("reference", new MockUser());
        assertEquals(a.getPathname(), "reference");
        assertEquals(a.getHasValidCoordinates(), false);
        assertEquals(a.getKarma(), 0);
    }

    @Test
    public void testCalculateScore() throws Exception {
        //TODO need help pls
    }

    @Test
    public void testEquals() throws Exception {
        Photo b = new Photo("reference", new MockUser());
        Photo c = new Photo("", new MockUser());
        Photo a = new Photo("reference", new MockUser());
        assertTrue(a.equals(b));
        assertFalse(c.equals(a) || c.equals(b));
    }

    @Test
    public void testHashCode() throws Exception {
        Photo a = new Photo("reference", new MockUser());
        Photo b = new Photo("reference", new MockUser());
        Photo c = new Photo("", new MockUser());
        Photo d = new Photo("", new MockUser());

        assertEquals(a.hashCode(), b.hashCode());
        assertEquals(c.hashCode(), d.hashCode());
        assertNotEquals(a.hashCode(), c.hashCode());
        assertNotEquals(b.hashCode(), d.hashCode());

    }

}
