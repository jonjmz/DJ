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

import edu.ucsd.dj.interfaces.ILocationTrackerSubject;
import edu.ucsd.dj.others.LocationService;
import edu.ucsd.dj.models.Photo;
import edu.ucsd.dj.others.PhotoCollection;
import edu.ucsd.dj.R;

/**
 * Created by Josh on 5/2/2017.
 */
@TargetApi(Build.VERSION_CODES.CUPCAKE)
/**
 * The widget, responsible for handling next, karma, release and previous button calls
 * @see https://developer.android.com/reference/android/appwidget/AppWidgetProvider.html
 */
public class WidgetProvider extends AppWidgetProvider {
    private static String NEXT = "next";
    private static String PREVIOUS = "previous";
    private ILocationTrackerSubject locationTrackerSubject;

    /**
     * called if an instance of WidgetProvider has been restored from backup
     * @param context context in which the receiver is running
     * @param oldWidgetIds array of old widget ids
     * @param newWidgetIds array of new widget ids
     */
    @Override
    public void onRestored(Context context, int[] oldWidgetIds, int[] newWidgetIds) {
        super.onRestored(context, oldWidgetIds, newWidgetIds);
    }

    /**
     * handles widget if last widget deleted
     * disconnects from google's api
     * @param context the context in which the receiver is running
     */
    @Override
    public void onDisabled(Context context) {
        super.onDisabled(context);
//        if(locationService != null)
//            locationService.disconnect();
    }

    private static String RELEASE = "release";
    private static String KARMA = "karma";

    /**
     * called when WidgetProvider is is instantiated
     * locationService, timer is initialized
     * @param context context in which the reciever is running
     */
    @Override
    public void onEnabled(Context context) {
        super.onEnabled(context);
        locationTrackerSubject = new LocationService();
        locationTrackerSubject.addObserver(PhotoCollection.getInstance());
        //Settings.initTimer();

    }

    /**
     * handles widget updateLocation
     * sets remoteviews to the buttons and handles each click, changing background as necessary
     * @param context context in which the reciever is running
     * @param appWidgetManager an AppWidgetManager object to call the updateLocation method on
     * @param appWidgetIds array for each id of each widget, needed for updateLocation
     */
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

    /**
     * to handle a received intent broadcast
     * @param context context in which the receiver is running
     * @param intent the intent being received
     */
    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);

//        if (WidgetProvider.locationService == null) {
//            WidgetProvider.locationService = new LocationService();
//            WidgetProvider.locationService.setCurrentLocation(null);
//            WidgetProvider.locationService.connect();
//            Settings.initTimer();
//        }
//
//        Settings.initTimer();

        PhotoCollection collection = PhotoCollection.getInstance();

        if (!collection.isEmpty()) {
            Photo photo = collection.current();

            if (intent.getAction().equals(NEXT)) {
                photo = collection.next();

                Log.i("onReceive: NEXT ", "Photo: [" +
                        collection.getCurrentIndex() + "/" + (collection.size() - 1) + "]" +
                        " Score: " + photo.getScore() + " DateTime: " + photo.getInfo().timeOfDay());
            }
            else if (intent.getAction().equals(PREVIOUS)) {
                photo = collection.previous();

                Log.i("onReceive: PREVIOUS ", "Photo: [" +
                        collection.getCurrentIndex() + "/" + (collection.size() - 1) + "]"
                        + " Score: " + photo.getScore() + " DateTime: " + photo.getInfo().timeOfDay());
            }
            else if (intent.getAction().equals(KARMA)) {
                photo.setHasKarma(!photo.hasKarma());
                String result = "Karma " + (photo.hasKarma() ? "given, tap to remove." : "taken.");
                Toast.makeText(context, result, Toast.LENGTH_SHORT).show();

                Log.i("onReceive: KARMA ", "Set karma to " + photo.hasKarma() + " for Photo: [" +
                        collection.getCurrentIndex() + "/" +
                        (collection.size() - 1) + "]" +
                        " Score: " + photo.getScore() + " DateTime: " + photo.getInfo().timeOfDay());
            }
            else if (intent.getAction().equals(RELEASE)) {

                Log.i("onReceive: RELEASE ", "Photo: [" +
                        collection.getCurrentIndex() + "/" +
                        (collection.size() - 1) + "]" + "will be " +
                        "released." +
                        " Score: " + photo.getScore() + " DateTime: " + photo.getInfo().timeOfDay());

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
                highlightKarma(context, photo);
            }
        }
    }

    /**
     * to handle karma button, sets filled heart if pic has karma, open for none
     * @param context context in which receiver is running
     * @param photo photo to be checked for karma
     */
    private void highlightKarma(Context context, Photo photo) {

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