package edu.ucsd.dj.interfaces;

import java.util.List;
import edu.ucsd.dj.models.Photo;

/**
 * Created by jakesutton on 6/1/17.
 */

public interface IRemotePhotoStore {
    List<Photo> getAllFriendsPhotos( IFriendList friends );
    List<Photo> getPhotos(IUser friend);
}
