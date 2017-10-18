package com.ibm.bluelist.weatherapp.Search;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.ibm.bluelist.weatherapp.Data.Model.City;
import com.ibm.bluelist.weatherapp.Data.Model.Weather;
import com.ibm.bluelist.weatherapp.R;

import java.util.ArrayList;

public class SearchCityActivity extends AppCompatActivity implements SearchCityContract.View {

    ListView listView;
    EditText editTextSearchCity;
    SearchCityPresenter presenter;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_city);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        presenter = new SearchCityPresenter(this);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        listView = (ListView) findViewById(R.id.listViewCities);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                presenter.loadWeather(presenter.getCityAtPosition(position).getName());
            }
        });

        editTextSearchCity = (EditText) findViewById(R.id.editTextCity);
        presenter.loadCitiesFromAssets();

        editTextSearchCity.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                presenter.loadCities(s.toString());

            }
        });
    }

    @Override
    public void showCities(ArrayList<City> cities) {
        ArrayList<String> cityCountryTextList = new ArrayList<>();
        for(City city : cities) {
            cityCountryTextList.add(city.getName() + " " + city.getCountry());
        }
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
        arrayAdapter.addAll(cityCountryTextList);
        listView.setAdapter(arrayAdapter);
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
    public void hideSpinner() {
        progressBar.setVisibility(View.INVISIBLE);
    }

    @Override
    public void hideActivity() {
        listView.setVisibility(View.INVISIBLE);
        editTextSearchCity.setVisibility(View.INVISIBLE);
    }

    @Override
    public void showActivity() {
        listView.setVisibility(View.VISIBLE);
        editTextSearchCity.setVisibility(View.VISIBLE);
    }

    @Override
    public void dismissSpinner() {
        progressBar.setVisibility(View.INVISIBLE);
    }

    @Override
    public void dismissActivity(Weather weather) {
        Intent data = new Intent();
        data.putExtra("city", weather.getCity());
        data.putExtra("cityId", weather.getCityId());
        data.putExtra("requestTime", weather.getRequestTime());
        data.putExtra("conditions", weather.getForecasts().get(0).getConditions());
        data.putExtra("currentTemp", weather.getForecasts().get(0).getCurrentTemp());
        data.putExtra("high", weather.getForecasts().get(0).getHigh());
        data.putExtra("low", weather.getForecasts().get(0).getLow());
        data.putExtra("iconBitmap", weather.getForecasts().get(0).getIconBitmap());
        data.putExtra("iconUrl", weather.getForecasts().get(0).getIconUrl());
        setResult(1, data);
        this.finish();
    }
}
