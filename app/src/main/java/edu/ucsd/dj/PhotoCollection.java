package edu.ucsd.dj;

import android.content.Context;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;

import edu.ucsd.dj.interfaces.IRating;

/**
 * Created by jakesutton on 5/6/17.
 */
public class PhotoCollection {

    // Collection of photos queried from the gallery
    private List<Photo> album;
    // A queue to handle previous feature, keeping track of your browsing history
    private Deque<Photo> history;
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
        history = new LinkedList<Photo>();
        curr = 0;
    }

    /**
     *  Updates the list of photo by querying from the current gallery and put
     *  them into the current photo array
     *
     */
    public void update(Context context) {

        PhotoLoader loader = new PhotoLoader(context);
        ArrayList<Photo> newAlbum = loader.getPhotos();

        //TODO optimization problem
        for(Photo photo: newAlbum){
            if(!album.contains(photo) && !releasedList.contains(photo)) {
                album.add( photo );
            }
        }

        Collections.sort(album);
    }

    /**
     *  Updates the values of photo, used after changing settings
     *
     */
    public void sort() {
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
        releasedList.add( album.remove(curr) );
    }

    /**
     * Return the next photo from the current list
     * If there is no image, return the stock image
     * Increases the current pointer to the next image
     * If a photo is blacklisted, it will not be returned
     * @return A photo object from the album data structure
     */
    public Photo next() {
        // Add to history
        history.addFirst(album.get(curr));
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
        // Loop back to start if need be
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

    public boolean hasHistory(){
        return !history.isEmpty();
    }
    /**
     * Return the previous photo from user's history
     *
     * @return A photo object from the history data structure
     */
    public Photo previous() {
        curr--;
        return history.removeFirst();
    }

    /**
     * Returns true if this collection is empty.
     * @return true if collection is empty.
     */
    public boolean isEmpty() {
        return album.isEmpty();
    }
}
