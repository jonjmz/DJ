package edu.ucsd.dj.others;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.io.File;
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
                // Using custom type of ImageView
                GreyImage image = (GreyImage) v;
                // It should now be a color image
                image.makeColor();
                // Copy the file over
                FileUtilities.copy(
                        Settings.getInstance().DCIM_LOCATION + image.getFileName(),
                        Settings.getInstance().MAIN_LOCATION + image.getFileName()
                );
                // Tell mediastore the file was created
                FileUtilities.updateMediastore(Settings.getInstance().MAIN_LOCATION + image.getFileName());
            }};

        image.setOnClickListener(listener);

        return image;
    }
    public void addPhoto(View view){

    }
}
