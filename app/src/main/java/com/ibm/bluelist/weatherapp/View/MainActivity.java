package com.ibm.bluelist.weatherapp.View;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.ibm.bluelist.weatherapp.Data.Model.Forecast;
import com.ibm.bluelist.weatherapp.Data.Model.Weather;
import com.ibm.bluelist.weatherapp.Presenter.Presenter;
import com.ibm.bluelist.weatherapp.Presenter.PresenterViewContract;
import com.ibm.bluelist.weatherapp.R;

/**
 * Passively displays data forwarded from the presenter. Receives a Weather object from the presenter,
 * parses and displays the data. All of the implemented methods are called by the Presenter
 */
public class MainActivity extends AppCompatActivity implements PresenterViewContract.View {
    Presenter presenter;
    ProgressBar progressBar;
    EditText editTextSearch;
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
        editTextSearch = (EditText) findViewById(R.id.editTextSearch);
        temperature = (TextView) findViewById(R.id.textViewTemp);
        highTemperature = (TextView) findViewById(R.id.textViewHigh);
        lowTemperature = (TextView) findViewById(R.id.textViewLow);
        conditions = (TextView) findViewById(R.id.textViewConditions);
        iconConditions = (ImageView) findViewById(R.id.iconConditions);
        cityName = (TextView) findViewById(R.id.textViewCityName);

        progressBar.setVisibility(View.VISIBLE);

        presenter = new Presenter(this);
        presenter.loadLastSearched();
    }

    public void searchCity(View view) {
        //dismiss keyboard
        InputMethodManager inputManager = (InputMethodManager)
                getSystemService(Context.INPUT_METHOD_SERVICE);

        inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                InputMethodManager.HIDE_NOT_ALWAYS);

        //set progress indicator and load weather
        progressBar.setVisibility(View.VISIBLE);
        presenter.loadWeather(editTextSearch.getText().toString());
    }

    @Override
    public void showWeather(Weather weather) {
        progressBar.setVisibility(View.INVISIBLE);
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
        progressBar.setVisibility(View.INVISIBLE);
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

}
