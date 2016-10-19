package com.ebay.myweatherapp.launch_activity;

import android.app.Activity;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.ebay.myweatherapp.R;
import com.ebay.myweatherapp.models.WeatherForecastIssue;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class LaunchActivityPresenter extends AppCompatActivity implements
        GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks {

    private static final String TAG = LaunchActivityPresenter.class.getSimpleName();
    private GoogleApiClient mGoogleApiClient;
    private Activity mContext;
    private static final int READ_TIMEOUT = 10000;
    private static final int CONNECTION_TIMEOUT = 10000;
    private static final int BUFFER = 1024;

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
     * Google's Auto Complete Fragment to get Place object
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
                Log.d(TAG, "An error occurred: " + status);
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
        Toast.makeText(mContext, R.string.Connection_Error, Toast.LENGTH_LONG).show();
        Log.i(TAG, i + "Connection temporarily disconnected");
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(mContext, R.string.Connection_Error, Toast.LENGTH_LONG).show();
        Log.i(TAG, "error connecting the client to the service");
    }

    /**
     * Networking
     * Json request object.
     */
    public void fetchWeatherDetails(LatLng locationLatLng) {
        new JSONParser().execute(getUriString(locationLatLng));
    }

    private WeatherForecastIssue parseToWeatherInfo(JSONObject response) {
        try {
            return new Gson().fromJson(response.toString(), WeatherForecastIssue.class);
        } catch (Exception e) {
            Log.d(TAG,e+"");
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

    /**
     * Uses AsyncTask to create a task away from the main UI thread. This task takes a
     * URL string and uses it to create an HttpUrlConnection. Once the connection
     * has been established, the AsyncTask downloads the contents
     */

    private class JSONParser extends AsyncTask<String, Void, JSONObject> {

        @Override
        protected JSONObject doInBackground(String... urls) {

            // params comes from the execute() call: params[0] is the url.
            try {
                return downloadUrl(urls[0]);
            } catch (IOException e) {
                Log.d(TAG, e + "");
                return null;
            } catch (JSONException e) {
                Log.d(TAG, e + "");
                return null;
            }
        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(JSONObject result) {
            Log.i(TAG, result + "");
            WeatherForecastIssue weatherForecast = parseToWeatherInfo(result);
            ((LaunchActivityCallBack) mContext).showWeatherDetails(weatherForecast);
        }
    }

    // Given a URL, establishes an HttpUrlConnection and retrieves JSON OBJECT
    private JSONObject downloadUrl(String currentUrl) throws IOException, JSONException {

            URL url = new URL(currentUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setReadTimeout(READ_TIMEOUT);
            connection.setConnectTimeout(CONNECTION_TIMEOUT);
            connection.setDoInput(true);

            // Starts the query
            connection.connect();
            int response = connection.getResponseCode();
            connection.getResponseMessage();
            BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));
            char[] buffer = new char[BUFFER];
            String jsonString = new String();
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line + "\n");
            }
            br.close();
            jsonString = sb.toString();

            Log.i(TAG, "The response is:" + new JSONObject(jsonString));
            return new JSONObject(jsonString);
    }
}
