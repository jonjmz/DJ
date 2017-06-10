package edu.ucsd.dj.models.mocks;

import edu.ucsd.dj.interfaces.models.IUser;

/**
 * Created by Jake Sutton on 6/9/17.
 */
public class MockUser implements IUser {
    @Override
    public String getName() {
        return "John Doe 6969";
    }

    @Override
    public String getEmail() {
        return "JohnDoe6969@jd.org";
    }

    @Override
    public String getUserId() {
        return "JohnDoe6969";
    }
}
