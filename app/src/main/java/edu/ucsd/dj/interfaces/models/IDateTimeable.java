package edu.ucsd.dj.interfaces.models;

/**
 * Created by jonathanjimenez on 5/14/17.
 * Interface for date/time picture taken
 */

public interface IDateTimeable {
    boolean hasValidDate();
    void setHasValidDate(boolean hvc);

    long getDateTime();
    void setDateTime(long dateTime);
}
