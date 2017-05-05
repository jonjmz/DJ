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
 * TODO
 */
public class DJPhotoCollection implements Serializable  {

    private Context context;
    private ArrayList<Photo> album;
    private Deque<Photo> history;
    private int curr;

    public DJPhotoCollection(Context ct) {
        context = ct;
        album = new ArrayList<Photo>();
        history = new LinkedList<Photo>();
        curr = 0;
    }

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
            Log.i("DeviceImageManager", " query count=" + cur.getCount());
            if (cur.moveToFirst()) {
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

                    int index = cur.getColumnIndex(MediaStore.Images.Media.DATA);


                String data = cur.getString(index); // the filepath
                Photo photo = new Photo(data);

                if (!album.contains(photo)) {
                    album.add( photo );
                }

                } while (cur.moveToNext());

            }

            Collections.sort(album);
        }
    }

    /**
     * TODO
     *
     * @return
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
     * TODO
     * @return
     */
    public Photo previous() {

        --curr;
        if (curr < 0) curr += album.size();

        return history.removeFirst();
    }

    /**
     * TODO
     *
     * @return
     */
    public boolean hasPrevious() {
        return !history.isEmpty();
    }

    /**
     * Save current album to file using album field as name.
     * @param context
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
     * @param context
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
