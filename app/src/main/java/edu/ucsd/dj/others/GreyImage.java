package edu.ucsd.dj.others;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.support.v7.widget.AppCompatImageView;
import android.widget.GridView;
import android.widget.ImageView;

import edu.ucsd.dj.managers.Settings;

/**
 * Created by jonathanjimenez on 6/8/17.
 */

public class GreyImage extends AppCompatImageView {
    private String fileName;
    private ColorMatrixColorFilter gray;
    private ColorMatrixColorFilter normal;

    public GreyImage(Context context, String f) {
        super(context);
        fileName = f;

        Bitmap myBitmap = BitmapFactory.decodeFile(Settings.getInstance().DCIM_LOCATION + fileName);
        setImageBitmap(myBitmap);
        setLayoutParams(new GridView.LayoutParams(240, 240));

        ColorMatrix matrix = new ColorMatrix();
        matrix.setSaturation(0);  //0 means grayscale
        this.gray = new ColorMatrixColorFilter(matrix);
        matrix = new ColorMatrix();
        matrix.setSaturation(1);  //0 means grayscale
        this.normal = new ColorMatrixColorFilter(matrix);
    }

    public void makeGrey(){
        setColorFilter(gray);
        setImageAlpha(128);   // 128 = 0.5
    }

    public void makeColor(){
        setColorFilter(normal);
        setImageAlpha(254);
    }

    public String getFileName(){
        return fileName;
    }
}
