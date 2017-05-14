package edu.ucsd.dj;

import java.util.TimerTask;

/**
 * Created by jakesutton on 5/14/17.
 */

public class PhotoUpdateTask extends TimerTask {

    @Override
    public void run() {
        PhotoCollection collection = PhotoCollection.getInstance();
        collection.update();

        DJWallpaper.getInstance().set( collection.current() );
    }
}
