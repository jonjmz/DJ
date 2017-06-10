package edu.ucsd.dj.models;

import android.content.ContentResolver;
import android.database.Cursor;
import android.provider.ContactsContract;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

import edu.ucsd.dj.interfaces.models.IFriendList;
import edu.ucsd.dj.interfaces.models.IUser;
import edu.ucsd.dj.managers.DJPhoto;

/**
 * Created by Jake Sutton on 6/4/17.
 * To hold list of friends
 */

public class DJFriends implements IFriendList {

    @Override
    public List<IUser> getFriends() {

        List<IUser> emlRecs = new LinkedList<>();
        HashSet<String> emlRecsHS = new HashSet<String>();
        ContentResolver cr = DJPhoto.getAppContext().getContentResolver();

        String[] PROJECTION = new String[] { ContactsContract.RawContacts._ID,
                ContactsContract.Contacts.DISPLAY_NAME,
                ContactsContract.Contacts.PHOTO_ID,
                ContactsContract.CommonDataKinds.Email.DATA,
                ContactsContract.CommonDataKinds.Photo.CONTACT_ID };
        String order = "CASE WHEN "
                + ContactsContract.Contacts.DISPLAY_NAME
                + " NOT LIKE '%@%' THEN 1 ELSE 2 END, "
                + ContactsContract.Contacts.DISPLAY_NAME
                + ", "
                + ContactsContract.CommonDataKinds.Email.DATA
                + " COLLATE NOCASE";

        String filter = ContactsContract.CommonDataKinds.Email.DATA + " NOT LIKE ''";
        Cursor cur = cr.query(ContactsContract.CommonDataKinds.Email.CONTENT_URI, PROJECTION,
                filter, null, order);
        if (cur.moveToFirst()) {
            do {
                // names comes in hand sometimes
                String name = cur.getString(1);
                String email = cur.getString(3);
                String userId = email.substring(0, email.indexOf("@"));

                // keep unique only
                if (emlRecsHS.add(email.toLowerCase())) {
                    emlRecs.add( new DJUser(name, email, userId) );
                }
            } while (cur.moveToNext());
        }

        cur.close();
        return emlRecs;
    }
}
