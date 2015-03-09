package com.fakecompany.weatherapp;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.util.Date;

public class DrawSpace extends View
{
    WeatherPage parentPage;
    Paint paint;
    RectF sunRect;
    Bitmap bitmap;

    public DrawSpace(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        paint = new Paint();
        paint.setColor(0xFFFFFFFF);
        paint.setStrokeWidth(5);
        paint.setStyle(Paint.Style.STROKE);
        paint.setFlags(Paint.ANTI_ALIAS_FLAG);

        Resources res = getResources();
        bitmap = BitmapFactory.decodeResource(res, R.drawable.ic_sun);
    }

    @Override
    protected void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);
        canvas.drawArc(sunRect, 180, 180, false, paint);

        long dif = parentPage.info.sunset - parentPage.info.sunrise;
        if (dif != 0)
        {
            long current = ((new Date()).getTime() / 1000) - parentPage.info.sunrise;
            double angle = 180 + (((double)current / (double)dif) * 180);
            double xx = Math.cos((Math.PI / 180) * angle);
            double yy = Math.sin((Math.PI / 180) * angle);
            canvas.drawBitmap(bitmap, sunRect.centerX() + (int)(xx * (sunRect.width() / 2)) - (bitmap.getWidth() / 2), sunRect.centerY() + (int)(yy * (sunRect.height() / 2) - (bitmap.getHeight() / 2)), paint);
        }
    }

    public void setParent(WeatherPage _parentPage)
    {
        parentPage = _parentPage;

        TextView center = (TextView) parentPage.getView().findViewById(R.id.txtCurrentTime);
        View left = parentPage.getView().findViewById(R.id.txtSunrise);
        TextView right = (TextView) parentPage.getView().findViewById(R.id.txtSunset);

        int a = (right.getLeft() + (right.getWidth() / 2)) - (left.getLeft() + (left.getWidth() / 2));
        sunRect = new RectF(left.getLeft() + (left.getWidth() / 2), left.getTop() - (a / 3), right.getLeft() + (right.getWidth() / 2), left.getTop() + (a / 3));
    }
}