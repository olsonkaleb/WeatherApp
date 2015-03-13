package com.fakecompany.weatherapp;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;

//API KEY: c888e616376f2d4854883d881a0e07d4

public class FrontPage extends ActionBarActivity
{
    public ArrayList<WeatherPage> weatherPages = new ArrayList<WeatherPage>();
    public int[] cityIds = new int[] {5037649, 5045360, 5128638};
    public int retrievedPages;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_front_page);

        retrievedPages = 0;

        weatherPages.add(WeatherPage.newInstance());
        weatherPages.add(WeatherPage.newInstance());
        weatherPages.add(WeatherPage.newInstance());

        for (int i = 0; i < cityIds.length; i++)
        {
            JsonRetriever retriever = new JsonRetriever();
            retriever.callbackActivity = this;
            retriever.linkedPage = weatherPages.get(i);
            retriever.execute("http://api.openweathermap.org/data/2.5/weather?id=" + cityIds[i] + "&APPID=c888e616376f2d4854883d881a0e07d4");
        }
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

    public void assemblePages()
    {
        ViewPager vp = (ViewPager) findViewById(R.id.vp_WeatherPager);
        vp.setAdapter(new WeatherPageAdapter(getSupportFragmentManager(), weatherPages));
    }

    public class WeatherPageAdapter extends FragmentStatePagerAdapter
    {
        public List<WeatherPage> pages;

        public WeatherPageAdapter(FragmentManager fm, List<WeatherPage> _pages)
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

        @Override
        public CharSequence getPageTitle(int position)
        {
            return pages.get(position).info.cityName;
        }
    }
}