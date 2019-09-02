package com.wordpress.packbackcf.khromtauweather;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpRequest;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class MainActivity extends AppCompatActivity {
    final String API_URL = "http://api.openweathermap.org/data/2.5/weather";
    final String APP_ID = "adc2efdc7e4580e17df91753b5c37378";
    final String city = "Khromtau";
    final String metric = "metric";
    private String degreeString;
    double roundedTempResult;
    // linking vars
    TextView degreeText;
    ImageView iconOfWeather;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        degreeText = (TextView) findViewById(R.id.degrees);
        iconOfWeather = (ImageView) findViewById(R.id.weather_icons);

        RequestParams params = new RequestParams();
        params.put("q", city);
        params.put("units", metric);
        params.put("appid", APP_ID);
        netWorking(params);
    }

    private void netWorking(RequestParams params) {
        AsyncHttpClient client = new AsyncHttpClient();

        client.get(API_URL, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    roundedTempResult = Math.rint(response.getJSONObject("main").getDouble("temp"));
                    degreeString = Integer.toString((int) roundedTempResult);
                    degreeText.setText(degreeString + "°C");
                    String desiredIconName = response.getJSONArray("weather").getJSONObject(0).getString("icon");
                    int resourceID = getResources().getIdentifier("d" + desiredIconName.substring(0, desiredIconName.length()-1), "drawable", getPackageName());
                    iconOfWeather.setImageResource(resourceID);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.e("Lol", "FUCK!: " + throwable.toString());
                Log.e("Lol", "onFailure: " + statusCode);
            }
        });

        // for determination of icons

    }
}
