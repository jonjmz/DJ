package edu.ucsd.dj;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.LinkedList;

/**
 * @author Jake Sutton & Huy Hac Nguyen
 * @since 5/3/2017
 * A photo collection class that handles processing photos from the gallery for
 * the widget.The class frequently update and process the photos from the
 * gallery and put it in a collection. Depending on the mode (Deja Vu or normal)
 * the class provides the backend functionality for the
 * user interface
 *
 *
 */
public class DJPhotoCollection implements Serializable  {

    //Context passed to class from the current Activity
    private Context context;
    //Collection of photos queried from the gallery
    private ArrayList<Photo> album;
    //A queue to handle previous feature, keeping track of your browsing history
    private Deque<Photo> history;
    //Current pointer to the image that's being set as the wallpaper
    private int curr;


    /**
     * Constructor
     * @param ct Current context of the activity
     */
    public DJPhotoCollection(Context ct) {
        context = ct;
        album = new ArrayList<Photo>();
        history = new LinkedList<Photo>();
        curr = 0;
    }

    /**
     *  Updates the list of photo by querying from the current gallery and put
     *  them into the current photo array
     *
     */
    public void update() {
        // which image properties are we querying
        String[] projection = new String[] {
            MediaStore.Images.Media._ID,
            MediaStore.Images.Media.BUCKET_DISPLAY_NAME,
            MediaStore.Images.Media.DATE_TAKEN,
            MediaStore.Images.Media.DATA
        };

        // content:// style URI for the "primary" external storage volume
        Uri images = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;

        // Make the query.
        Cursor cur = context.getContentResolver().query(images,
            projection, // Which columns to return
            null,       // Which rows to return (all rows)
            null,       // Selection arguments (none)
            null        // Ordering
        );
        if ( cur != null && cur.getCount() > 0 ) {
            //Logging the size of the image gallery
            Log.i("DeviceImageManager", " query count=" + cur.getCount());

            //Moving the cursor to the first image of the gallery
            if (cur.moveToFirst()) {

                //Taking the name of the album and the date
                String bucket;
                String date;
                int bucketColumn = cur.getColumnIndexOrThrow(
                        MediaStore.Images.Media.BUCKET_DISPLAY_NAME);

                int dateColumn = cur.getColumnIndexOrThrow(
                        MediaStore.Images.Media.DATE_TAKEN);

                do {
                    // Get the field values
                    bucket = cur.getString(bucketColumn);
                    date = cur.getString(dateColumn);

                    // Do something with the values.
                    Log.i("ListingImages", " bucket=" + bucket
                            + "  date_taken=" + date);

                    //Get the index column of the filepath
                    int index = cur.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);


                String data = cur.getString(index); // the filepath
                Photo photo = new Photo(data);

                // Checking for duplicate
                if (!album.contains(photo)) {
                    album.add( photo );
                }

                } while (cur.moveToNext());

            }

            Collections.sort(album);
        }
    }

    /**
     * Return the next photo from the current list
     * If there is no image, return the stock image
     * Increases the current pointer to the next image
     * If a photo is blacklisted, it will not be returned
     * @return A photo object from the album data structure
     */
    public Photo next() {

        // TODO return the special image
        if (album.size() < 1) return null;

        history.addFirst(album.get(curr));

        curr++;

        // Loop back to start if need be
        if (curr == album.size()) curr = 0;

        // TODO check for removed from album
        Photo result = album.get(curr);
        return result;
    }

    /**
     * Return the previous photo from user's history
     *
     * @return A photo object from the history data structure
     */
    public Photo previous() {

        --curr;
        if (curr < 0) curr += album.size();

        return history.removeFirst();
    }

    /**
     * Checks if there is a previous photo in the history data structure
     *
     * @return true if there is a photo in history
     *         false if there isn't a photo in history
     */
    public boolean hasPrevious() {
        return !history.isEmpty();
    }

    /**
     * Save current album to file using album field as name.
     *
     * @param context The current state of the application
     * @see {@linktourl http://stackoverflow.com/questions/4118751/how-do-i-serialize-an-object-and-save-it-to-a-file-in-android}
     */
    public void saveToFile(Context context){
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
     * @param context Current state of the application
     * @param albumName the name of the album that needs to be loaded
     * @see {@linktourl http://stackoverflow.com/questions/4118751/how-do-i-serialize-an-object-and-save-it-to-a-file-in-android}
     */
    public static DJPhotoCollection loadFromFile(Context context, String albumName){
        try {
            FileInputStream fis = context.openFileInput(albumName + ".album");
            ObjectInputStream is = new ObjectInputStream(fis);
            DJPhotoCollection tempAlbum = (DJPhotoCollection) is.readObject();
            is.close();
            fis.close();
            // Load new photos from album
            tempAlbum.update();
            return tempAlbum;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
}
