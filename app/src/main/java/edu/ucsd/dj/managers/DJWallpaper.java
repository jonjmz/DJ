package edu.ucsd.dj.managers;

import android.app.WallpaperManager;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.IOException;

import edu.ucsd.dj.activities.WidgetProvider;
import edu.ucsd.dj.interfaces.observers.ICollectionObserver;
import edu.ucsd.dj.models.DJPrimaryUser;
import edu.ucsd.dj.others.PhotoCollection;
import edu.ucsd.dj.strategies.AddressLabelStrategy;
import edu.ucsd.dj.others.BitmapLabeler;
import edu.ucsd.dj.R;
import edu.ucsd.dj.models.Photo;

/**
 * Wallpaper manager that handles setting the wallpaper for the phone
 * Created by Jake Sutton on 5/13/17.
 */
public class DJWallpaper implements ICollectionObserver {
    private static final DJWallpaper ourInstance = new DJWallpaper();
    private BitmapLabeler labeler;
    private AddressLabelStrategy labelStrategy;
    private Bitmap defaultPhoto;

    public static DJWallpaper getInstance() {
        return ourInstance;
    }

    private DJWallpaper() {
        labeler = new BitmapLabeler();
    }

    /**
     * Set a certain photo as the new wallpaper
     * @param photo The photo to be set as the new wallpaper for the phone
     */
    public void set(Photo photo) {

        Context context = DJPhoto.getAppContext();

        if (photo == null) {
            Log.i(this.getClass().toString(), "photo passed to set(Photo photo) is null.");
            setDefault();
            return;
        }
        try {
            labelStrategy = new AddressLabelStrategy(context);
            Bitmap newBackground = null;
            BitmapFactory.Options opt = new BitmapFactory.Options();
            opt.inMutable = true;
            Bitmap bitmap = BitmapFactory.decodeFile(photo.getPathname(), opt);

            String label = "";
            if (photo.getHasCustomLocation()){
                label = photo.getCustomLocation();
            } else {
                if (photo.getInfo().getHasValidCoordinates()) {
                    label = labelStrategy.getLabel(photo.getInfo());
                }
            }

            newBackground = labeler.label( bitmap, label, context );
            newBackground = labeler.createBitmapWithKarmaLabel(newBackground, photo);
            WallpaperManager.getInstance(context).setBitmap( newBackground );

            Log.i(this.getClass().toString(), "wallpaper set successfully.");

        } catch (IOException e) {
            e.printStackTrace();
        }

        // The widget doesn't know that the wallpaper has changed. Tell it
        Intent intent = new Intent(DJPhoto.getAppContext(), WidgetProvider.class);
        intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        // Use an array and EXTRA_APPWIDGET_IDS instead of AppWidgetManager.EXTRA_APPWIDGET_ID,
        // since it seems the onUpdate() is only fired on that:
        int[] ids = {R.xml.widget_info};
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS,ids);
        DJPhoto.getAppContext().sendBroadcast(intent);
    }

    /**
     * In case there is no photo to be set as the new wallpaper,
     * a default photo from resources will be set as the new wallpaper
     */
    public void setDefault() {
        try {
            Context context = DJPhoto.getAppContext();

            defaultPhoto = BitmapFactory.decodeResource(context.getResources(),
                    R.drawable.dejaphotodefault);
            WallpaperManager.getInstance(context).setBitmap( defaultPhoto );

            Log.i(this.getClass().toString(), "Default wallpaper set.");
        } catch (Exception e) {

            Log.i(this.getClass().toString(), "Error setting default wallpaper.");
            e.printStackTrace();
        }
    }

    @Override
    public void update() {

        PhotoCollection collection = PhotoCollection.getInstance();
        if (collection.isEmpty())
            this.setDefault();
        else
            this.set( PhotoCollection.getInstance().current() );
    }
}
