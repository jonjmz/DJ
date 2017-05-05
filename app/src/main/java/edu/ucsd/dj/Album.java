package edu.ucsd.dj;

import android.content.Context;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;

import java.io.Serializable;

/**
 * Created by jonathanjimenez on 5/1/17.
 */

public class Album implements Serializable {
    private ArrayList<Photo> photos;
    private HashSet<String> released;
    private int pos;
    private String album;

    public Album() {
        this.photos = new ArrayList<Photo>();
        this.pos = 0;
        this.album = "";
    }

    /**
     * Save current album to file using album field as name.
     * @param context
     * @see {@linktourl http://stackoverflow.com/questions/4118751/how-do-i-serialize-an-object-and-save-it-to-a-file-in-android}
     */
    private void saveToFile(Context context){
        try {
            FileOutputStream fos = context.openFileOutput(album + ".album", Context.MODE_PRIVATE);
            ObjectOutputStream os = new ObjectOutputStream(fos);
            os.writeObject(this);
            os.close();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * Load Album using album field
     * @param context
     * @see {@linktourl http://stackoverflow.com/questions/4118751/how-do-i-serialize-an-object-and-save-it-to-a-file-in-android}
     */
    private static Album loadFromFile(Context context, String albumName){
        try {
            FileInputStream fis = context.openFileInput(albumName + ".album");
            ObjectInputStream is = new ObjectInputStream(fis);
            Album tempAlbum = (Album) is.readObject();
            is.close();
            fis.close();
            // Load new photos from album
            tempAlbum.includeNewImages();
            return tempAlbum;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void includeNewImages() {
        // TODO: Loop through album and add new images
    }

    public void recalculateOrder() {
        for (Photo p : photos) {
            p.calculateScore();
        }
        Collections.sort(photos);
        pos = 0;
    }

    public Photo getNextPhoto() {
        // If album is empty return default photo
        if (photos.size() < 1) {
            return null;
        }
        pos++;
        // Loop back to start if need be
        if (pos == photos.size()) {
            pos = 0;
        }
        // TODO: Check if photo is still in album
        if (false) {
            // TODO: If not: remove photo from list
            // return what would be the next photo
            return getNextPhoto();
        }
        // Everything worked
        return photos.get(pos);
    }
}
