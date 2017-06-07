package edu.ucsd.dj.managers;

import android.os.Environment;
import android.util.Log;

import java.io.File;

/**
 * Created by jonathanjimenez on 6/6/17.
 */

public class StartUpUtilities {
    public static void CreateAlbums(){
        String[] RequiredDirectories = {"DejaPhoto", "DejaPhotoFriends", "DejaPhotoCopied"};
        for (String directoryName : RequiredDirectories){
            // Create the directory
            File directory = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)+File.separator+directoryName);
            directory.mkdirs();
        }
    }
}
