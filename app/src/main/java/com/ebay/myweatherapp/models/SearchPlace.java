package com.ebay.myweatherapp.models;

import com.google.android.gms.location.places.Place;
import com.google.android.gms.maps.model.LatLng;

public class SearchPlace {

    private String placeId;
    private String placeName;
    private LatLng latLng;
   // private double longitude;

    public SearchPlace() {
        super();
    }

    public SearchPlace(Place place) {
        this();
        this.placeId = place.getId();
        this.placeName = place.getName().toString();
        this.latLng = place.getLatLng();
        //this.longitude = place.getLatLng().longitude;
    }

    public String getPlaceName() {
        return placeName;
    }

    public void setPlaceName(String placeName) {
        this.placeName = placeName;
    }

    public String getPlaceId() {
        return placeId;
    }

    public void setPlaceId(String placeId) {
        this.placeId = placeId;
    }

    public LatLng getLatLng() {
        return latLng;
    }

    public void setLatLng(LatLng latLng) {
        this.latLng = latLng;
    }

}
