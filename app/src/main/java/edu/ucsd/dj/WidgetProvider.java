package edu.ucsd.dj;

/**
 * Created by Admin on 5/4/2017.
 */

import android.annotation.TargetApi;
import android.app.PendingIntent;
import android.app.WallpaperManager;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
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
    public class MyUndoListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {

            // Code to undo the user's last action
        }
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

            Photo photo = PhotoCollection.getInstance().next();
            try {
                WallpaperManager.getInstance(context).setBitmap( photo.getBitmap() );
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else if (intent.getAction().equals(PREVIOUS)) {
            Photo photo = PhotoCollection.getInstance().previous();
            try {
                WallpaperManager.getInstance(context).setBitmap( photo.getBitmap() );
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else if (intent.getAction().equals(KARMA)) {
            rViews = new RemoteViews( context.getPackageName(), R.layout.simple_widget);
            //Button button = (Button) rViews;
            Photo photo = PhotoCollection.getInstance().current();
            if (!photo.hasKarma() && photo.isKarmable()){
                photo.giveKarma();
                Toast.makeText(context, "Karma given"     , Toast.LENGTH_SHORT).show();
                rViews.setImageViewResource(R.id.heart, R.drawable.filled_heart);
            }
            else if (photo.isKarmable()){
                photo.removeKarma();
                Toast.makeText(context, "Karma taken"     , Toast.LENGTH_SHORT).show();
                rViews.setImageViewResource(R.id.heart, R.drawable.open_heart);
            }

            // TODO toggle button pressed/not

            Log.i("Testing", "This is action: " + intent.getAction());
        }
        else if (intent.getAction().equals(RELEASE)) {
            //go to next photo
            doubleClickCounter++;
           /* Handler handler = new Handler();
            Runnable r = new Runnable() {

                @Override
                public void run() {
                    doubleClickCounter = 0;
                }
            };
*/
            Photo photo = PhotoCollection.getInstance().next();
            if (photo.isReleasable() && !photo.isReleased()){
                if (doubleClickCounter == 1){
                    Toast.makeText(context, "Photo released, double tap to undo", Toast.LENGTH_SHORT).show();
                    //handler.postDelayed(r, 250000);
                    photo.release();
                    photo = PhotoCollection.getInstance().next();
                }
                else if (doubleClickCounter == 2){
                    photo = PhotoCollection.getInstance().previous();
                    photo.removeRelease();
                    Toast.makeText(context, "Photo unreleased", Toast.LENGTH_SHORT).show();
                    doubleClickCounter = 0;
                }
            }

            try {
                WallpaperManager.getInstance(context).setBitmap( photo.getBitmap() );
            } catch (IOException e) {
                e.printStackTrace();
            }
            Log.i("Testing", "This is action: " + intent.getAction());
        }
    }


}