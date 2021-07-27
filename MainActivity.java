package com.example.weatherappattempt;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class MainActivity extends AppCompatActivity {

    final String APP_ID = "c0ead9243bf724091892d569000466f6"; //API Key
    //final String URL = "https://api.openweathermap.org/data/2.5/weather";
    final String URL = "api.openweathermap.org/data/2.5/weather?q={city name}&appid={API key}";

    final long MIN_TIME = 5000; //5 seconds
    final float MIN_DISTANCE = 1000; //1 metre

    final int REQUEST_CODE = 101;

    String Location_Provider = LocationManager.GPS_PROVIDER;

    TextView cityName, weatherState, temperature;
    ImageView weatherIcon;
    RelativeLayout rlCityFinder;

    LocationManager locationManager;
    LocationListener locationListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        weatherState = findViewById(R.id.weatherCondition);
        temperature = findViewById(R.id.temperature);
        weatherIcon = findViewById(R.id.weatherIcon);
        cityName = findViewById(R.id.cityName);
        rlCityFinder = findViewById(R.id.cityFinder);

        rlCityFinder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, cityFinder.class);
                startActivity(intent);
            }
        });

    }

    //when the app opens run code, call weather for location so opens current location weather

   /* @Override
    protected void onResume() {
        super.onResume();
        getWeatherForCurrentLocation();
    }*/

    @Override
    protected void onResume() {
        super.onResume();

        Intent i = getIntent();
        String city = i.getStringExtra("city");

        //find weather of he city

        if (city!=null) {
            getWeatherForNewCity(city);
        }
        else{
            getWeatherForCurrentLocation();
        }
    }
    private void getWeatherForNewCity(String city){
        RequestParams params = new RequestParams();
        params.put("q", city);
        params.put("appid",APP_ID);
        letsdoSomeNetworking(params);
    }

    private void getWeatherForCurrentLocation() {
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {

            @Override //if the locations change
            public void onLocationChanged(@NonNull Location location) {
                //Get the latitude and longitude of the user

                String Latitude = String.valueOf(location.getLatitude());
                String Longitude = String.valueOf(location.getLongitude());

                //pass the lat and lon to the api with the api key and then fetch the weather data

                RequestParams params = new RequestParams();
                params.put("lat",Latitude);
                params.put("lon", Longitude);
                params.put("appid", APP_ID);

                //letsdoSomeNetworking();
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {


            }

            @Override //Enabled location
            public void onProviderEnabled(@NonNull String provider) {

            }

            @Override //if users disable location or don't allow location services
            public void onProviderDisabled(@NonNull String provider) {

                //Toast.makeText()

            }
        };

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},REQUEST_CODE);
            return;
        }
        // Time and distance for the weather to change and check
        locationManager.requestLocationUpdates(Location_Provider, MIN_TIME, MIN_DISTANCE, locationListener);
    }


    @Override //Check if th user allows permission or not
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == REQUEST_CODE){

            if(grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Toast.makeText(MainActivity.this, "Location got Successfully", Toast.LENGTH_SHORT).show();
                getWeatherForCurrentLocation();

            }
            else{
                //user has denied the permissions

            }

        }
    }

    private void letsdoSomeNetworking(RequestParams params ){

        AsyncHttpClient client = new AsyncHttpClient();
        client.get(URL,params,new JsonHttpResponseHandler(){

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                //super.onSuccess(statusCode, headers, response);

                Toast.makeText(MainActivity.this,"Data Success",Toast.LENGTH_LONG).show();
                weatherData weatherD = weatherData.fromJson(response);

                //call update Ui function
                updateUI(weatherD);


            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                //super.onFailure(statusCode, headers, throwable, errorResponse);
            }

        });

    }

    private void updateUI(weatherData weatherData){

        temperature.setText(weatherData.getTemperature());
        weatherState.setText(weatherData.getWeatherType());
        cityName.setText(weatherData.getCity());
        int resourceID = getResources().getIdentifier(weatherData.getIcon(), "drawable", getPackageName());
        weatherIcon.setImageResource(resourceID);

    }

    @Override
    protected void onPause() {
        super.onPause();
        if(locationManager!= null){
            locationManager.removeUpdates(locationListener);

        }
    }
}