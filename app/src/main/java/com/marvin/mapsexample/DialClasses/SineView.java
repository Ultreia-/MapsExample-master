package com.marvin.mapsexample.DialClasses;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.marvin.mapsexample.HelperPackage.Game;
import com.marvin.mapsexample.IntroScreen;

/**
 * Created by Rasmus on 10/3/2014.
 */
public class SineView extends SurfaceView implements SurfaceHolder.Callback{

    Context context;
    SineCurveThread sineCurveThread;
    Paint mPaint;
    int wheelRotation = 0;

    public void run(){
    }

    public SineView(Context ctx, AttributeSet attributeSet){
        super(ctx, attributeSet);
        context = ctx;
        SurfaceHolder holder = getHolder();
        holder.addCallback(this);
        mPaint = new Paint();
        mPaint.setColor(Color.BLACK);
        mPaint.setStrokeWidth(6);

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
        curveControl(canvas);
        //Log.v("doDraw", "is running");
    }

    public void curveControl(Canvas sineCanvas){
        int width =  sineCanvas.getWidth();
        int height = sineCanvas.getHeight();

        int halfHeight = height / 2;
        //Log.v("Curvecontrol", "Width: " + Integer.toString(width));
        for (int i = 0; i < width; i++) {
            sineCanvas.drawPoint(i, getNormalizedSine(i, halfHeight, width), mPaint);
            //Log.v("CurveControl", "Numbaaa: " + Integer.toString(i));
        }
        //Log.v("CurveControl","Method has been entered");
    }

    int getNormalizedSine(int x, int halfY, int maxX) {
        wheelRotation = Game.currentRotation;
        //Log.v("Normalized Line","Amplitude: " + Integer.toString(wheelRotation));
        double amplitude = wheelRotation/((double) Game.totalNicks);
        //Log.v("Normalized Line","Amplitude: " + Double.toString(amplitude));
        double piDouble = 2 * Math.PI;
        double factor = (piDouble / maxX);
        //Log.v("Normalized Line","Factor: " + Double.toString(factor));
        //Log.v("Normalized Line", "Drawn");
        return (int) (amplitude*(Math.sin(x * factor)) * halfY + halfY);
    }
}
