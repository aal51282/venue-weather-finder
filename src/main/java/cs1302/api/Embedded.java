package cs1302.api;

import com.google.gson.annotations.SerializedName;

/**
 * Class to parse through the TickemasterAPI.
 */

public class Embedded {

    @SerializedName("_embedded")
    public VenuesEmbedded venuesEmbedded;

} // Embedded
