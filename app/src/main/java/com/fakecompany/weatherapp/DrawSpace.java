package com.fakecompany.weatherapp;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

public class DrawSpace extends View
{
    Paint paint = new Paint();

    public DrawSpace(Context context)
    {
        super(context);
    }

    public DrawSpace(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    @Override
    protected void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);
    }
}
