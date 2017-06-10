package edu.ucsd.dj;

import android.content.Context;
import android.support.test.InstrumentationRegistry;

import org.junit.Test;

import java.util.Calendar;
import java.util.GregorianCalendar;

import edu.ucsd.dj.interfaces.IRating;
import edu.ucsd.dj.interfaces.models.IPhoto;
import edu.ucsd.dj.interfaces.observers.ICollectionObserver;
import edu.ucsd.dj.managers.DJPhoto;
import edu.ucsd.dj.managers.Settings;
import edu.ucsd.dj.models.Event;
import edu.ucsd.dj.models.Photo;
import edu.ucsd.dj.others.PhotoCollection;
import edu.ucsd.dj.strategies.RatingStrategy;

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
    public void prioritizeRecent() {
        Calendar calendar = new GregorianCalendar();
        calendar.setTimeInMillis(10);

        // get relevent objects
        PhotoCollection collection = PhotoCollection.getInstance();
        Settings settings = Settings.getInstance();
        IRating rating = collection.getRating();

        // remove old rating and replace with new rating
        settings.removeObserver(rating);
        rating = new RatingStrategy(true, false, false, calendar);
        collection.setRatingStrategy(rating);
        settings.addObserver(rating);

        // Let's add a fake photo to the collection
        Photo photo = new Photo();
        photo.setPathname("testPath.jpg");
        Event info = new Event();
        info.setDateTime(5); // Right before the supposed current time
        photo.setInfo(info);
        collection.addPhoto(photo);

        // Now the time is set to the earliest time
        collection.update();

        // get the current photo, it should be the earliest
        IPhoto bestPhoto = collection.current();
        assertEquals(bestPhoto, photo);
    }

    @Test
    public void prioritizeTimeOfDay() {
        Calendar calendar = new GregorianCalendar();
        calendar.setTimeInMillis(10);

        // get relevent objects
        PhotoCollection collection = PhotoCollection.getInstance();
        Settings settings = Settings.getInstance();
        IRating rating = collection.getRating();

        // remove old rating and replace with new rating
        settings.removeObserver(rating);
        rating = new RatingStrategy(false, true, false, calendar);
        collection.setRatingStrategy(rating);
        settings.addObserver(rating);

        // Let's add a fake photo to the collection
        Photo photo = new Photo();
        photo.setPathname("testPath.jpg");
        Event info = new Event();
        info.setTod(Event.TimeOfDay.Afternoon);
        photo.setInfo(info);
        collection.addPhoto(photo);

        // Now the time is set to the earliest time
        collection.update();

        // get the current photo, it has to be an Afternoon photo (we have at least one)
        IPhoto bestPhoto = collection.current();
        //assertEquals(bestPhoto.getInfo().getTod(), Event.TimeOfDay.Afternoon);
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
