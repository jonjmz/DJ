package edu.ucsd.dj.managers;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Josh on 6/7/2017.
 */

public class Camera extends AppCompatActivity{
    private static final int REQUEST_IMAGE_CAPTURE = 2;
}
