package edu.ucsd.dj;

/**
 * Created by Admin on 5/4/2017.
 */

import android.annotation.TargetApi;
import android.app.PendingIntent;
import android.app.WallpaperManager;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.os.Build;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;



import java.io.IOException;

/**
 * Created by Josh on 5/2/2017.
 */

@TargetApi(Build.VERSION_CODES.CUPCAKE)
public class WidgetProvider extends AppWidgetProvider {
    private static String NEXT = "next";
    private static String PREVIOUS = "previous";
    private static String RELEASE = "release";
    private static String KARMA = "karma";

    @Override
    public void onEnabled(Context context) {
        super.onEnabled(context);

        PhotoCollection.getInstance().update( context );
    }

    private void highlightKarma(Context context, Photo photo, Intent intent) {
        RemoteViews remoteViews = new RemoteViews( context.getPackageName(), R.layout.simple_widget);

        if (photo.hasKarma()) {
            remoteViews.setImageViewResource(R.id.heart, R.mipmap.filled);
        }
        else if (!photo.hasKarma()){
            remoteViews.setImageViewResource(R.id.heart, R.mipmap.open);
        }

        ComponentName thisWidget = new ComponentName( context, WidgetProvider.class );
        AppWidgetManager.getInstance( context ).updateAppWidget( thisWidget, remoteViews );
    }

    private void set(Photo photo, Context context) {
        try {
            PhotoLabeler labeler = new PhotoLabeler();
            AddressLoader loader = new AddressLoader(context);

            String label = "";
            if (photo.getInfo().hasValidCoordinates()) {
                Address address = loader.generateAddress( photo.getInfo() );
                label = labeler.generateLabel(address);
            }

            Bitmap bitmap = BitmapFactory.decodeFile(photo.getPathname());
            Bitmap newBackground = labeler.label( bitmap, label, context );

            WallpaperManager.getInstance(context).setBitmap( newBackground );

        } catch (IOException e) {
            e.printStackTrace();
        }
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
        Log.i("Testing ", "onReceive: " + intent.getAction());

        if (intent.getAction().equals(NEXT)) {

            Photo photo = PhotoCollection.getInstance().next();
            set(photo, context);
            highlightKarma(context, photo, intent);

        }
        else if (intent.getAction().equals(PREVIOUS)) {
            Photo photo = PhotoCollection.getInstance().previous();
            set(photo, context);
            highlightKarma(context, photo, intent);

        }
        else if (intent.getAction().equals(KARMA)) {
            Photo photo = PhotoCollection.getInstance().current();
            if (!photo.hasKarma()){
                photo.setHasKarma(true);
                Toast.makeText(context, "Karma given, tap again to remove", Toast.LENGTH_SHORT).show();
            }
            else{
                photo.setHasKarma(false);
                Toast.makeText(context, "Karma taken", Toast.LENGTH_SHORT).show();
            }
            highlightKarma(context, photo, intent);
            Log.i("Testing", "This is action: " + intent.getAction());
        }
        else if (intent.getAction().equals(RELEASE)) {

            Photo photo = PhotoCollection.getInstance().current();
            photo.release();
            photo = PhotoCollection.getInstance().next();
            set(photo, context);
            highlightKarma(context, photo, intent);
            Log.i("Testing", "This is action: " + intent.getAction());
        }
    }


}