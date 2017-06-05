package edu.ucsd.dj.models;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
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
    private static final DatabaseReference
            databaseRef = FirebaseDatabase.getInstance().getReference();

    private DatabaseReference
            primaryUserRef,
            primaryUserPhotoRef;

    private static final String IMAGE_PREFIX = "images";
    private static final String USERS = "users";
    private static final String DELIMITER = "/";
    private static final String TAG = "FirebaseDB";

    private static List<Photo> friendsPhotos =  new LinkedList<>();


    @Override
    public void downloadAllFriendsPhotos(IFriendList friends) {

        for (IUser u : friends.getFriends()) {
            Log.i("FirebaseDB", "Friends: " + u.getUserId());
            downloadPhotos(u);
        }
    }

    @Override
    public void addUser(IUser user) {
        primaryUserRef = databaseRef.child(user.getUserId());
        primaryUserRef.setValue(user.getEmail());
        primaryUserPhotoRef = primaryUserRef.child("photos");
    }

    @Override
    public void removeUser(IUser user) {

    }

    @Override
    public void uploadPhotos(IUser user, List<Photo> photos) {
        int count = 0;
        for (Photo p: photos) {
            //DatabaseReference temp = primaryUserPhotoRef.child("photo" + count);
            DatabaseReference temp = primaryUserPhotoRef.push();
            temp.setValue(p);
            temp.child("uid").setValue( p.getPathname() + "@" + primaryUserRef.getKey());

            count++;

            storePhoto(user, p);
        }
    }

    @Override
    public void downloadPhotos(IUser friend) {

        Query query = databaseRef.child(friend.getUserId()).child("photos").orderByKey();

            query.addListenerForSingleValueEvent(new ValueEventListener() {

                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    for (DataSnapshot dsp : dataSnapshot.getChildren()) {
                        Photo tempPhoto = dsp.getValue(Photo.class);
                        Log.i("FirebaseDB", "Photo: " + tempPhoto.getPathname());
                        friendsPhotos.add(tempPhoto);
                    }
                }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // nothing
            }
        });
    }

    public StorageReference storePhoto(IUser user, Photo photo){

        //Get the path to upload
        StorageReference ref = buildStoragePath(user, photo.getName());
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


        return ref;
    }


    private StorageReference buildStoragePath(IUser user, String path){
        return storageRef.child(user.getUserId() + DELIMITER + path);
    }

    @Override
    public DatabaseReference getPrimaryUserPhotoRef() {
        return primaryUserPhotoRef;
    }

    @Override
    public DatabaseReference getPrimaryUserRef() {
        return primaryUserRef;
    }
}
