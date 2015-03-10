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
        WeatherPage newWeatherPage = new WeatherPage();
        newWeatherPage.jsonRetrieved = false;
        return newWeatherPage;
    }

    public WeatherPage()
    {
        info = new WeatherInfo();
    }

    @Override
    public void onStart()
    {
        super.onStart();
        drawSpace = (DrawSpace) getView().findViewById(R.id.drawSpace);
        drawSpace.parentPage = this;

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

    public void updatePage()
    {
        Date currentTime = new Date();
        Date sunriseTime = new Date(info.sunrise * 1000);
        Date sunsetTime = new Date(info.sunset * 1000);
        SimpleDateFormat timeFormat = new SimpleDateFormat("h:mm", Locale.US);

        ((TextView) this.getView().findViewById(R.id.txtCurrentTemp)).setText(String.valueOf(Math.round(info.currentTemp)) + "\u00b0");
        ((TextView) this.getView().findViewById(R.id.txtWeatherDescription)).setText(info.description.substring(0, 1).toUpperCase() + info.description.substring(1));

        ((TextView) this.getView().findViewById(R.id.txtWindSpeed)).setText("Light wind\nblowing Northeast");

        ((TextView) this.getView().findViewById(R.id.txtCityName)).setText(info.cityName);
        ((TextView) this.getView().findViewById(R.id.txtSunrise)).setText("Sunrise\n" + timeFormat.format(sunriseTime));
        ((TextView) this.getView().findViewById(R.id.txtSunset)).setText("Sunset\n" + timeFormat.format(sunsetTime));
        ((TextView) this.getView().findViewById(R.id.txtCurrentTime)).setText(timeFormat.format(currentTime));

        drawSpace.update();
    }
}