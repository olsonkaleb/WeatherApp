package com.fakecompany.weatherapp;

import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class JsonRetriever extends AsyncTask<String, Void, String>
{
    public WeatherPage linkedPage;

    protected String doInBackground(String... urls)
    {
        try
        {
            URL url = new URL(urls[0]);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String json = in.readLine();
            in.close();
            return json;
        }
        catch(Exception e)
        {
            Log.e("ASD", "Error retrieving JSON", e);
            return null;
        }
    }

    protected void onPostExecute(String json)
    {
        parseJson(json);
        linkedPage.updatePage();
    }

    public void parseJson(String json)
    {
        try
        {
            JSONObject jObject = new JSONObject(json);
            linkedPage.info.cityName = jObject.getString("name");
            linkedPage.info.country = jObject.getJSONObject("sys").getString("country");
            linkedPage.info.sunrise = jObject.getJSONObject("sys").getLong("sunrise");
            linkedPage.info.sunset = jObject.getJSONObject("sys").getLong("sunrise");
            linkedPage.info.currentTemp = jObject.getJSONObject("main").getDouble("temp");
            linkedPage.info.minTemp = jObject.getJSONObject("main").getDouble("temp_min");
            linkedPage.info.maxTemp = jObject.getJSONObject("main").getDouble("temp_max");
            linkedPage.info.windSpeed = jObject.getJSONObject("wind").getDouble("speed");
            linkedPage.info.windDirection = jObject.getJSONObject("wind").getDouble("deg");
        }
        catch (Exception e)
        {
            Log.e("CV", "T", e);
        }
    }
}