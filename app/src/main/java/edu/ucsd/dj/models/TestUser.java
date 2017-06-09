package edu.ucsd.dj.models;

import edu.ucsd.dj.interfaces.models.IUser;

/**
 * Created by nguyen on 6/4/2017.
 * Class for unit testing of IUser
 */

public class TestUser implements IUser {
    @Override
    public String getName() {
        return "testing name";
    }

    @Override
    public String getEmail() {
        return "testing LMAO 420 lit";
    }

    @Override
    public String getUserId() {
        return "testing LMAO 420 lit";
    }
}
