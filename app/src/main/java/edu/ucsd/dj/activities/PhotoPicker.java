package edu.ucsd.dj.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.GridView;

import java.util.ArrayList;
import java.util.List;

import edu.ucsd.dj.R;
import edu.ucsd.dj.models.Photo;
import edu.ucsd.dj.others.ImageAdapter;
import edu.ucsd.dj.others.PhotoLoader;

public class PhotoPicker extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_picker);


        PhotoLoader loader = new PhotoLoader("DCIM");
        List<Photo> newAlbum = loader.getPhotos();

        GridView gridView = (GridView) findViewById(R.id.grid);
        gridView.setAdapter(new ImageAdapter(this, newAlbum));
    }
}
