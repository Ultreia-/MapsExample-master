package com.marvin.mapsexample.DialClasses;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.marvin.mapsexample.R;

/**
 * Created by Rasmus on 10/3/2014.
 */
public class SineView extends SurfaceView implements SurfaceHolder.Callback{

    Context context;
    Canvas sineCanvas = new Canvas();
    SineCurveThread sineCurveThread;
    private static final int BACKGROUND = 0xFFDDDDDD;
    Paint mPaint;


    public SineView(Context ctx, AttributeSet attributeSet){
        super(ctx, attributeSet);
        context = ctx;
        sineCanvas.drawColor(BACKGROUND);
        SurfaceHolder holder = getHolder();
        holder.addCallback(this);
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setColor(Color.BLACK);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(12);

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        sineCurveThread = new SineCurveThread(holder, context, this);
        sineCurveThread.setRunning(true);
        sineCurveThread.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i2, int i3) {
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        sineCurveThread.setRunning(false);
        boolean retry = true;
        while (retry){
            try {
                sineCurveThread.join();
                retry = false;
            }
            catch (Exception e){
                Log.v("Exception!", e.getMessage());
            }
        }
    }

    void doDraw(Canvas canvas){
        canvas.drawColor(Color.CYAN);
        //canvas.drawLine(0,0,100,100, mPaint);
    }
}
