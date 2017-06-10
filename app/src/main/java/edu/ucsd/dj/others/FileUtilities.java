package edu.ucsd.dj.others;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import edu.ucsd.dj.managers.DJPhoto;

/**
 * Created by Jonathan Jimenez on 6/8/17.
 * Class for I/O operations.
 */
public class FileUtilities {

    /**
     * Copy a photo from one location on the device to another one.
     *
     * @param fromFile
     * @param toFile
     */
    public static void copy(String fromFile, String toFile){
        File from = new File(fromFile);
        File to = new File(toFile);

        InputStream is = null;
        OutputStream os = null;
        try {
            is = new FileInputStream(from);
            os = new FileOutputStream(to);
            byte[] buffer = new byte[1024];
            int length;
            while ((length = is.read(buffer)) > 0) {
                os.write(buffer, 0, length);
            }
            is.close();
            os.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Save a given bitmap to some location on the device.
     *
     * @param bitmap
     * @param toFile
     */
    public static void save(Bitmap bitmap, String toFile){
        bitmap = BitmapLabeler.resize(bitmap);
        File to = new File(toFile);
        OutputStream os = null;
        try {
            os = new FileOutputStream(to);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, os);
            os.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Update the devices MediaStore to detect changes in the filesystem.
     *
     * @param fileName
     */
    public static void updateMediastore(String fileName){
        File file = new File(fileName);
        Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        intent.setData(Uri.fromFile(file));
        DJPhoto.getAppContext().sendBroadcast(intent);
    }


    /**
     * Delete a file that is on the device currently.
     *
     * @param fileName
     */
    public static void deleteFile(String fileName){
        File file = new File(fileName);
        file.delete();
    }

}
