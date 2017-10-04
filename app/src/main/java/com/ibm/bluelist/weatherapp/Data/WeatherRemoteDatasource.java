package com.ibm.bluelist.weatherapp.Data;

import android.graphics.Bitmap;

import com.ibm.bluelist.weatherapp.Data.Model.Forecast;
import com.ibm.bluelist.weatherapp.View.MainApplication;
import com.ibm.bluelist.weatherapp.Data.Model.Weather;
import com.squareup.picasso.Picasso;

import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * Created by michaelgeorgescu on 10/2/17.
 */

/**
 * Fetches data from the services. Pulls in 5 day forecasts
 * and uses first forecast item to display current weather
 */
public class WeatherRemoteDatasource {
    OkHttpClient client = new OkHttpClient();
    public static final String API_KEY = "fa0034eeccb77ad403ed51eea01b24f0";
    public static final String URL_CURRENT_WEATHER = "http://api.openweathermap.org/data/2.5/forecast";
    public static final String URL_ICON_CONDITIONS = "http://openweathermap.org/img/w/";
    public static final String FILE_TYPE_PNG = ".png";

    public void fetchWeather(String param, Callback callback) {
        Request request = new Request.Builder()
                .url(URL_CURRENT_WEATHER + "?q=" + param + "&appid=" + API_KEY)
                .build();
        client.newCall(request).enqueue(callback);
    }

    public void downloadConditionIconsForForecasts(Weather weather) throws Exception {
        for(Forecast forecast : weather.getForecasts()) {
            Bitmap bm = Picasso.with(MainApplication.context).load(URL_ICON_CONDITIONS + forecast.getIconUrl() + FILE_TYPE_PNG).get();
            forecast.setIconBitmap(bm);
        }

    }

}
