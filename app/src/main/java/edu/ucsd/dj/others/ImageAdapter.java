package edu.ucsd.dj.others;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.InputType;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import edu.ucsd.dj.R;
import edu.ucsd.dj.activities.MainActivity;
import edu.ucsd.dj.activities.PhotoPicker;
import edu.ucsd.dj.managers.DJPhoto;
import edu.ucsd.dj.managers.Settings;
import edu.ucsd.dj.models.DJPrimaryUser;
import edu.ucsd.dj.models.FirebaseDB;
import edu.ucsd.dj.models.Photo;

/**
 * Created by jonathanjimenez on 6/6/17.
 * Adapter to shoehorn in our images
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
            image.setOnClickListener(new ImageClickedListener());
        }

        return image;
    }
    class ImageClickedListener implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            // Using custom type of ImageView
            final GreyImage image = (GreyImage) v;
            // It should now be a color image
            image.makeColor();
            image.setOnClickListener(null);
            // Copy the file over
            FileUtilities.copy(
                    Settings.getInstance().DCIM_LOCATION + image.getFileName(),
                    Settings.getInstance().MAIN_LOCATION + image.getFileName()
            );
            // Tell mediastore the file was created
            FileUtilities.updateMediastore(Settings.getInstance().MAIN_LOCATION + image.getFileName());

            final EditText input = new EditText(v.getContext());
            AlertDialog alertDialog = new AlertDialog.Builder(v.getContext()) //Read Update
                    .setTitle("Custom Name")
                    .setMessage("Please enter custom name or press default")
                    .setView(input)
                    .setPositiveButton("Custom",new DialogCustomResponseListener(image.getFileName(), input))
                    .setNegativeButton("Default", new DialogDefaultResponseListener(image.getFileName()))
                    .create();
            alertDialog.show();
        }
        class DialogCustomResponseListener implements DialogInterface.OnClickListener{
            private String fileName;
            private EditText input;
            DialogCustomResponseListener(String file, EditText in){
                fileName = file;
                input = in;
            }
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Create a photo and user
                DJPrimaryUser primaryUser = new DJPrimaryUser();
                Photo tempPhoto = new Photo(fileName, primaryUser);
                tempPhoto.setPathname(Settings.getInstance().MAIN_LOCATION + fileName);
                tempPhoto.setCustomLocation(input.getText().toString());
                PhotoCollection.getInstance().addPhoto(tempPhoto);
                FirebaseDB.getInstance().uploadPhotos(primaryUser, Arrays.asList(tempPhoto));
            }
        }
        class DialogDefaultResponseListener implements DialogInterface.OnClickListener{
            private String fileName;
            DialogDefaultResponseListener(String file){
                fileName = file;
            }
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Create a photo and user
                DJPrimaryUser primaryUser = new DJPrimaryUser();
                Photo tempPhoto = new Photo(fileName, primaryUser);
                tempPhoto.setPathname(Settings.getInstance().MAIN_LOCATION + fileName);
                PhotoCollection.getInstance().addPhoto(tempPhoto);
                if(Settings.getInstance().isSharePhotos())
                    FirebaseDB.getInstance().uploadPhotos(primaryUser, Arrays.asList(tempPhoto));
            }
        }
    }
}
