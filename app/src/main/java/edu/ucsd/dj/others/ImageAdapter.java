package edu.ucsd.dj.others;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import java.io.File;
import java.util.List;

import edu.ucsd.dj.models.Photo;

/**
 * Created by jonathanjimenez on 6/6/17.
 */

public class ImageAdapter extends BaseAdapter {
    private Context context;
    List<Photo> photos;
    ColorMatrixColorFilter gray;
    ColorMatrixColorFilter normal;

    public ImageAdapter(Context c, List<Photo> p){
        this.context = c;
        this.photos = p;
        ColorMatrix matrix = new ColorMatrix();
        matrix.setSaturation(0);  //0 means grayscale
        this.gray = new ColorMatrixColorFilter(matrix);
        matrix = new ColorMatrix();
        matrix.setSaturation(1);  //0 means grayscale
        this.normal = new ColorMatrixColorFilter(matrix);
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
        File imgFile = new  File(photos.get(position).getPathname());
        Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
        ImageView imageView = new ImageView(context);
        imageView.setImageBitmap(myBitmap);
        imageView.setLayoutParams(new GridView.LayoutParams(240, 240));

        if(PhotoCollection.getInstance().getAlbum().contains(photos.get(position))){
            imageView.setEnabled(false);
        } else {
            imageView.setColorFilter(gray);
            imageView.setImageAlpha(128);   // 128 = 0.5

            View.OnClickListener listener  = new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    ImageView image = (ImageView) v;
                    image.setImageAlpha(254);
                    image.setColorFilter(normal);
                    // call some method that moves file into album
                }
            };
            imageView.setOnClickListener(listener);
        }
        return imageView;
    }

    public void addPhoto(View view){

    }
}
