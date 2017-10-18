package com.ibm.bluelist.weatherapp.View;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.ibm.bluelist.weatherapp.Data.Model.Forecast;
import com.ibm.bluelist.weatherapp.Data.Model.Weather;
import com.ibm.bluelist.weatherapp.Presenter.Presenter;
import com.ibm.bluelist.weatherapp.Presenter.PresenterViewContract;
import com.ibm.bluelist.weatherapp.R;
import com.ibm.bluelist.weatherapp.Search.SearchCityActivity;

/**
 * Passively displays data forwarded from the presenter. Receives a Weather object from the presenter,
 * parses and displays the data. All of the implemented methods are called by the Presenter
 */
public class MainActivity extends AppCompatActivity implements PresenterViewContract.View {
    private static final int REQUEST_CODE = 1;
    Presenter presenter;
    ProgressBar progressBar;
    TextView temperature;
    TextView highTemperature;
    TextView lowTemperature;
    TextView conditions;
    TextView cityName;
    ImageView iconConditions;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //init views
        progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        temperature = (TextView) findViewById(R.id.textViewTemp);
        highTemperature = (TextView) findViewById(R.id.textViewHigh);
        lowTemperature = (TextView) findViewById(R.id.textViewLow);
        conditions = (TextView) findViewById(R.id.textViewConditions);
        iconConditions = (ImageView) findViewById(R.id.iconConditions);
        cityName = (TextView) findViewById(R.id.textViewCityName);

        presenter = new Presenter(this);
        presenter.loadLastSearched();
    }

    public void searchCity(View view) {
        startActivityForResult(new Intent(this, SearchCityActivity.class), REQUEST_CODE);
    }

    @Override
    public void showWeather(Weather weather) {
        Forecast currentForecast = weather.getForecasts().get(0); //get 1st entry from five day forecast
        temperature.setText(Integer.toString(currentForecast.getCurrentTemp()) + "˚F");
        highTemperature.setText("High: " + Integer.toString(currentForecast.getHigh()) + "˚F");
        lowTemperature.setText("Low: " + Integer.toString(currentForecast.getLow()) + "˚F");
        conditions.setText(currentForecast.getConditions());
        iconConditions.setImageBitmap(currentForecast.getIconBitmap());
        iconConditions.setVisibility(View.VISIBLE);
        cityName.setText(weather.getCity());
    }

    @Override
    public void showError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void showSpinner() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void dismissSpinner() {
        progressBar.setVisibility(View.INVISIBLE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        if (requestCode == REQUEST_CODE && data != null) {
            temperature.setText(Integer.toString(data.getExtras().getInt("currentTemp")) + "˚F");
            highTemperature.setText("High: " + Integer.toString(data.getExtras().getInt("high")) + "˚F");
            lowTemperature.setText("Low: " + Integer.toString(data.getExtras().getInt("low")) + "˚F");
            conditions.setText(data.getExtras().getString("conditions"));
            iconConditions.setImageBitmap((Bitmap) data.getExtras().get("iconBitmap"));
            iconConditions.setVisibility(View.VISIBLE);
            cityName.setText(data.getExtras().getString("city"));

        }
    }
}
