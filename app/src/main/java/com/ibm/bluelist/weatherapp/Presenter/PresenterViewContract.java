package com.ibm.bluelist.weatherapp.Presenter;

import com.ibm.bluelist.weatherapp.Data.Model.Weather;

/**
 * Created by michaelgeorgescu on 10/2/17.
 */

public interface PresenterViewContract {
    interface Presenter {
        void loadWeather(String city);
        void loadLastSearched();
        void showWeather(Weather weather);
        void showError(String message);
    }

    interface View {
        void showWeather(Weather weather);
        void showError(String message);
    }
}
