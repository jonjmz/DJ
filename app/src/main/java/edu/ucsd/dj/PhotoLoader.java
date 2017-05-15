package edu.ucsd.dj;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import java.util.ArrayList;

import edu.ucsd.dj.managers.DJPhoto;
import edu.ucsd.dj.models.Photo;

/**
 * Created by nguyen on 5/9/2017.
 */
public class PhotoLoader  {
    String[] projection;
    String selectionClause;
    String[] selectionArgs;
    String sortOrder;
    Uri images;

    public PhotoLoader() {
        projection = new String[] {
                MediaStore.Images.Media.DATE_TAKEN,
                MediaStore.Images.Media.DATA
        };
        selectionClause = null;
        selectionArgs = null;
        sortOrder = null;
        images = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
    }

    //TODO Do some customizable query methods to optimize later

    public ArrayList<Photo> getPhotos(){

        ArrayList<Photo> album = new ArrayList<>();

        ContentResolver resolver;
        if (DJPhoto.getAppContext() != null) {
            resolver = DJPhoto.getAppContext().getContentResolver();

            try {
                Cursor cur = resolver.query(
                        images,
                        projection, // Which columns to return
                        selectionClause,       // Which rows to return (all rows)
                        selectionArgs,       // Selection arguments (none)
                        sortOrder        // Ordering
                );

                //Checking if the cursor is valid
                if(cur == null){
                    //Cursor is null, meaning there is an error that needs to be resolved
                    Log.d("NullPointerException", "Cursor is null");
                    return album;
                } else if(cur.getCount() < 1){
                    // there is no photo in the gallery, perform adding a default photo into the album

                    //TODO handle a null/0 count photocollection

                    return album;
                }
                else {
                    Log.i("DeviceImageManager", " query count=" + cur.getCount());

                    cur.moveToFirst();
                    long date;
                    String pathName;
                    int dateColumn = cur.getColumnIndexOrThrow(MediaStore.Images.Media.DATE_TAKEN);
                    int index = cur.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);

                    CoordinatesLoader latLngLoader = new CoordinatesLoader();
                    do {
                        date = cur.getLong(dateColumn);
                        pathName = cur.getString(index);

                        Log.i("ListingImages", "  date_taken: " + date +
                                " path_name: " + pathName);

                        Photo photo = new Photo(pathName, date);
                        latLngLoader.loadCoordinatesFor(pathName, photo.getInfo());

                        album.add(photo);

                    } while (cur.moveToNext());
                }
            }
            catch(Exception e){
                Log.d("Cursor exception", e.getMessage());
            }
        }

        return album;
    }

}
