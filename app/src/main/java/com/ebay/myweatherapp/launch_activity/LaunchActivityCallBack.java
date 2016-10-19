package com.ebay.myweatherapp.launch_activity;

import com.ebay.myweatherapp.models.WeatherForeCast;

public interface LaunchActivityCallBack {

    //  void displayLocation(LatLng latLng);
    void showWeatherDetails(WeatherForeCast weatherForecast);
}
