package edu.ucsd.dj.others;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import edu.ucsd.dj.managers.Settings;
import edu.ucsd.dj.models.Photo;

/**
 * Created by jonathanjimenez on 6/6/17.
 */

public class ImageAdapter extends BaseAdapter {
    private Context context;
    List<Photo> photos;

    public ImageAdapter(Context c, List<Photo> p){
        this.context = c;
        this.photos = p;
    }
    @Override
    public int getCount() {
        return photos.size();
    }

    @Override
    public Object getItem(int position) {
        return photos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        GreyImage image = new GreyImage(context, new File(photos.get(position).getPathname()).getName());

        if (!PhotoCollection.getInstance().getAlbum().contains(photos.get(position))) {
            image.makeGrey();
        }

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GreyImage image = (GreyImage) v;
                image.makeColor();

                // call some method that moves file into album
                File from = new File(Settings.getInstance().DCIM_LOCATION + image.getFileName());
                File to = new File(Settings.getInstance().MAIN_LOCATION + image.getFileName());

                InputStream is = null;
                OutputStream os = null;
                try {
                    is = new FileInputStream(from);
                    os = new FileOutputStream(to);
                    byte[] buffer = new byte[1024];
                    int length;
                    while ((length = is.read(buffer)) > 0) {
                        os.write(buffer, 0, length);
                    }
                    is.close();
                    os.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                // Update MediaStore
                Intent intent =
                        new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                intent.setData(Uri.fromFile(to));
                context.sendBroadcast(intent);
            }};

        image.setOnClickListener(listener);

        return image;
    }
    public void addPhoto(View view){

    }
}
