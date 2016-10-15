package com.ebay.myweatherapp.launchactivity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.ebay.myweatherapp.R;
import com.ebay.myweatherapp.models.WeatherForecast;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;

import butterknife.BindView;
import butterknife.ButterKnife;


public class LaunchActivity extends AppCompatActivity implements LaunchActivityCallBack{

    @BindView(R.id.textViewPlaceName) TextView mPlaceName;
    @BindView(R.id.textViewWeatherCondition) TextView mWeatherCondition;
    @BindView(R.id.textViewTemperature) TextView mTemperature;

    private static final String TAG = LaunchActivity.class.getSimpleName();
    private LaunchActivityPresenter mPresenter;

    @Override
    protected void onStart() {
        super.onStart();
        mPresenter.googlePlayServices();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch);
        ButterKnife.bind(this);
        getSupportActionBar().hide();
        mPresenter = new LaunchActivityPresenter(this);
        getPlaceNameAutoPlace();
    }

    /**
     * Googles Auto Complete Fragment to get Place object
     */
    public void getPlaceNameAutoPlace() {
        PlaceAutocompleteFragment autocompleteFragment = (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);

        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                Log.i(TAG, "Place:" + place.getName());
                mPresenter.fetchWeatherDetails(place.getLatLng());
            }

            @Override
            public void onError(Status status) {
                Log.i(TAG, "An error occurred: " + status);
            }
        });
    }

    /**
     * Updating UI with current weather
     */
    @Override
    public void showWeatherDetails(WeatherForecast weatherForecast) {
        mPlaceName.setText(weatherForecast.getName());
        mTemperature.setText(weatherForecast.getMain().getTemp());
        mWeatherCondition.setText(weatherForecast.getWeather()[0].getMain());
    }

}


