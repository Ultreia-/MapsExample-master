package com.marvin.mapsexample.DialClasses;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.marvin.mapsexample.HelperPackage.Game;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Rasmus on 10/3/2014.
 */
public class SineView extends SurfaceView implements SurfaceHolder.Callback{

    Context context;
    SineCurveThread sineCurveThread;
    Paint sPaint;
    Paint gPaint;
    int wheelRotation = 0;
    int sineCurvePhase = 0;
    double amplitude;
    long ampLong;
    long period = 75;

    public SineView(Context ctx, AttributeSet attributeSet){
        super(ctx, attributeSet);
        context = ctx;
        SurfaceHolder holder = getHolder();
        holder.addCallback(this);
        sPaint = new Paint();
        sPaint.setColor(Color.GREEN);
        sPaint.setStrokeWidth(4);

        gPaint = new Paint();
        gPaint.setColor(Color.BLACK);
        gPaint.setStrokeWidth(2);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        sineCurveThread = new SineCurveThread(holder, context, this);
        sineCurveThread.setRunning(true);
        sineCurveThread.start();

        final TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                sineCurvePhase++;
            }
        };
        final Timer t = new Timer();
        t.scheduleAtFixedRate(timerTask, 0, period);

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
        canvas.drawRGB(33, 168, 195);
        drawGrid(canvas);
        curveControl(canvas);
    }

    public void curveControl(Canvas sineCanvas){
        int width =  sineCanvas.getWidth();
        int height = sineCanvas.getHeight();

        int halfHeight = height / 2;
        for (int i = 0; i < width; i++) {
            sineCanvas.drawPoint((int)(i + ((Math.random()* 150.0)*(Game.scramble-0.5))),
                    (int)(getNormalizedSine(i, halfHeight, width)+((Math.random()* 150.0)*(Game.scramble-0.5))), sPaint);

        }
    }

    int getNormalizedSine(int x, int halfY, int maxX) {

        wheelRotation = Game.currentRotation;
        amplitude = wheelRotation/((double) Game.totalNicks);
        ampLong = (long) Math.abs(amplitude*100);
        period = 100-ampLong;

        double piDouble = 2 * Math.PI;
        double factor = (piDouble / maxX);

        return (int) (amplitude * (Math.sin(x * factor + sineCurvePhase)) * halfY + halfY);


    }

    public void drawGrid(Canvas sineCanvas){

        int width =  sineCanvas.getWidth();
        int height = sineCanvas.getHeight();

        int gridNumberX = 8;
        int gridNumberY = 10;

        for (int i = 0; i < (height/gridNumberX); i++) {
            sineCanvas.drawLine(0, ((height/gridNumberX)+1)*i, width, ((height/gridNumberX)+1)*i, gPaint);
        }

        for (int i = 0; i < (width/gridNumberY); i++) {
            sineCanvas.drawLine(((width/gridNumberY)+1)*i, 0, ((width/gridNumberY)+1)*i, height, gPaint);
        }
    }
}
