package com.fakecompany.weatherapp;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import java.util.Date;

public class DrawSpace extends View
{
    WeatherPage parentPage;
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

        if (parentPage.jsonRetrieved)
        {
            canvas.drawArc(sunRect, 180, 180, false, paint);

            long currentTimeStamp = (new Date().getTime() / 1000);

            if (currentTimeStamp > parentPage.info.sunrise && currentTimeStamp < parentPage.info.sunset)
            {
                double angle = Math.toRadians(180 + ((double) (currentTimeStamp - parentPage.info.sunrise) / (double) (parentPage.info.sunset - parentPage.info.sunrise)) * 180);
                int iconX = (int) (sunRect.centerX() + (Math.cos(angle) * (sunRect.width() / 2)) - (sunIcon.getWidth() / 2));
                int iconY = (int) (sunRect.centerY() + (Math.sin(angle) * (sunRect.height() / 2)) - (sunIcon.getHeight() / 2));
                canvas.drawBitmap(sunIcon, iconX, iconY, paint);
            }
            else
            {
                double angle = Math.toRadians(180 + (((currentTimeStamp - parentPage.info.sunset) / (86400 - (parentPage.info.sunset - parentPage.info.sunrise))) * 180));
                int iconX = (int) (sunRect.centerX() + (Math.cos(angle) * (sunRect.width() / 2)) - (moonIcon.getWidth() / 2));
                int iconY = (int) (sunRect.centerY() + (Math.sin(angle) * (sunRect.height() / 2)) - (moonIcon.getHeight() / 2));
                canvas.drawBitmap(moonIcon, iconX, iconY, paint);
            }
        }
    }

    public void createArc()
    {
        View sunriseView = parentPage.getView().findViewById(R.id.txtSunrise);
        View sunsetView = parentPage.getView().findViewById(R.id.txtSunset);

        int heightRadius = ((sunsetView.getLeft() + (sunsetView.getWidth() / 2)) - (sunriseView.getLeft() + (sunriseView.getWidth() / 2))) / 3;
        sunRect = new RectF(sunriseView.getLeft() + (sunriseView.getWidth() / 2), sunriseView.getTop() - heightRadius, sunsetView.getLeft() + (sunsetView.getWidth() / 2), sunriseView.getTop() + heightRadius);

        invalidate();
    }
}