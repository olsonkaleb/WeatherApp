package com.fakecompany.weather;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.fakecompany.weatherapp.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class FrontPage extends Activity
{
    //API link using the city ID for Minneapolis
    private String MINNEAPOLIS_URL = "http://api.openweathermap.org/data/2.5/weather?id=5037649&APPID=4aec2bbf4928ecf73243f827357d7799";

    public WeatherInfo currentWeatherInfo; //WeatherInfo object that holds the data retrieved from the API
    public DrawSpace drawSpace; //Custom view for drawing simple graphics

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_front_page);

        //Hide text fields while loading screen is being shown
        setVisibility(View.INVISIBLE);

        currentWeatherInfo = new WeatherInfo();

        //Get DrawSpace in layout and set parent
        drawSpace = (DrawSpace) findViewById(R.id.drawSpace);
        drawSpace.parentActivity = this;

        //Start API call using the AsyncTask JsonRetriever
        JsonRetriever retriever = new JsonRetriever();
        retriever.callbackActivity = this;
        retriever.execute(MINNEAPOLIS_URL);
    }

    //Displays the info currently held in currentWeatherInfo
    public void showWeatherInfo()
    {
        Date currentTime = new Date();
        Date sunriseTime = new Date(currentWeatherInfo.sunrise * 1000);
        Date sunsetTime = new Date(currentWeatherInfo.sunset * 1000);
        SimpleDateFormat timeFormat = new SimpleDateFormat("h:mm", Locale.US);

        ((TextView) findViewById(R.id.txtCityName)).setText(currentWeatherInfo.cityName);

        ((TextView) findViewById(R.id.txtCurrentTemp)).setText(String.valueOf(Math.round(currentWeatherInfo.currentTemp)) + "\u00b0");
        ((TextView) findViewById(R.id.txtWeatherDescription)).setText(currentWeatherInfo.description.substring(0, 1).toUpperCase() + currentWeatherInfo.description.substring(1));

        ((TextView) findViewById(R.id.txtWindSpeed)).setText(getWindDescription());

        //If it's currently daytime then show sunrise time on left and sunset on right
        if (currentTime.after(sunriseTime) && currentTime.before(sunsetTime))
        {
            ((TextView) findViewById(R.id.txtSunrise)).setText(getString(R.string.sunrise) + "\n" + timeFormat.format(sunriseTime));
            ((TextView) findViewById(R.id.txtSunset)).setText(getString(R.string.sunset) + "\n" + timeFormat.format(sunsetTime));
        }
        else //Otherwise reverse them
        {
            ((TextView) findViewById(R.id.txtSunrise)).setText(getString(R.string.sunset) + "\n" + timeFormat.format(sunsetTime));
            ((TextView) findViewById(R.id.txtSunset)).setText(getString(R.string.sunrise) + "\n" + timeFormat.format(sunriseTime));
        }

        //Add an OnLayoutChangeListener to measure the sunrise/sunset arc once views have been measured
        findViewById(R.id.txtSunset).addOnLayoutChangeListener(new View.OnLayoutChangeListener()
        {
            @Override
            public void onLayoutChange(View view, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom)
            {
                drawSpace.createArc();
            }
        });

        //Show text fields
        setVisibility(View.VISIBLE);

        //Hide loading screen
        findViewById(R.id.pbarLoadingDisplay).setVisibility(View.INVISIBLE);
        findViewById(R.id.txtLoadingText).setVisibility(View.INVISIBLE);
    }

    //Retries connection to API if previous calls failed
    public void retryConnection()
    {
        //Show connection failure text
        ((TextView)findViewById(R.id.txtLoadingText)).setText(getString(R.string.retrieving_data_retry));

        //Retry connection
        JsonRetriever retriever = new JsonRetriever();
        retriever.callbackActivity = this;
        retriever.execute(MINNEAPOLIS_URL);
    }

    //Builds a string describing the current wind speed and direction
    public String getWindDescription()
    {
        StringBuilder builder = new StringBuilder();

        //Get wind speed descriptor based on the current wind speed in meters per second
        if (currentWeatherInfo.windSpeed > 10)
            builder.append(getString(R.string.wind_speed_heavy));
        else if (currentWeatherInfo.windSpeed > 5)
            builder.append(getString(R.string.wind_speed_moderate));
        else if (currentWeatherInfo.windSpeed > 1)
            builder.append(getString(R.string.wind_speed_light));
        else
            builder.append(getString(R.string.wind_speed_very_light));

        builder.append("\n");

        //Get wind direction descriptor based on the angle in degrees
        if (currentWeatherInfo.windDirection > 338 || currentWeatherInfo.windDirection < 23)
            builder.append(getString(R.string.wind_direction_east));
        else if (currentWeatherInfo.windDirection > 293)
            builder.append(getString(R.string.wind_direction_northeast));
        else if (currentWeatherInfo.windDirection > 248)
            builder.append(getString(R.string.wind_direction_north));
        else if (currentWeatherInfo.windDirection > 203)
            builder.append(getString(R.string.wind_direction_northwest));
        else if (currentWeatherInfo.windDirection > 158)
            builder.append(getString(R.string.wind_direction_west));
        else if (currentWeatherInfo.windDirection > 113)
            builder.append(getString(R.string.wind_direction_southwest));
        else if (currentWeatherInfo.windDirection > 68)
            builder.append(getString(R.string.wind_direction_south));
        else if (currentWeatherInfo.windDirection >= 23)
            builder.append(getString(R.string.wind_direction_southeast));

        return builder.toString();
    }

    //Used to show or hide the weather info display
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