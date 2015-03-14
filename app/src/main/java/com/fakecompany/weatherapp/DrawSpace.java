package com.fakecompany.weatherapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import java.util.Date;

public class DrawSpace extends View
{
    FrontPage parentActivity;
    Paint paint;
    RectF sunRect;
    Bitmap sunIcon, moonIcon;

    public DrawSpace(Context context, AttributeSet attrs)
    {
        super(context, attrs);

        paint = new Paint();
        paint.setColor(0xFFFFFFFF);
        paint.setStrokeWidth(5);
        paint.setStyle(Paint.Style.STROKE);
        paint.setFlags(Paint.ANTI_ALIAS_FLAG);

        sunIcon = BitmapFactory.decodeResource(getResources(), R.drawable.ic_sun);
        moonIcon = BitmapFactory.decodeResource(getResources(), R.drawable.ic_moon);
    }

    @Override
    protected void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);

        if (parentActivity.jsonRetrieved)
        {
            canvas.drawArc(sunRect, 180, 180, false, paint);

            long currentTimeStamp = (new Date().getTime() / 1000);

            if (currentTimeStamp > parentActivity.currentWeatherInfo.sunrise && currentTimeStamp < parentActivity.currentWeatherInfo.sunset)
            {
                double angle = Math.toRadians(180 + ((double) (currentTimeStamp - parentActivity.currentWeatherInfo.sunrise) / (double) (parentActivity.currentWeatherInfo.sunset - parentActivity.currentWeatherInfo.sunrise)) * 180);
                int iconX = (int) (sunRect.centerX() + (Math.cos(angle) * (sunRect.width() / 2)) - (sunIcon.getWidth() / 2));
                int iconY = (int) (sunRect.centerY() + (Math.sin(angle) * (sunRect.height() / 2)) - (sunIcon.getHeight() / 2));
                canvas.drawBitmap(sunIcon, iconX, iconY, paint);
            }
            else
            {
                double angle = Math.toRadians(180 + (((double)(currentTimeStamp - parentActivity.currentWeatherInfo.sunset) / (double)(86400 - (parentActivity.currentWeatherInfo.sunset - parentActivity.currentWeatherInfo.sunrise))) * 180));
                int iconX = (int) (sunRect.centerX() + (Math.cos(angle) * (sunRect.width() / 2)) - (moonIcon.getWidth() / 2));
                int iconY = (int) (sunRect.centerY() + (Math.sin(angle) * (sunRect.height() / 2)) - (moonIcon.getHeight() / 2));
                canvas.drawBitmap(moonIcon, iconX, iconY, paint);
            }
        }
    }

    public void createArc()
    {
        View sunriseView = parentActivity.findViewById(R.id.txtSunrise);
        View sunsetView = parentActivity.findViewById(R.id.txtSunset);

        sunriseView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        sunsetView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);

        int heightRadius = (int)((sunsetView.getTop() - parentActivity.findViewById(R.id.viewSpacer2).getBottom()) * .6);
        sunRect = new RectF(sunriseView.getRight() - (sunriseView.getMeasuredWidth() / 2), sunriseView.getTop() - heightRadius, sunsetView.getLeft() + (sunsetView.getMeasuredWidth() / 2), sunriseView.getTop() + heightRadius);

        invalidate();
    }
}