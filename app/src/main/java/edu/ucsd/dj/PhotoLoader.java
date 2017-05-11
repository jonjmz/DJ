package edu.ucsd.dj;

import android.content.Context;
import android.content.ContextWrapper;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by nguyen on 5/9/2017.
 */

public class PhotoLoader extends ContextWrapper {
    String[] projection;
    String selectionClause;
    String[] selectionArgs;
    String sortOrder;
    Uri images;

    public PhotoLoader(Context context) {
        super(context);
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
        try {
            Cursor cur = getContentResolver().query(images,
                    projection, // Which columns to return
                    selectionClause,       // Which rows to return (all rows)
                    selectionArgs,       // Selection arguments (none)
                    sortOrder        // Ordering
            );
            ArrayList<Photo> album = new ArrayList<>();
            //Checking if the cursor is valid
            if(cur == null){
                //Cursor is null, meaning there is an error that needs to be resolved
                Log.d("NullPointerException", "Cursor is null");
                return album;
            }
            //If there is no photo in the gallery, perform adding a default photo into the album
            else if(cur.getCount() < 1){
                //TODO handle a null/0 count photocollection
                return album;
            }
            else {
                Log.i("DeviceImageManager", " query count=" + cur.getCount());
                cur.moveToFirst();
                long date;
                String pathName;
                int dateColumn = cur.getColumnIndexOrThrow(
                        MediaStore.Images.Media.DATE_TAKEN);
                int index = cur.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);

                do {
                    // Get the field values
                    date = cur.getLong(dateColumn);
                    pathName = cur.getString(index);

                    // Do something with the values.
                    Log.i("ListingImages", "  date_taken: " + date +
                            " path_name: " + pathName);

                    Photo photo = new Photo(pathName, date);
                    CoordinatesLoader latLngLoader = new CoordinatesLoader();
                    AddressLoader addressLoader = new AddressLoader(getApplicationContext());
                    PhotoLabeler labeler = new PhotoLabeler();
                    latLngLoader.loadCoordinatesFor(photo);
                    if (photo.getInfo().hasValidCoordinates()) {
                        addressLoader.loadAddressFor(photo);
                    }

                    photo.calculateScore();
                    album.add(photo);


                } while (cur.moveToNext());
            }
            return album;
        }
        catch(Exception e){
            Log.d("Cursor exception", e.getMessage());
        }
        return null;
    }

}
