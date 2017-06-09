package edu.ucsd.dj.others;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import edu.ucsd.dj.managers.DJPhoto;
import edu.ucsd.dj.managers.Settings;

/**
 * Created by jonathanjimenez on 6/8/17.
 * Class for handling i/o operations
 */

public class FileUtilities {
    public static void copy(String fromFile, String toFile){
        Bitmap bitmap = BitmapFactory.decodeFile(fromFile);
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

    public static void updateMediastore(String fileName){
        File file = new File(fileName);
        Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        intent.setData(Uri.fromFile(file));
        DJPhoto.getAppContext().sendBroadcast(intent);
    }
    public static void deleteFile(String fileName){
        File file = new File(fileName);
        file.delete();
    }

}
