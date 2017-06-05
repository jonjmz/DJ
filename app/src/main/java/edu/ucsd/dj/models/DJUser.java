package edu.ucsd.dj.models;

import edu.ucsd.dj.interfaces.models.IUser;

/**
 * Created by Jake Sutton on 6/4/17.
 */

public class DJUser implements IUser {

    private String name, email;

    public DJUser(String name, String email) {
        this.name = name;
        this.email = email;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getEmail() {
        return email;
    }
}
