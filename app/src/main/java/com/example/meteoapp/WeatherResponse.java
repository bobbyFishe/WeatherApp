package com.example.meteoapp;
import com.google.gson.annotations.SerializedName;

public class WeatherResponse {

    @SerializedName("current")
    public Current current;

    public static class Current {
        @SerializedName("temperature_2m")
        public double temperature;

        @SerializedName("relative_humidity_2m")
        public int humidity;

        public double rain;

        @SerializedName("weather_code")
        public int weatherCode;

        @SerializedName("wind_speed_10m")
        public double windSpeed;

        @SerializedName("wind_direction_10m")
        public int windDirection;
    }
}
