package com.ebay.myweatherapp.launch_activity;

import com.ebay.myweatherapp.models.WeatherForecast;

public interface LaunchActivityCallBack {

    //  void displayLocation(LatLng latLng);
    void showWeatherDetails(WeatherForecast weatherForecast);
}
