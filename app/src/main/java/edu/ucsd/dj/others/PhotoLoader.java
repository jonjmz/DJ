package edu.ucsd.dj.others;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import java.util.List;
import java.util.ArrayList;

import edu.ucsd.dj.interfaces.IRemotePhotoStore;
import edu.ucsd.dj.interfaces.IUser;
import edu.ucsd.dj.managers.DJPhoto;
import edu.ucsd.dj.models.FirebaseDB;
import edu.ucsd.dj.models.MockEvent;
import edu.ucsd.dj.models.Photo;
import edu.ucsd.dj.models.TestUser;

/**
 * Load photos from the phone's gallery
 * Created by nguyen on 5/9/2017.
 */
public class PhotoLoader  {

    private String[] projection;
    private String selectionClause;
    private String[] selectionArgs;
    private String sortOrder;
    private Uri images;

    /**
     * Default constructor. Initializing params for the query.
     */
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

    /**
     * Get the list of photos in the phone using the current query
     * @return list of photos in the phone
     */
    public List<Photo> getPhotos(){

        List<Photo> album = new ArrayList<>();

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
                    int index = cur.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);

                    CoordinatesLoader latLngLoader = new CoordinatesLoader();
                    DateTimeLoader dateLoader = new DateTimeLoader();
                    do {
                        pathName = cur.getString(index);

                        Log.i("ListingImages",  " path_name: " + pathName);

                        Photo photo = new Photo(pathName);
                        latLngLoader.loadCoordinatesFor(pathName, photo.getInfo());
                        dateLoader.loadDateFor(pathName, photo.getInfo());

                        album.add(photo);
                        testDB(photo);
                    } while (cur.moveToNext());
                }
            }
            catch(Exception e){
                Log.d("Cursor exception", e.getMessage());
            }
        }

        return album;
    }

    private void testDB(Photo photo){
        IUser user = new TestUser();
        FirebaseDB db = new FirebaseDB();
        db.uploadPhoto(user, photo);
        db.uploadMetadata(user, photo, new MockEvent());
    }


}