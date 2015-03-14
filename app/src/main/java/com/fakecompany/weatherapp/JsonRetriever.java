package com.fakecompany.weatherapp;

import android.os.AsyncTask;
import android.util.JsonReader;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;

public class JsonRetriever extends AsyncTask<String, Void, String>
{
    public FrontPage callbackActivity;

    protected String doInBackground(String... urls)
    {
        try
        {
            URL url = new URL(urls[0]);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(10000);
            BufferedReader readIn = new BufferedReader(new InputStreamReader(connection.getInputStream()));

            String newLine;
            StringBuilder stringBuilder = new StringBuilder();
            while ((newLine = readIn.readLine()) != null)
                stringBuilder.append(newLine);

            readIn.close();

            return stringBuilder.toString();
        }
        catch(IOException e)
        {
            Log.e("WeatherApp", "Connectivity error while retrieving JSON", e);
            return null;
        }
    }

    protected void onPostExecute(String json)
    {
        if (json == null)
        {
            callbackActivity.retryConnection();
            return;
        }

        try
        {
            JSONObject jObject = new JSONObject(json);

            callbackActivity.currentWeatherInfo.cityName = jObject.getString("name");
            callbackActivity.currentWeatherInfo.sunrise = jObject.getJSONObject("sys").getLong("sunrise");
            callbackActivity.currentWeatherInfo.sunset = jObject.getJSONObject("sys").getLong("sunset");
            callbackActivity.currentWeatherInfo.currentTemp = (jObject.getJSONObject("main").getDouble("temp") - 273.15) * 1.8 + 32;
            callbackActivity.currentWeatherInfo.description = jObject.getJSONArray("weather").getJSONObject(0).getString("description");
            callbackActivity.currentWeatherInfo.windSpeed = jObject.getJSONObject("wind").getDouble("speed");
            callbackActivity.currentWeatherInfo.windDirection = jObject.getJSONObject("wind").getDouble("deg");
        }
        catch (JSONException e)
        {
            Log.e("WeatherApp", "Error parsing JSON", e);
        }

        callbackActivity.jsonRetrieved = true;
        callbackActivity.updatePage();
    }
}