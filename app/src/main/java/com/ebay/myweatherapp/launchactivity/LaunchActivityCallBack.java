package com.ebay.myweatherapp.launchactivity;

import com.ebay.myweatherapp.models.WeatherForecast;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.maps.model.LatLng;

public interface LaunchActivityCallBack {

  //  void displayLocation(LatLng latLng);
    void showWeatherDetails(WeatherForecast weatherForecast);
}
