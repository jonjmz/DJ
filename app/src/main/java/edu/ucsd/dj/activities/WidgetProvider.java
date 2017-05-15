package edu.ucsd.dj.activities;

import android.annotation.TargetApi;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

import edu.ucsd.dj.DJWallpaper;
import edu.ucsd.dj.LocationProvider;
import edu.ucsd.dj.Photo;
import edu.ucsd.dj.PhotoCollection;
import edu.ucsd.dj.R;
import edu.ucsd.dj.Settings;

/**
 * Created by Josh on 5/2/2017.
 */
@TargetApi(Build.VERSION_CODES.CUPCAKE)
public class WidgetProvider extends AppWidgetProvider {
    private static String NEXT = "next";
    private static String PREVIOUS = "previous";
    private LocationProvider locationProvider;
    @Override
    public void onRestored(Context context, int[] oldWidgetIds, int[] newWidgetIds) {
        super.onRestored(context, oldWidgetIds, newWidgetIds);
    }

    @Override
    public void onDisabled(Context context) {
        super.onDisabled(context);
        if(locationProvider != null)
            locationProvider.disconnect();
    }

    private static String RELEASE = "release";
    private static String KARMA = "karma";

    @Override
    public void onEnabled(Context context) {
        super.onEnabled(context);
        locationProvider = new LocationProvider();
        locationProvider.setCurrentLocation(null);
        locationProvider.connect();
        Settings.initTimer();
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        final int count = appWidgetIds.length;

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

        PhotoCollection collection = PhotoCollection.getInstance();

        if (!collection.isEmpty()) {
            Photo photo = collection.current();

            if (intent.getAction().equals(NEXT)) {
                photo = collection.next();

                Log.i("onReceive: NEXT ", "Photo: [" +
                        collection.getCurrentIndex() + "/" + (collection.size() - 1) + "]" +
                        " Score: " + photo.getScore());
            }
            else if (intent.getAction().equals(PREVIOUS)) {
                photo = collection.previous();

                Log.i("onReceive: PREVIOUS ", "Photo: [" +
                        collection.getCurrentIndex() + "/" + (collection.size() - 1) + "]"
                        + " Score: " + photo.getScore());
            }
            else if (intent.getAction().equals(KARMA)) {
                photo.setHasKarma(!photo.hasKarma());
                String result = "Karma " + (photo.hasKarma() ? "given, tap to remove." : "taken.");
                Toast.makeText(context, result, Toast.LENGTH_SHORT).show();

                Log.i("onReceive: KARMA ", "Set karma to " + photo.hasKarma() + " for Photo: [" +
                        collection.getCurrentIndex() + "/" +
                        (collection.size() - 1) + "]" +
                        " Score: " + photo.getScore());
            }
            else if (intent.getAction().equals(RELEASE)) {

                Log.i("onReceive: RELEASE ", "Photo: [" +
                        collection.getCurrentIndex() + "/" +
                        (collection.size() - 1) + "]" + "will be " +
                        "released." +
                        " Score: " + photo.getScore());

                collection.release();

                if (!collection.isEmpty()) {

                    Log.i("onReceive: RELEASE ", "Setting to new current photo.");
                    photo = collection.current();
                }
                else {
                    Log.i("onReceive: RELEASE ", "After release the album is empty. Should set " +
                            "default.");
                    photo = null;
                }
            }

            if (photo != null) {
                highlightKarma(context, photo, intent);
            }

            DJWallpaper.getInstance().set(photo);
        }
    }

    private void highlightKarma(Context context, Photo photo, Intent intent) {

        Log.i("highlightKarma() ", "Updated widget to reflect photo karma.");

        RemoteViews remoteViews = new RemoteViews( context.getPackageName(), R.layout.simple_widget);

        if (photo.hasKarma())
            remoteViews.setImageViewResource(R.id.heart, R.mipmap.filled);
        else
            remoteViews.setImageViewResource(R.id.heart, R.mipmap.open);

        ComponentName thisWidget = new ComponentName( context, WidgetProvider.class );
        AppWidgetManager.getInstance( context ).updateAppWidget( thisWidget, remoteViews );
    }
}