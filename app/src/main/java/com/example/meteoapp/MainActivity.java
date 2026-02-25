package com.example.meteoapp;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    private TextView textView;
    private Button mos, kaz;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        textView = findViewById(R.id.text_weather);
        mos = findViewById(R.id.btn_moscow);
        kaz = findViewById(R.id.btn_kazan);


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.open-meteo.com")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        WeatherApi api = retrofit.create(WeatherApi.class);
        String fields = "temperature_2m,relative_humidity_2m,rain,weather_code,wind_speed_10m,wind_direction_10m";

        mos.setOnClickListener(view -> {
            weather(api, fields, 55.75, 37.61);
        });

        kaz.setOnClickListener(view -> {
            weather(api, fields, 55.79, 49.11);
        });

    }

    void weather(WeatherApi api, String fields, double lat, double lon) {
        api.getWeather(lat, lon, fields).enqueue(new Callback<WeatherResponse>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(Call<WeatherResponse> call, Response<WeatherResponse> response) {
                if(response.isSuccessful() && response.body() != null) {
                    WeatherResponse.Current data = response.body().current;
                    String info = "Погода: " + translateWeatherCode(data.weatherCode) + "\n" +
                            "Температура: " + data.temperature + "°C\n" +
                            "Влажность: " + data.humidity + "%\n" +
                            "Ветер: " + data.windSpeed + " км/ч (" + getWindDirection(data.windDirection) + ")\n" +
                            "Осадки: " + (data.rain > 0 ? data.rain + " мм" : "нет");
                    textView.setText(info);
                } else {
                    textView.setText("Ошибка сервера: " + response.code());
                }
            }

            @SuppressLint("SetTextI18n")
            @Override
            public void onFailure(Call<WeatherResponse> call, Throwable t) {
                textView.setText("Ошибка сети: " + t.getMessage());
            }
        });
    }

    private String translateWeatherCode(int code) {
        if (code == 0) return "Ясно ☀️";
        if (code >= 1 && code <= 3) return "Облачно ⛅";
        if (code >= 45 && code <= 48) return "Туман 🌫️";
        if (code >= 51 && code <= 65) return "Дождь 🌧️";
        if (code >= 71 && code <= 77) return "Снег ❄️";
        if (code >= 95) return "Гроза ⚡";
        return "Неизвестно";
    }

    private String getWindDirection(int degrees) {
        if (degrees >= 337.5 || degrees < 22.5) return "Северный";
        if (degrees >= 22.5 && degrees < 67.5) return "СВ";
        if (degrees >= 67.5 && degrees < 112.5) return "Восточный";
        if (degrees >= 112.5 && degrees < 157.5) return "ЮВ";
        if (degrees >= 157.5 && degrees < 202.5) return "Южный";
        if (degrees >= 202.5 && degrees < 247.5) return "ЮЗ";
        if (degrees >= 247.5 && degrees < 292.5) return "Западный";
        return "СЗ";
    }
}