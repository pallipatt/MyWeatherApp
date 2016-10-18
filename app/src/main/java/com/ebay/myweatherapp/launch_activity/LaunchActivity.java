package com.ebay.myweatherapp.launch_activity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;
import com.ebay.myweatherapp.R;
import com.ebay.myweatherapp.models.WeatherForecast;

public class LaunchActivity extends Activity implements LaunchActivityCallBack {

    private TextView mPlaceName;
    private TextView mWeatherCondition;
    private TextView mTemperature;

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
        initVariables();
        mPresenter = new LaunchActivityPresenter(this);
        mPresenter.getPlaceNameAutoPlace();
    }

    private void initVariables() {
        mPlaceName = (TextView) findViewById(R.id.textViewPlaceName);
        mWeatherCondition = (TextView) findViewById(R.id.textViewWeatherCondition);
        mTemperature = (TextView) findViewById(R.id.textViewTemperature);
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


