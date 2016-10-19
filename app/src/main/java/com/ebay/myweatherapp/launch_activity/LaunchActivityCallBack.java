package com.ebay.myweatherapp.launch_activity;

import com.ebay.myweatherapp.models.WeatherForecastIssue;

public interface LaunchActivityCallBack {

    //  void displayLocation(LatLng latLng);
    void showWeatherDetails(WeatherForecastIssue weatherForecast);
}
