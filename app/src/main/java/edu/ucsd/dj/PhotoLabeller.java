package edu.ucsd.dj;

/**
 * Created by jakesutton on 5/9/17.
 */
public class PhotoLabeller {

    /**
     * TODO
     *
     * @param photo
     * @return Written description of the location of this image.
     */
    public String label(Photo photo) {

        String result = "";

        if (photo.hasValidCoordinates()) {
            // TODO implement me.
        } else {
            result = "Location Unknown";
        }

        return result;
    }
}
