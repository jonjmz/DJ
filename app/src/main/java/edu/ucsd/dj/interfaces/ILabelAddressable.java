package edu.ucsd.dj.interfaces;

import edu.ucsd.dj.interfaces.models.IAddressable;

/**
 * Created by Jake Sutton on 5/13/17.
 * Interface for the address labeler
 */
public interface ILabelAddressable {
    String getLabel(IAddressable info);
}
