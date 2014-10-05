package com.marvin.mapsexample.DialClasses;

import android.content.Context;
import android.graphics.Canvas;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * Created by Rasmus on 10/5/2014.
 */
public class SineCurveThread extends Thread {
    boolean mRun;
    SurfaceHolder surfaceHolder;
    Canvas mCanvas;
    Context context;
    SineView mSineView;

    public SineCurveThread(SurfaceHolder sHolder, Context ctx,
                           SineView sineView){

        surfaceHolder = sHolder;
        context = ctx;
        mRun = false;
        mSineView = sineView;

    }

    void setRunning(boolean bRun){
        mRun = bRun;
    }

    public void run(){
        super.run();
        while(mRun){

            mCanvas = surfaceHolder.lockCanvas();

            if(mCanvas != null){
                mSineView.doDraw(mCanvas);
                surfaceHolder.unlockCanvasAndPost(mCanvas);
            }
        }
    }

}
