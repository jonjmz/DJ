package edu.ucsd.dj;

import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import edu.ucsd.dj.interfaces.IRating;
import edu.ucsd.dj.managers.Settings;
import edu.ucsd.dj.models.Photo;
import edu.ucsd.dj.strategies.RatingStrategy;

/**
 * Holds all photos and core functionality of the application
 * Created by jakesutton on 5/6/17.
 */
public class PhotoCollection {

    // Collection of photos queried from the gallery
    private List<Photo> album;
    // Collection of photos queried from the gallery, but not shown
    private List<Photo> releasedList;
    // Current pointer to the image that's being set as the wallpaper
    private int curr;

    private static final PhotoCollection ourInstance = new PhotoCollection();

    public static PhotoCollection getInstance() {
        return ourInstance;
    }

    protected PhotoCollection() {
        album = new ArrayList<Photo>();
        releasedList = new ArrayList<Photo>();
        curr = 0;
    }

    /**
     *  Updates the list of photo by querying from the current gallery and put
     *  them into the current photo array
     *
     */
    public void update() {

        PhotoLoader loader = new PhotoLoader();
        ArrayList<Photo> newAlbum = loader.getPhotos();

        //TODO optimization problem
        for(Photo photo: newAlbum){
            if(!album.contains(photo) && !releasedList.contains(photo)) {
                album.add( photo );
            }
        }

        sort();
    }

    /**
     *  Updates the values of photo, used after changing settings
     *
     */
    public void sort() {

        Log.i(this.getClass().toString(), "Running sort()");

        IRating rating = new RatingStrategy(Settings.isConsideringRecency(),
                Settings.isConsideringTOD(),
                Settings.isConsideringProximity());

        //TODO optimization problem
        for(Photo photo: album){
            photo.setScore(rating.rate(photo.getInfo(), photo.hasKarma()));
        }

        Collections.sort(album);

        curr = 0;
    }

    /**
     * Releases the current photo from the album
     * next() should be called immediately after this method
     */
    public void release() {

        Photo photo = album.remove(curr);
        if (curr == album.size()) curr--;

        releasedList.add( photo );
    }

    /**
     * Return the next photo from the current list
     * If there is no image, return the stock image
     * Increases the current pointer to the next image
     * If a photo is blacklisted, it will not be returned
     * @return A photo object from the album data structure
     */
    public Photo next() {
        switchPhoto();
        if(album.size() < 1){
            return null;
        } else {
            return album.get(curr);
        }
    }

    /**
     * Return the next photo from the current list
     * If there is no image, return the stock image
     * Increases the current pointer to the next image
     * If a photo is blacklisted, it will not be returned
     * @return A photo object from the album data structure
     */
    private void switchPhoto() {
        curr++;

        if (curr == album.size()) {
            sort();
        }
    }

    /**
     * @return The current photo object from the album
     */
    public Photo current() {
        return album.get(curr);
    }

    /**
     * TODO
     * @return
     */
    public int getCurrentIndex() {
        return curr;
    }

    /**
     *
     * @return
     */
    public int size() {
        return album.size();
    }

    /**
     * Return the previous photo from user's history
     *
     * @return A photo object from the history data structure
     */
    public Photo previous() {
        curr--;
        if (curr < 0) curr = album.size() - 1;
        return album.get( curr );
    }

    /**
     * Returns true if this collection is empty.
     * @return true if collection is empty.
     */
    public boolean isEmpty() {
        return album.isEmpty();
    }
}
