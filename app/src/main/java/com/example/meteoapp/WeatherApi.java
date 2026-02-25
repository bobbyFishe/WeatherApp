package com.example.meteoapp;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface WeatherApi {
    @GET("v1/forecast")
    Call<WeatherResponse> getWeather(
            @Query("latitude") double lat,
            @Query("longitude") double lon,
            // Мы просим сервер прислать именно эти поля в объекте 'current'
            @Query("current") String currentFields
    );
}
