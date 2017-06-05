package edu.ucsd.dj.interfaces;

import java.util.List;

import edu.ucsd.dj.interfaces.models.IFriendList;
import edu.ucsd.dj.interfaces.models.IUser;
import edu.ucsd.dj.models.Photo;

/**
 * Created by Jake Sutton on 6/1/17.
 */

public interface IRemotePhotoStore {
    List<Photo> getAllFriendsPhotos( IFriendList friends );
    List<Photo> getPhotos(IUser friend);
}
