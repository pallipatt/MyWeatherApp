package com.ebay.myweatherapp.launch_activity;

import com.ebay.myweatherapp.models.WeatherForeCastTOcheck;

public interface LaunchActivityCallBack {

    //  void displayLocation(LatLng latLng);
    void showWeatherDetails(WeatherForeCastTOcheck weatherForecast);
}
