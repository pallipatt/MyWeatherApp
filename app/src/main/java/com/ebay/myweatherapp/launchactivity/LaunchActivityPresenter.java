package com.ebay.myweatherapp.launchactivity;

import android.app.Activity;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.ebay.myweatherapp.R;
import com.ebay.myweatherapp.models.WeatherForecast;
import com.ebay.myweatherapp.util.AppController;
import com.ebay.myweatherapp.util.NetworkConstants;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;

import org.json.JSONObject;

public class LaunchActivityPresenter extends AppCompatActivity implements
        GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks {

    private static final String TAG = LaunchActivityPresenter.class.getSimpleName();
    private GoogleApiClient mGoogleApiClient;
    private Activity mContext;

    public LaunchActivityPresenter(Activity mContext) {
        this.mContext = mContext;
        initGoogleLocationApi();
    }

    private void initGoogleLocationApi() {
        /* Create an instance of GoogleAPIClient. */
        if (mGoogleApiClient == null) {
            Log.d(TAG, "GoogleAPIClient built");
            mGoogleApiClient = new GoogleApiClient.Builder(mContext)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
    }

    /**
     * Googles Auto Complete Fragment to get Place object
     */
    public void getPlaceNameAutoPlace() {
        PlaceAutocompleteFragment autocompleteFragment = (PlaceAutocompleteFragment)
                mContext.getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);

        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                Log.i(TAG, "Place:" + place.getName());
                fetchWeatherDetails(place.getLatLng());
            }

            @Override
            public void onError(Status status) {
                Log.i(TAG, "An error occurred: " + status);
            }
        });
    }


    public void googlePlayServices() {
        mGoogleApiClient.connect();
    }

    @Override
    @SuppressWarnings("MissingPermission")
    public void onConnected(@Nullable Bundle bundle) {
        Location lastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        LatLng currentLocation = new LatLng(lastLocation.getLatitude(), lastLocation.getLongitude());
        fetchWeatherDetails(currentLocation);
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.i(TAG, i + "Connection temporarily disconnected");
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.i(TAG, "error connecting the client to the service");
    }

    /**
     * Volley Networking
     */
    public void fetchWeatherDetails(LatLng locationLatLng) {
                /* Json request object. */
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                getUriString(locationLatLng), null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());
                WeatherForecast weatherForecast = parseToWeatherInfo(response);
                ((LaunchActivityCallBack) mContext).showWeatherDetails(weatherForecast);

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, "Error: " + error.getMessage());
            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq);
    }

    private WeatherForecast parseToWeatherInfo(JSONObject response) {
        try {
            return new Gson().fromJson(response.toString(), WeatherForecast.class);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    /* This method takes a Lat and Lng and builds a url request */
    /* Example URL */
    // http://api.openweathermap.org/data/2.5/weather?lat=45.5172612&lon=-122.8448095&units=imperial&appid=6a4c44b5734d0eebf1096300a2c2dd2f
    private String getUriString(LatLng location) {

        Uri.Builder builtUrl = new Uri.Builder();
        builtUrl.scheme(NetworkConstants.SCHEME)
                .authority(NetworkConstants.BASE_URL)
                .path(NetworkConstants.PATH)
                .appendQueryParameter(NetworkConstants.LAT, String.valueOf(location.latitude))
                .appendQueryParameter(NetworkConstants.LON, String.valueOf(location.longitude))
                .appendQueryParameter(NetworkConstants.UNITS, NetworkConstants.IMPERIAL)
                .appendQueryParameter(NetworkConstants.APPID, NetworkConstants.KEY);

        /* The generated url as a string variable. */
        String url = builtUrl.build().toString();

        /* Displaying generated url for debugging purposes. */
        Log.d(TAG, "DEBUG_URL_DESCRIPTION" + url);
        return url;
    }

}
