package com.fakecompany.weatherapp;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class FrontPage extends Activity
{
    private String MINNEAPOLIS_URL = "http://api.openweathermap.org/data/2.5/weather?id=5037649&APPID=4aec2bbf4928ecf73243f827357d7799";

    public WeatherInfo currentWeatherInfo;
    public DrawSpace drawSpace;
    public boolean jsonRetrieved;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_front_page);

        setVisibility(View.INVISIBLE);

        currentWeatherInfo = new WeatherInfo();
        drawSpace = (DrawSpace) findViewById(R.id.drawSpace);
        drawSpace.parentActivity = this;
        jsonRetrieved = false;

        JsonRetriever retriever = new JsonRetriever();
        retriever.callbackActivity = this;
        retriever.execute(MINNEAPOLIS_URL);
    }

    public void updatePage()
    {
        Date currentTime = new Date();
        Date sunriseTime = new Date(currentWeatherInfo.sunrise * 1000);
        Date sunsetTime = new Date(currentWeatherInfo.sunset * 1000);
        SimpleDateFormat timeFormat = new SimpleDateFormat("h:mm", Locale.US);

        ((TextView) findViewById(R.id.txtCityName)).setText(currentWeatherInfo.cityName);

        ((TextView) findViewById(R.id.txtCurrentTemp)).setText(String.valueOf(Math.round(currentWeatherInfo.currentTemp)) + "\u00b0");
        ((TextView) findViewById(R.id.txtWeatherDescription)).setText(currentWeatherInfo.description.substring(0, 1).toUpperCase() + currentWeatherInfo.description.substring(1));

        ((TextView) findViewById(R.id.txtWindSpeed)).setText(getWindDescription());

        if (currentTime.after(sunriseTime) && currentTime.before(sunsetTime))
        {
            ((TextView) findViewById(R.id.txtSunrise)).setText("Sunrise\n" + timeFormat.format(sunriseTime));
            ((TextView) findViewById(R.id.txtSunset)).setText("Sunset\n" + timeFormat.format(sunsetTime));
        }
        else
        {
            ((TextView) findViewById(R.id.txtSunrise)).setText("Sunset\n" + timeFormat.format(sunsetTime));
            ((TextView) findViewById(R.id.txtSunset)).setText("Sunrise\n" + timeFormat.format(sunriseTime));
        }

        drawSpace.createArc();
        setVisibility(View.VISIBLE);
        findViewById(R.id.pbarLoadingDisplay).setVisibility(View.INVISIBLE);
        findViewById(R.id.txtLoadingText).setVisibility(View.INVISIBLE);
    }

    public void retryConnection()
    {
        ((TextView)findViewById(R.id.txtLoadingText)).setText("Connection failed, retrying...");

        JsonRetriever retriever = new JsonRetriever();
        retriever.callbackActivity = this;
        retriever.execute(MINNEAPOLIS_URL);
    }

    public String getWindDescription()
    {
        StringBuilder builder = new StringBuilder();

        if (currentWeatherInfo.windSpeed > 10)
            builder.append("Heavy wind\n");
        else if (currentWeatherInfo.windSpeed > 5)
            builder.append("Moderate wind\n");
        else if (currentWeatherInfo.windSpeed > 1)
            builder.append("Light wind\n");
        else
            builder.append("Very light wind\n");

        if (currentWeatherInfo.windDirection > 338 || currentWeatherInfo.windDirection < 23)
            builder.append("Blowing East");
        else if (currentWeatherInfo.windDirection > 293)
            builder.append("Blowing Northeast");
        else if (currentWeatherInfo.windDirection > 248)
            builder.append("Blowing North");
        else if (currentWeatherInfo.windDirection > 203)
            builder.append("Blowing Northwest");
        else if (currentWeatherInfo.windDirection > 158)
            builder.append("Blowing West");
        else if (currentWeatherInfo.windDirection > 113)
            builder.append("Blowing Southwest");
        else if (currentWeatherInfo.windDirection > 68)
            builder.append("Blowing South");
        else if (currentWeatherInfo.windDirection >= 23)
            builder.append("Blowing Southeast");

        return builder.toString();
    }

    public void setVisibility(int visibility)
    {
        findViewById(R.id.txtCityName).setVisibility(visibility);
        findViewById(R.id.txtCurrentTemp).setVisibility(visibility);
        findViewById(R.id.txtWeatherDescription).setVisibility(visibility);
        findViewById(R.id.txtWindSpeed).setVisibility(visibility);
        findViewById(R.id.txtclockCurrentTime).setVisibility(visibility);
        findViewById(R.id.txtSunrise).setVisibility(visibility);
        findViewById(R.id.txtSunset).setVisibility(visibility);
        findViewById(R.id.viewSpacer1).setVisibility(visibility);
        findViewById(R.id.viewSpacer2).setVisibility(visibility);
    }
}