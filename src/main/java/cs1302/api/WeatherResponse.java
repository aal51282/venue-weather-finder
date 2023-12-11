package cs1302.api;

import com.google.gson.annotations.SerializedName;

/**
 * Class to parse through the WeatherAPI.
 */

public class WeatherResponse {
    public double temp;
    public int humidity;
    @SerializedName("wind_speed")
    public double windSpeed;
    @SerializedName("cloud_pct")
    public int cloudPercentage;
} // WeatherResponse
