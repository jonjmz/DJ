package edu.ucsd.dj;

/**
 * Created by Admin on 5/4/2017.
 */

import android.annotation.TargetApi;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.widget.RemoteViews;

import java.util.Random;

/**
 * Created by Josh on 5/2/2017.
 */

import java.util.Random;

import static android.content.ContentValues.TAG;

@TargetApi(Build.VERSION_CODES.CUPCAKE)
public class WidgetProvider extends AppWidgetProvider {
    private static String NEXT = "next";
    private static String PREVIOUS = "previous";
    private static String RELEASE = "release";
    private static String KARMA = "karma";
    private DJPhotoCollection collection;
    private DJWallpaperManager manager;

    @Override
    public void onEnabled(Context context) {
        super.onEnabled(context);
        /*
        collection = new DJPhotoCollection(context);
        collection.update();
        manager = new DJWallpaperManager(context);
        */

    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        final int count = appWidgetIds.length;
        //TODO Load from file later
        //collection.update();

        for (int i = 0; i < count; i++) {
            int widgetId = appWidgetIds[i];

            RemoteViews remoteViews = new RemoteViews(context.getPackageName(),
                    R.layout.simple_widget);

            Intent intentNext = new Intent(context, WidgetProvider.class);
            Intent intentPrevious = new Intent(context, WidgetProvider.class);
            Intent intentRelease = new Intent(context, WidgetProvider.class);
            Intent intentKarma = new Intent(context, WidgetProvider.class);

            intentNext.setAction(NEXT);
            intentPrevious.setAction(PREVIOUS);
            intentRelease.setAction(RELEASE);
            intentKarma.setAction(KARMA);
            //intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetIds);
            PendingIntent pendingIntentNext = PendingIntent.getBroadcast(context,
                    0, intentNext, PendingIntent.FLAG_UPDATE_CURRENT);
            PendingIntent pendingIntentPrevious = PendingIntent.getBroadcast(context,
                    1, intentPrevious, PendingIntent.FLAG_UPDATE_CURRENT);
            PendingIntent pendingIntentRelease = PendingIntent.getBroadcast(context,
                    2, intentRelease, PendingIntent.FLAG_UPDATE_CURRENT);
            PendingIntent pendingIntentKarma = PendingIntent.getBroadcast(context,
                    3, intentKarma, PendingIntent.FLAG_UPDATE_CURRENT);

            remoteViews.setOnClickPendingIntent(R.id.left, pendingIntentPrevious);
            remoteViews.setOnClickPendingIntent(R.id.trash, pendingIntentRelease);
            remoteViews.setOnClickPendingIntent(R.id.heart, pendingIntentKarma);
            remoteViews.setOnClickPendingIntent(R.id.right, pendingIntentNext);
            appWidgetManager.updateAppWidget(widgetId, remoteViews);
        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        Log.i("Testing ", "onReceive: " + intent.getAction());

        if (intent.getAction().equals(NEXT)) {
            //do some really cool stuff here
            Photo photo = collection.next();
            manager.setWallpaper(photo);
            Log.i("Testing" , "Setting this particular photo: " + photo.getPathname());
        }
        else if (intent.getAction().equals(PREVIOUS)) {
            //do some really cool stuff here
            Photo photo = collection.previous();
            manager.setWallpaper(photo);
            Log.i("Testing" , "Setting this particular photo: " + photo.getPathname());

        }
        else if (intent.getAction().equals(KARMA)) {
            //do some really cool stuff here
            Log.i("Testing", "This is action: " + intent.getAction());
        }
        else if (intent.getAction().equals(PREVIOUS)) {
            //do some really cool stuff here
            Log.i("Testing", "This is action: " + intent.getAction());


        }



    }
}
