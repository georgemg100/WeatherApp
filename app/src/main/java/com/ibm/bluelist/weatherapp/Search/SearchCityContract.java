package com.ibm.bluelist.weatherapp.Search;

import com.ibm.bluelist.weatherapp.Data.Model.City;
import com.ibm.bluelist.weatherapp.Data.Model.Weather;

import java.util.ArrayList;

/**
 * Created by michaelgeorgescu on 10/15/17.
 */

public interface SearchCityContract {
    interface View {
        void showCities(ArrayList<City> cities);

        void showError(String message);
        void showSpinner();
        void hideSpinner();
        void hideActivity();
        void showActivity();

        void dismissSpinner();

        void dismissActivity(Weather weather);
    }

    interface Presenter {
        void loadCities(String text);
        void loadCitiesFromAssets();

        City getCityAtPosition(int index);

        void loadWeather(String city);
        }
}
