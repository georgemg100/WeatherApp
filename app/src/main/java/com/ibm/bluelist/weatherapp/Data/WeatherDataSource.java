package com.ibm.bluelist.weatherapp.Data;

import com.ibm.bluelist.weatherapp.Data.Model.Weather;

import java.util.List;

/**
 * Created by michaelgeorgescu on 10/17/17.
 */

public interface WeatherDataSource  {
    interface LoadWeatherCallback {

        void onWeatherLoaded(Weather weather);

        void onDataNotAvailable(String message);
    }
}
