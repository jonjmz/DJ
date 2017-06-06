package edu.ucsd.dj.interfaces.models;

/**
 * Created by jonathanjimenez on 5/14/17.
 */

public interface IDateTimeable {
    boolean hasValidDate();
    void setHasValidDate(boolean hvc);

    long getDateTime();
    void setDateTime(long dateTime);
}
