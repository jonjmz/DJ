package edu.ucsd.dj.interfaces;

import com.google.firebase.database.DatabaseReference;

import java.util.List;

import edu.ucsd.dj.interfaces.models.IFriendList;
import edu.ucsd.dj.interfaces.models.IUser;
import edu.ucsd.dj.models.Photo;

/**
 * Created by Jake Sutton on 6/1/17.
 */

public interface IRemotePhotoStore {
    void addUser(IUser user);
    void removeUser(IUser user);

    void uploadPhotos( IUser user, List<Photo> photos );

    DatabaseReference getPrimaryUserPhotoRef();
    DatabaseReference getPrimaryUserRef();

    void downloadPhotos(IUser friend);
    void downloadAllFriendsPhotos(IFriendList friends );
}
