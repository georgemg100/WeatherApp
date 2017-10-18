package com.ibm.bluelist.weatherapp.Presenter;

import android.os.Handler;
import android.os.Looper;

import com.ibm.bluelist.weatherapp.AppUtils;
import com.ibm.bluelist.weatherapp.Data.Model.Weather;
import com.ibm.bluelist.weatherapp.Data.WeatherDataSource;
import com.ibm.bluelist.weatherapp.Data.WeatherRepository;

/**
 * Created by michaelgeorgescu on 10/2/17.
 */

/**
 * This is the Presenter which is responsible for forwarding 'commands' between the View and the Model layers
 */
public class Presenter implements PresenterViewContract.Presenter {
    WeatherRepository repository;
    PresenterViewContract.View view;
    Weather weather;

    public Presenter(PresenterViewContract.View view) {
        this.view = view;
        repository = WeatherRepository.getInstance();
    }

    @Override
    public void loadWeather(String city) {
        if(AppUtils.isValidCityName(city)) {
            view.showSpinner();
            repository.fetchWeather(city, new WeatherDataSource.LoadWeatherCallback() {

                @Override
                public void onWeatherLoaded(final Weather weather) {
                    runOnUIThread(new Runnable() {
                        @Override
                        public void run() {
                            view.dismissSpinner();
                            view.showWeather(weather);
                        }
                    });

                }

                @Override
                public void onDataNotAvailable(final String message) {
                    runOnUIThread(new Runnable() {
                        @Override
                        public void run() {
                            view.showError(message);
                        }
                    });
                }
            } );
        } else {
            view.showError("Please enter a city name!");
        }
    }

    @Override
    public void loadLastSearched() {
        view.showSpinner();
        repository.loadLastSearched(new WeatherDataSource.LoadWeatherCallback() {
            @Override
            public void onWeatherLoaded(final Weather weather) {
                runOnUIThread(new Runnable() {
                    @Override
                    public void run() {
                        view.dismissSpinner();
                        view.showWeather(weather);
                    }
                });
            }

            @Override
            public void onDataNotAvailable(final String message) {
                runOnUIThread(new Runnable() {
                    @Override
                    public void run() {
                        view.showError(message);
                    }
                });

            }
        } );
    }

    public void runOnUIThread(Runnable task) {
        Handler mainHandler = new Handler(Looper.getMainLooper());
        mainHandler.post(task);
    }
}
