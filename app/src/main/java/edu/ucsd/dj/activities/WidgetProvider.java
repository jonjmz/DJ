package edu.ucsd.dj.activities;

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
import android.os.Build;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;



import java.io.IOException;

import edu.ucsd.dj.AddressLabelStrategy;
import edu.ucsd.dj.BitmapLabeler;
import edu.ucsd.dj.Photo;
import edu.ucsd.dj.PhotoCollection;
import edu.ucsd.dj.R;

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
        Photo photo = PhotoCollection.getInstance().current();
        if (intent.getAction().equals(NEXT)) {
            photo = PhotoCollection.getInstance().next();
        }
        else if (intent.getAction().equals(PREVIOUS)) {
            photo = PhotoCollection.getInstance().previous();
        }
        else if (intent.getAction().equals(KARMA)) {
            photo.setHasKarma(!photo.hasKarma());
            String result = "Karma " + (photo.hasKarma() ? "given, tap to remove." : "taken.");
            Toast.makeText(context, result, Toast.LENGTH_SHORT).show();

            Log.i("Testing", "This is action: " + intent.getAction());
        }
        else if (intent.getAction().equals(RELEASE)) {
            photo.release();
            photo = PhotoCollection.getInstance().next();

            Log.i("Testing", "This is action: " + intent.getAction());
        }

        set(photo, context);
        highlightKarma(context, photo, intent);
    }

    private void set(Photo photo, Context context) {
        try {
            BitmapLabeler labeler = new BitmapLabeler();
            AddressLabelStrategy addressLabelMaker = new AddressLabelStrategy(context);

            String label = "";
            if (photo.getInfo().hasValidCoordinates()) {
                label = addressLabelMaker.getLabel(photo.getInfo());
            }

            Bitmap bitmap = BitmapFactory.decodeFile(photo.getPathname());
            Bitmap newBackground = labeler.label( bitmap, label, context );

            WallpaperManager.getInstance(context).setBitmap( newBackground );

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void highlightKarma(Context context, Photo photo, Intent intent) {
        RemoteViews remoteViews = new RemoteViews( context.getPackageName(), R.layout.simple_widget);

        if (photo.hasKarma())
            remoteViews.setImageViewResource(R.id.heart, R.mipmap.filled);
        else
            remoteViews.setImageViewResource(R.id.heart, R.mipmap.open);

        ComponentName thisWidget = new ComponentName( context, WidgetProvider.class );
        AppWidgetManager.getInstance( context ).updateAppWidget( thisWidget, remoteViews );
    }
}