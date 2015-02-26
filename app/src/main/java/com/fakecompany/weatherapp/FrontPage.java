package com.fakecompany.weatherapp;

import android.net.http.AndroidHttpClient;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

//API KEY: c888e616376f2d4854883d881a0e07d4
//http://api.openweathermap.org/data/2.5/weather?id=5037649&APPID=c888e616376f2d4854883d881a0e07d4

public class FrontPage extends ActionBarActivity
{
    public WeatherInfo currentInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_front_page);

        new JsonRetriever().execute("http://api.openweathermap.org/data/2.5/weather?id=5037649&APPID=c888e616376f2d4854883d881a0e07d4");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu_front_page, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();

        if (id == R.id.action_settings)
            return true;

        return super.onOptionsItemSelected(item);
    }

    class JsonRetriever extends AsyncTask<String, Void, String>
    {
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
            updateScreen();
        }
    }

    public void parseJson(String json)
    {
        currentInfo = new WeatherInfo();

        try
        {
            JSONObject jObject = new JSONObject(json);
            currentInfo.cityName = jObject.getString("name");
            currentInfo.country = jObject.getJSONObject("sys").getString("country");
            currentInfo.sunrise = jObject.getJSONObject("sys").getLong("sunrise");
            currentInfo.sunset = jObject.getJSONObject("sys").getLong("sunrise");
            currentInfo.currentTemp = jObject.getJSONObject("main").getDouble("temp");
            currentInfo.minTemp = jObject.getJSONObject("main").getDouble("temp_min");
            currentInfo.maxTemp = jObject.getJSONObject("main").getDouble("temp_max");
            currentInfo.windSpeed = jObject.getJSONObject("wind").getDouble("speed");
            currentInfo.windDirection = jObject.getJSONObject("wind").getDouble("deg");
        }
        catch (Exception e)
        {
            Log.e("CV", "T", e);
        }
    }

    public void updateScreen()
    {
        ((TextView)findViewById(R.id.cityName)).setText(currentInfo.cityName);
    }
}