package edu.ucsd.dj.models;

import edu.ucsd.dj.interfaces.models.IUser;

/**
 * Created by Jake Sutton on 6/4/17.
 */

public class DJUser implements IUser {

    protected String name, email, userId;

    public DJUser(String name, String email) {
        this.name = name;
        this.email = email;

    }
    public DJUser(String name, String email, String userId){
        this(name, email);
        this.userId = userId;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getEmail() {
        return email;
    }

    @Override
    public String getUserId() {
        return userId;
    }
}
