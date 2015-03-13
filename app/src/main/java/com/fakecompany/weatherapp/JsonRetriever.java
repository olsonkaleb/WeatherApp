package com.fakecompany.weatherapp;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public class JsonRetriever extends AsyncTask<String, Void, String>
{
    public FrontPage callbackActivity;
    public WeatherPage linkedPage;

    protected String doInBackground(String... urls)
    {
        try
        {
            URL url = new URL(urls[0]);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            BufferedReader readIn = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String json = readIn.readLine();
            readIn.close();

            return json;
        }
        catch(IOException e)
        {
            Log.e("WeatherApp", "Connectivity error while retrieving JSON", e);
            return null;
        }
    }

    protected void onPostExecute(String json)
    {
        //if json null return error message

        try
        {
            JSONObject jObject = new JSONObject(json);
            linkedPage.info.cityName = jObject.getString("name");
            linkedPage.info.sunrise = jObject.getJSONObject("sys").getLong("sunrise");
            linkedPage.info.sunset = jObject.getJSONObject("sys").getLong("sunset");
            linkedPage.info.currentTemp = jObject.getJSONObject("main").getDouble("temp") - 273.15;
            linkedPage.info.description = jObject.getJSONArray("weather").getJSONObject(0).getString("description");
            linkedPage.info.windSpeed = jObject.getJSONObject("wind").getDouble("speed");
            linkedPage.info.windDirection = jObject.getJSONObject("wind").getDouble("deg");
        }
        catch (JSONException e)
        {
            Log.e("WeatherApp", "Error parsing JSON", e);
        }

        linkedPage.jsonRetrieved = true;

        if (linkedPage.isAdded())
            linkedPage.updatePage();

        callbackActivity.retrievedPages++;

        if (callbackActivity.retrievedPages == callbackActivity.cityIds.length)
            callbackActivity.assemblePages();
    }
}