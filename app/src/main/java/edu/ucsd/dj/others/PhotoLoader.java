package edu.ucsd.dj.others;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.util.List;
import java.util.ArrayList;

import edu.ucsd.dj.interfaces.models.IPhoto;
import edu.ucsd.dj.interfaces.models.IUser;
import edu.ucsd.dj.managers.DJPhoto;
import edu.ucsd.dj.models.DJPrimaryUser;
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
    public PhotoLoader(String folder) {
        projection = new String[] {
                MediaStore.Images.Media.DATE_TAKEN,
                MediaStore.Images.Media.DATA
        };
        selectionClause = MediaStore.Images.Media.DATA + " like ? ";
        selectionArgs = new String[]{"%" + folder + "%"};
        sortOrder = null;
//        Log.v("Photo Loader", Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)+File.separator+"DejaPhotoMain");
//        Log.v("Photo Loader", MediaStore.Images.Media.EXTERNAL_CONTENT_URI.toString());
//        Log.v("Photo Loader",  MediaStore.Images.Media.getContentUri(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)+File.separator+"DejaPhotoMain").toString());


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

            //TODO REFACTOR
            DJPrimaryUser user = new DJPrimaryUser();
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

                        Photo photo = new Photo(pathName, user);
                        latLngLoader.loadCoordinatesFor(pathName, photo.getInfo());
                        dateLoader.loadDateFor(pathName, photo.getInfo());

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

//    public void initMediaDir(){
//        File sdcard = Environment.getExternalStorageDirectory();
//        if (sdcard != null) {
//            File DejaPhoto = new File(sdcard, "DCIM/DejaPhoto");
//            File DejaPhotoCopied = new File(sdcard, "DCIM/DejaPhotoCopied");
//            File DejaPhotoFriends = new File(sdcard, "DCIM/DejaPhotoFriends");
//
//            if (!DejaPhoto.exists()) {
//                DejaPhoto.mkdirs();
//            }
//            if (!DejaPhotoCopied.exists()) {
//                DejaPhotoCopied.mkdirs();
//            }
//            if (!DejaPhotoFriends.exists()) {
//                DejaPhotoFriends.mkdirs();
//            }
//
//        }
//
//    }

    public static void insertPhoto(Bitmap source,
                                     final Photo photo, String album) {

        ContentResolver cr = DJPhoto.getAppContext().getContentResolver();
        String url = MediaStore.Images.Media.insertImage(cr, source, photo.getName() , photo.getUid());

        File file = new File(Environment.getExternalStorageDirectory() + "/DCIM/" + album + photo.getName());
        file.mkdirs();
        ContentValues values = new ContentValues();
//        values.put( Media.TITLE, title );
//        values.put( Images.Media.DATE_TAKEN, System.currentTimeMillis() );
//        values.put( Images.Media.BUCKET_ID, filePath.hashCode() );
//        values.put( Images.Media.BUCKET_DISPLAY_NAME, Constants.TA_PHOTO_ALBUM_NAME );
//
//        values.put( Images.Media.MIME_TYPE, "image/jpeg" );
//        values.put( Media.DESCRIPTION, context.getResources().getString( R.string.product_image_description ) );
        values.put( MediaStore.MediaColumns.DATA, url );
        Uri uri = cr.insert( Uri.fromFile(file) , values );
        MediaScannerConnection connection = new MediaScannerConnection(DJPhoto.getAppContext(), null);
        connection.connect();
        connection.scanFile(file.toString(), null);
    }

    /**
     * A copy of the Android internals StoreThumbnail method, it used with the insertPhoto to
     * populate the android.provider.MediaStore.Images.Media#insertPhoto with all the correct
     * meta data. The StoreThumbnail method is private so it must be duplicated here.
     * @see android.provider.MediaStore.Images.Media (StoreThumbnail private method)
     */
    private static final Bitmap storeThumbnail(
            ContentResolver cr,
            Bitmap source,
            long id,
            float width,
            float height,
            int kind) {

        // create the matrix to scale it
        Matrix matrix = new Matrix();

        float scaleX = width / source.getWidth();
        float scaleY = height / source.getHeight();

        matrix.setScale(scaleX, scaleY);

        Bitmap thumb = Bitmap.createBitmap(source, 0, 0,
                source.getWidth(),
                source.getHeight(), matrix,
                true
        );

        ContentValues values = new ContentValues(4);
        values.put(MediaStore.Images.Thumbnails.KIND,kind);
        values.put(MediaStore.Images.Thumbnails.IMAGE_ID,(int)id);
        values.put(MediaStore.Images.Thumbnails.HEIGHT,thumb.getHeight());
        values.put(MediaStore.Images.Thumbnails.WIDTH,thumb.getWidth());

        Uri url = cr.insert(MediaStore.Images.Thumbnails.EXTERNAL_CONTENT_URI, values);

        try {
            OutputStream thumbOut = cr.openOutputStream(url);
            thumb.compress(Bitmap.CompressFormat.JPEG, 100, thumbOut);
            thumbOut.close();
            return thumb;
        } catch (FileNotFoundException ex) {
            return null;
        } catch (IOException ex) {
            return null;
        }
    }

}