package edu.ucsd.dj.others;

import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import edu.ucsd.dj.interfaces.IRemotePhotoStore;
import edu.ucsd.dj.interfaces.models.IAddressable;
import edu.ucsd.dj.interfaces.models.IUser;
import edu.ucsd.dj.interfaces.observers.ICollectionObserver;
import edu.ucsd.dj.interfaces.observers.ICollectionSubject;
import edu.ucsd.dj.interfaces.IRating;
import edu.ucsd.dj.interfaces.observers.ILocationTrackerObserver;
import edu.ucsd.dj.interfaces.observers.IRatingObserver;
import edu.ucsd.dj.interfaces.observers.ISettingsObserver;
import edu.ucsd.dj.managers.Settings;
import edu.ucsd.dj.models.DJFriends;
import edu.ucsd.dj.models.DJPrimaryUser;
import edu.ucsd.dj.models.FirebaseDB;
import edu.ucsd.dj.models.Photo;

/**
 * Holds all photos and core functionality of the application
 * Created by Jake Sutton on 5/6/17.
 */
public class PhotoCollection implements ICollectionSubject,
                                        ILocationTrackerObserver,
                                        IRatingObserver,
                                        ISettingsObserver{

    // Current pointer to the image that's being set as the wallpaper
    private int curr;
    // Collection of photos queried from the gallery
    private List<Photo> album;
    // Collection of photos queried from the gallery, but not shown
    private List<Photo> releasedList;
    // Objects observing the photo collection
    private List<ICollectionObserver> observers;

    private IRating rating;

    private static final PhotoCollection ourInstance = new PhotoCollection();

    public static PhotoCollection getInstance() {
        return ourInstance;
    }

    public PhotoCollection() {
        curr = 0;
        album = new ArrayList<>();
        releasedList = new LinkedList<>();
        observers = new LinkedList<>();
    }

    @Override
    public void updateLocation(IAddressable loc) {
        rating.setCurrentLocation(loc);
        sort();
    }

    /**
     *  Updates the list of photo by querying from the current gallery and put
     *  them into the current photo array
     *
     */
    public void update() {

        //TODO REFACTOR
        if(Settings.getInstance().isViewingMyAlbum()){
            PhotoLoader loader = new PhotoLoader("DejaPhoto");
            List<Photo> newAlbum = loader.getPhotos();
            //TODO optimization problem
            for(Photo photo: newAlbum){
                if(!album.contains(photo) && !releasedList.contains(photo)) {
                    album.add( photo );
                }
            }

        }

        else{
            DJPrimaryUser user = new DJPrimaryUser();
            for(Iterator<Photo> it = album.listIterator(); it.hasNext();){
                Photo temp = it.next();
                if(temp.getUid().startsWith(user.getUserId()))
                    it.remove();
            }

        }

        /*
        //TODO REFACTOR
        if(Settings.getInstance().isViewingFriendsAlbum()){
            PhotoLoader loader = new PhotoLoader("DejaPhotoFriends");
            List<Photo> newAlbum = loader.getPhotos();
            for(Photo photo: newAlbum) {
                if (!album.contains(photo) && !releasedList.contains(photo)) {
                    album.add(photo);
                }
            }
        }

        //TODO delete
        else{
            DJFriends friendsList = new DJFriends();
            for(IUser friend: friendsList.getFriends()){
                for(Iterator<Photo> it = album.listIterator(); it.hasNext();){
                    Photo temp = it.next();
                    if(temp.getUid().startsWith(friend.getUserId()))
                        it.remove();
                }
            }
        }
        */

        sort(); // this notifies the observers
    }

    public void addPhoto(Photo photo){
        if (!album.contains(photo) && !releasedList.contains(photo)) {
            album.add(photo);
        }
        sort();
    }

    public void updatePhotoFromStorage(Photo photo){
        for(int i = 0; i < album.size(); i++){
            if(photo.getUid().equals(album.get(i).getUid())){
                album.set(i, photo);
                return;
            }
        }
    }
    /**
     *  Updates the values of photo, used after changing settings
     */
    public void sort() {

        Log.i(this.getClass().toString(), "Running sort()");

        for(Photo photo: album){
            photo.setScore(rating.rate(photo));
        }

        Collections.sort(album);

        curr = 0;

        notifyObservers();
    }

    /**
     * Releases the current photo from the album
     * next() should be called immediately after this method
     */
    public void release() {

        Photo photo = album.remove(curr);
        if (curr == album.size()) curr--;

        releasedList.add( photo );

        notifyObservers();
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
        notifyObservers();
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

        notifyObservers();

        return album.get( curr );
    }

    /**
     * Returns true if this collection is empty.
     * @return true if collection is empty.
     */
    public boolean isEmpty() {
        return album.isEmpty();
    }

    public IRating getRating() {
        return rating;
    }

    public void setRatingStrategy(IRating rating) {
        this.rating = rating;
        this.rating.addObserver(this);
    }

    @Override
    public void notifyObservers() {
        for (ICollectionObserver o : observers) {
            o.update();
        }
    }

    @Override
    public void addObserver(ICollectionObserver o) {
        observers.add(o);
    }

    @Override
    public void removeObserver(ICollectionObserver o) {
        observers.remove(o);
    }

    @Override
    public void updateRatingChange() {
        sort();
    }

    public List<Photo> getAlbum() {
        return album;
    }

}


