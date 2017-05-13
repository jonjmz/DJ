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
import android.graphics.Color;
import android.os.Build;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.RemoteViews;
import android.widget.Toast;



import java.io.IOException;
import java.util.Random;

/**
 * Created by Josh on 5/2/2017.
 */

import java.util.Random;
import java.util.Set;

import static android.content.ContentValues.TAG;
import static edu.ucsd.dj.R.id.heart;

@TargetApi(Build.VERSION_CODES.CUPCAKE)
public class WidgetProvider extends AppWidgetProvider {
    private static String NEXT = "next";
    private static String PREVIOUS = "previous";
    private static String RELEASE = "release";
    private static String KARMA = "karma";
    private RemoteViews rViews;

    private static int doubleClickCounter = 0;
    @Override
    public void onEnabled(Context context) {
        super.onEnabled(context);

        PhotoCollection.getInstance().update( context );
    }
    private void highlightKarma(Context context, Photo photo, Intent intent) {
        RemoteViews remoteViews = new RemoteViews( context.getPackageName(), R.layout.simple_widget);
//        ImageButton button = (ImageButton) remoteViews.getLayoutId()
        if (photo.hasKarma()) {
            Toast.makeText(context, "++++++++++", Toast.LENGTH_SHORT).show();
            remoteViews.setImageViewResource(R.id.heart, R.mipmap.filled);
        }
        else if (!photo.hasKarma()){
            Toast.makeText(context, "----------", Toast.LENGTH_SHORT).show();
            remoteViews.setImageViewResource(R.id.heart, R.mipmap.open);
        }
        int appWidgetId = intent.getIntExtra("appWidgetId", -1);
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        appWidgetManager.updateAppWidget(appWidgetId, remoteViews);

//        ComponentName cn = new ComponentName(context, WidgetProvider.class);
//        onUpdate(context, AppWidgetManager.getInstance(context), appWidgetIds);

        Toast.makeText(context, "leaving highlight checker", Toast.LENGTH_SHORT).show();
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
            Intent intent = new Intent ("highlightKarma");
            intent.putExtra("appWidgetId", widgetId);

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

            remoteViews.setImageViewResource(R.id.heart, R.mipmap.open);

            remoteViews.setOnClickPendingIntent(R.id.left, pendingIntentPrevious);
            remoteViews.setOnClickPendingIntent(R.id.trash, pendingIntentRelease);
            remoteViews.setOnClickPendingIntent(R.id.heart, pendingIntentKarma);
            remoteViews.setOnClickPendingIntent(R.id.right, pendingIntentNext);
//            Photo photo = PhotoCollection.getInstance().current();
//            highlightKarma(context, photo);

            appWidgetManager.updateAppWidget(widgetId, remoteViews);
        }
    }



    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        Log.i("Testing ", "onReceive: " + intent.getAction());

//        ComponentName thisAppWidget = new ComponentName(context.getPackageName(), WidgetProvider.class.getName());
//        int[] appWidgetIds = WidgetProvider.getAppWidgetIds(thisAppWidget);

        if (intent.getAction().equals(NEXT)) {

            Photo photo = PhotoCollection.getInstance().next();
            try {
                WallpaperManager.getInstance(context).setBitmap( photo.getBitmap() );
                highlightKarma(context, photo, intent);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else if (intent.getAction().equals(PREVIOUS)) {
            Photo photo = PhotoCollection.getInstance().previous();
            try {
                WallpaperManager.getInstance(context).setBitmap( photo.getBitmap() );
                highlightKarma(context, photo, intent);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else if (intent.getAction().equals(KARMA)) {
            Photo photo = PhotoCollection.getInstance().current();
            if (!photo.hasKarma() && photo.isKarmable()){
                photo.giveKarma();
                Toast.makeText(context, "Karma given, tap again to remove", Toast.LENGTH_SHORT).show();
            }
            else if (photo.isKarmable()){
                photo.removeKarma();
                Toast.makeText(context, "Karma taken", Toast.LENGTH_SHORT).show();
            }
            highlightKarma(context, photo, intent);
            Log.i("Testing", "This is action: " + intent.getAction());
        }
        else if (intent.getAction().equals(RELEASE)) {
            Photo photo = PhotoCollection.getInstance().next();
            if (photo.isReleasable()){
                Toast.makeText(context, "Photo released", Toast.LENGTH_SHORT).show();
                photo.release();
                photo = PhotoCollection.getInstance().next();
                highlightKarma(context, photo, intent);
                try {
                    WallpaperManager.getInstance(context).setBitmap( photo.getBitmap() );
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }


            Log.i("Testing", "This is action: " + intent.getAction());
        }
    }


}