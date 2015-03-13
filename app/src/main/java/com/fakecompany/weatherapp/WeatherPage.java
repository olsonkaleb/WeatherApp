package com.fakecompany.weatherapp;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextClock;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class WeatherPage extends Fragment
{
    public WeatherInfo info;
    public DrawSpace drawSpace;
    public boolean jsonRetrieved;

    public static WeatherPage newInstance()
    {
        return new WeatherPage();
    }

    public WeatherPage()
    {
        info = new WeatherInfo();
        jsonRetrieved = false;
    }

    @Override
    public void onStart()
    {
        super.onStart();
        setVisibility(View.INVISIBLE);
        drawSpace = (DrawSpace) getView().findViewById(R.id.drawSpace);
        drawSpace.parentPage = this;

        ((TextClock) getView().findViewById(R.id.txtclockCurrentTime)).setFormat12Hour("h:mm");

        if (jsonRetrieved)
            updatePage();
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.fragment_weather_page, container, false);
    }

    @Override
    public void onAttach(Activity activity)
    {
        super.onAttach(activity);
    }

    @Override
    public void onDetach()
    {
        super.onDetach();
    }

    public void setVisibility(int visibility)
    {
        getView().findViewById(R.id.txtCurrentTemp).setVisibility(visibility);
        getView().findViewById(R.id.txtWeatherDescription).setVisibility(visibility);
        getView().findViewById(R.id.txtWindSpeed).setVisibility(visibility);
        getView().findViewById(R.id.txtclockCurrentTime).setVisibility(visibility);
        getView().findViewById(R.id.txtSunrise).setVisibility(visibility);
        getView().findViewById(R.id.txtSunset).setVisibility(visibility);
        getView().findViewById(R.id.viewSpacer1).setVisibility(visibility);
        getView().findViewById(R.id.viewSpacer2).setVisibility(visibility);
    }

    public String getWindDescription()
    {
        StringBuilder builder = new StringBuilder();

        if (info.windSpeed > 10)
            builder.append("Heavy wind\n");
        else if (info.windSpeed > 5)
            builder.append("Moderate wind\n");
        else if (info.windSpeed > 1)
            builder.append("Light wind\n");
        else
            builder.append("Very light wind\n");

        if (info.windDirection > 338 || info.windDirection < 23)
            builder.append("Blowing East");
        else if (info.windDirection > 293)
            builder.append("Blowing Northeast");
        else if (info.windDirection > 248)
            builder.append("Blowing North");
        else if (info.windDirection > 203)
            builder.append("Blowing Northwest");
        else if (info.windDirection > 158)
            builder.append("Blowing West");
        else if (info.windDirection > 113)
            builder.append("Blowing Southwest");
        else if (info.windDirection > 68)
            builder.append("Blowing South");
        else if (info.windDirection >= 23)
            builder.append("Blowing Southeast");

        return builder.toString();
    }

    public void updatePage()
    {
        Date currentTime = new Date();
        Date sunriseTime = new Date(info.sunrise * 1000);
        Date sunsetTime = new Date(info.sunset * 1000);
        SimpleDateFormat timeFormat = new SimpleDateFormat("h:mm", Locale.US);

        ((TextView) getView().findViewById(R.id.txtCityName)).setText(info.cityName);

        ((TextView) getView().findViewById(R.id.txtCurrentTemp)).setText(String.valueOf(Math.round(info.currentTemp)) + "\u00b0");
        ((TextView) getView().findViewById(R.id.txtWeatherDescription)).setText(info.description.substring(0, 1).toUpperCase() + info.description.substring(1));

        ((TextView) getView().findViewById(R.id.txtWindSpeed)).setText(getWindDescription());

        if (currentTime.after(sunriseTime) && currentTime.before(sunsetTime))
        {
            ((TextView) getView().findViewById(R.id.txtSunrise)).setText("Sunrise\n" + timeFormat.format(sunriseTime));
            ((TextView) getView().findViewById(R.id.txtSunset)).setText("Sunset\n" + timeFormat.format(sunsetTime));
        }
        else
        {
            ((TextView) getView().findViewById(R.id.txtSunrise)).setText("Sunset\n" + timeFormat.format(sunsetTime));
            ((TextView) getView().findViewById(R.id.txtSunset)).setText("Sunrise\n" + timeFormat.format(sunriseTime));
        }

        getView().addOnLayoutChangeListener(new View.OnLayoutChangeListener()
            {
                @Override
                public void onLayoutChange(View view, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom)
                {
                    drawSpace.createArc();
                }
            }
        );

        setVisibility(View.VISIBLE);
    }
}