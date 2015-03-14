package com.fakecompany.weather;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import com.fakecompany.weatherapp.R;

//This custom view is used to draw an arc that shows an estimation of
//how close the sun is to setting/rising
public class DrawSpace extends View
{
    FrontPage parentActivity;
    Paint paint;
    RectF sunRect; //A rectangle used to size the arc
    Bitmap sunIcon, moonIcon; //Sun and moon icons to be drawn on the arc
    boolean arcReady; //Used to tell whether or not the rectangle for the arc has been created

    public DrawSpace(Context context, AttributeSet attrs)
    {
        super(context, attrs);

        arcReady = false;

        paint = new Paint();
        paint.setColor(getResources().getColor(R.color.white));
        paint.setStrokeWidth(getResources().getDimension(R.dimen.line_size));
        paint.setStyle(Paint.Style.STROKE);
        paint.setFlags(Paint.ANTI_ALIAS_FLAG);

        sunIcon = BitmapFactory.decodeResource(getResources(), R.drawable.ic_sun);
        moonIcon = BitmapFactory.decodeResource(getResources(), R.drawable.ic_moon);
    }

    @Override
    protected void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);

        if (arcReady)
        {
            canvas.drawArc(sunRect, 180, 180, false, paint);

            long currentTimeStamp = System.currentTimeMillis() / 1000;

            //If daytime draw the sun icon based on the time between sunrise and sunset
            if (currentTimeStamp > parentActivity.currentWeatherInfo.sunrise && currentTimeStamp < parentActivity.currentWeatherInfo.sunset)
            {
                double angle = Math.toRadians(180 + ((double) (currentTimeStamp - parentActivity.currentWeatherInfo.sunrise) / (double) (parentActivity.currentWeatherInfo.sunset - parentActivity.currentWeatherInfo.sunrise)) * 180);
                int iconX = (int) (sunRect.centerX() + (Math.cos(angle) * (sunRect.width() / 2)) - (sunIcon.getWidth() / 2));
                int iconY = (int) (sunRect.centerY() + (Math.sin(angle) * (sunRect.height() / 2)) - (sunIcon.getHeight() / 2));
                canvas.drawBitmap(sunIcon, iconX, iconY, paint);
            }
            else //Otherwise draw the moon icon based on the time between sunset and sunrise
            {
                double angle = Math.toRadians(180 + (((double)(currentTimeStamp - parentActivity.currentWeatherInfo.sunset) / (double)(86400 - (parentActivity.currentWeatherInfo.sunset - parentActivity.currentWeatherInfo.sunrise))) * 180));
                int iconX = (int) (sunRect.centerX() + (Math.cos(angle) * (sunRect.width() / 2)) - (moonIcon.getWidth() / 2));
                int iconY = (int) (sunRect.centerY() + (Math.sin(angle) * (sunRect.height() / 2)) - (moonIcon.getHeight() / 2));
                canvas.drawBitmap(moonIcon, iconX, iconY, paint);
            }
        }
    }

    //Create the arc rectangle, and then invalidate the view to draw it
    public void createArc()
    {
        View sunriseView = parentActivity.findViewById(R.id.txtSunrise);
        View sunsetView = parentActivity.findViewById(R.id.txtSunset);

        int heightRadius = (int)((sunsetView.getTop() - parentActivity.findViewById(R.id.viewSpacer2).getBottom()) * .6);
        sunRect = new RectF(sunriseView.getRight() - (sunriseView.getWidth() / 2), sunriseView.getTop() - heightRadius, sunsetView.getLeft() + (sunsetView.getWidth() / 2), sunriseView.getTop() + heightRadius);

        arcReady = true;

        invalidate();
    }
}