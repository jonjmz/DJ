package edu.ucsd.dj;

import android.media.ExifInterface;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import edu.ucsd.dj.interfaces.IDateTimeable;

/**
 * Loads the date and time of a photo using exif standard
 * Created by jonathanjimenez on 5/14/17.
 */

public class DateTimeLoader {
    private ExifInterface exifInterface;

    /**
     * If possible, meaning if the exif data for this photo is non-null,
     * sets the photo's date to whatever is saved in the exif data.
     *
     * Methods tries each possible source of datetime until one is found that works
     */
    public void loadDateFor(String pathname, IDateTimeable info) {

        Log.i("DateTimeLoader", "Attempting to load date for " + pathname);

        try {
            exifInterface = new ExifInterface(pathname);
        } catch (IOException e) {
            e.printStackTrace();
            Log.i("DateTimeLoader", "Error: Opening ExifInterface failed for " + pathname);
            return;
        }

        Log.i("DateTimeLoader", "Success: Opening ExifInterface succeeded for " + pathname);

        // While we havn't fond a datetime in the photo, keep trying different fields
        long date = 0;
        String tempDate, tempTime;

        // TAG_DATETIME_ORIGINAL
        Log.i("DateTimeLoader", "Trying TAG_DATETIME_ORIGINAL");
        tempDate = exifInterface.getAttribute(ExifInterface.TAG_DATETIME_ORIGINAL);
        if(tempDate != null){
            SimpleDateFormat parser = new SimpleDateFormat("yyyy:MM:dd HH:mm:ss");
            try {
                date = parser.parse(tempDate).getTime();
            } catch (ParseException e) {}
            if( date != 0) {
                Log.i("DateTimeLoader", "FTF");
                info.setDateTime(date);
                return;
            }
        }

        // TAG_DATETIME
        Log.i("DateTimeLoader", "Trying TAG_DATETIME");
        tempDate = exifInterface.getAttribute(ExifInterface.TAG_DATETIME);
        if(tempDate != null){
            SimpleDateFormat parser = new SimpleDateFormat("yyyy:MM:dd HH:mm:ss");
            try {
                date = parser.parse(tempDate).getTime();
            } catch (ParseException e) {}
            if( date != 0) {
                Log.i("DateTimeLoader", "FTF");
                info.setDateTime(date);
                return;
            }
        }

        // TAG_DATETIME_DIGITIZED
        Log.i("DateTimeLoader", "Trying TAG_DATETIME_DIGITIZED");
        tempDate = exifInterface.getAttribute(ExifInterface.TAG_DATETIME_DIGITIZED);
        if(tempDate != null){
            SimpleDateFormat parser = new SimpleDateFormat("yyyy:MM:dd HH:mm:ss");
            try {
                date = parser.parse(tempDate).getTime();
            } catch (ParseException e) {}
            if( date != 0) {
                Log.i("DateTimeLoader", "FTF");
                info.setDateTime(date);
                return;
            }
        }

        // TAG_GPS_DATESTAMP
        Log.i("DateTimeLoader", "Trying TAG_GPS_DATESTAMP");
        tempDate = exifInterface.getAttribute(ExifInterface.TAG_GPS_DATESTAMP);
        tempTime = exifInterface.getAttribute(ExifInterface.TAG_GPS_TIMESTAMP);
        if(tempDate != null && tempTime != null){
            SimpleDateFormat parser = new SimpleDateFormat("yyyy:MM:dd HH:mm:ss");
            try {
                date = parser.parse(tempDate + " " + tempTime).getTime();
            } catch (ParseException e) {}
            if( date != 0) {
                info.setDateTime(date);
                return;
            }
        }

        // Maybe the normal metadata?
        date = (new Date(new File(pathname).lastModified())).getTime();
        info.setDateTime(date);
    }
}
