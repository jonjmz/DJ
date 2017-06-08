package edu.ucsd.dj.interfaces;

import android.graphics.Bitmap;

import edu.ucsd.dj.interfaces.models.IPhoto;

/**
 * Created by Jake Sutton on 6/8/17.
 */

public interface ILabelKarma {
    Bitmap createBitmapWithKarmaLabel(Bitmap bm, IPhoto photo);
}
