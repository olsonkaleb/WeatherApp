package com.fakecompany.weatherapp;

import android.net.Uri;
import android.net.http.AndroidHttpClient;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
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
import java.util.ArrayList;
import java.util.List;

//API KEY: c888e616376f2d4854883d881a0e07d4
//http://api.openweathermap.org/data/2.5/weather?id=5037649&APPID=c888e616376f2d4854883d881a0e07d4

public class FrontPage extends ActionBarActivity implements WeatherPage.OnFragmentInteractionListener
{
    public WeatherInfo currentInfo;
    public List<WeatherPage> f = new ArrayList<WeatherPage>();
    public int[] cityIds = new int[] {5037649, 5045360};

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_front_page);

        f.add(WeatherPage.newInstance());
        f.add(WeatherPage.newInstance());

        for (int i = 0; i < cityIds.length; i++)
        {
            JsonRetriever retriever = new JsonRetriever();
            retriever.linkedPage = f.get(i);
            retriever.execute("http://api.openweathermap.org/data/2.5/weather?id=" + cityIds[i] + "&APPID=c888e616376f2d4854883d881a0e07d4");
        }

        ViewPager vp = (ViewPager) findViewById(R.id.vp_WeatherPager);
        vp.setAdapter(new asd(getSupportFragmentManager(), f));

        //new JsonRetriever().execute("http://api.openweathermap.org/data/2.5/weather?id=5037649&APPID=c888e616376f2d4854883d881a0e07d4");
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

    @Override
    public void onFragmentInteraction(Uri uri)
    {
    }

    public class asd extends FragmentStatePagerAdapter
    {
        public List<WeatherPage> pages;

        public asd(FragmentManager fm, List<WeatherPage> _pages)
        {
            super(fm);
            pages = _pages;
        }

        @Override
        public Fragment getItem(int position)
        {
            return pages.get(position);
        }

        @Override
        public int getCount()
        {
            return pages.size();
        }
    }
}