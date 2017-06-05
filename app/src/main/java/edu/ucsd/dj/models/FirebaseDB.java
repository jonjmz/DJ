package edu.ucsd.dj.models;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.LinkedList;
import java.util.List;

import edu.ucsd.dj.interfaces.models.IAddressable;
import edu.ucsd.dj.interfaces.models.IFriendList;
import edu.ucsd.dj.interfaces.IRemotePhotoStore;
import edu.ucsd.dj.interfaces.models.IUser;

/**
 * Created by nguyen on 6/4/2017.
 */
public class FirebaseDB implements IRemotePhotoStore {

    private static final StorageReference storageRef =
            FirebaseStorage.getInstance().getReference();
    private static final DatabaseReference databaseRef =
            FirebaseDatabase.getInstance().getReference();

    private static final String IMAGE_PREFIX = "images";
    private static final String USERS = "users";
    private static final String DELIMITER = "/";
    private static final String TAG = "FirebaseDB";

    @Override
    public List<Photo> getAllFriendsPhotos(IFriendList friends) {
        List<Photo> result = new LinkedList<>();

        // TODO this is going to be slow as shit
        for (IUser u : friends.getFriends()) {
             result.addAll(getPhotos(u));
        }

        return result;
    }

    @Override
    public List<Photo> getPhotos(IUser friend) {

        // TODO implement me

        return null;
    }

    public void uploadPhoto(IUser user, Photo photo){

        //Get the path to upload
        StorageReference ref = buildStoragePath(user, photo.getPathname());
        // Get the data from an ImageView as bytes
        Bitmap bitmap = BitmapFactory.decodeFile(photo.getPathname());
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        UploadTask uploadTask = ref.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
                Log.w(TAG, "Upload: onFailure", exception);

            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // Upload succeeded
                Log.d(TAG, "Upload: onSuccess");
                // Handle successful uploads
            }
        });

        //uploadMetadata(user, photo);
    }
    private StorageReference buildStoragePath(IUser user, String path){
        return storageRef.child(path);
    }
    private DatabaseReference buildMetaPath(IUser user, String path){
        return databaseRef.child(user.getEmail());
    }
    public void uploadMetadata(IUser user, Photo photo, IAddressable address){
        buildMetaPath(user, photo.getPathname()).setValue(address);
    }
}
