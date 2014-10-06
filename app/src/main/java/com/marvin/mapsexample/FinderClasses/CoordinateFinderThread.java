package com.marvin.mapsexample.FinderClasses;

import android.content.Context;
import android.graphics.Canvas;
import android.view.SurfaceHolder;

import com.marvin.mapsexample.DialClasses.SineView;

/**
 * Created by Rasmus on 10/5/2014.
 */
public class CoordinateFinderThread extends Thread {
    boolean mRun;
    SurfaceHolder surfaceHolder;
    Canvas mCanvas;
    Context context;
    CoordinateFinder mCoordinateFinder;

    public CoordinateFinderThread(SurfaceHolder sHolder, Context ctx,
                                  CoordinateFinder coorFinder){

        surfaceHolder = sHolder;
        context = ctx;
        mRun = false;
        mCoordinateFinder = coorFinder;

    }

    void setRunning(boolean bRun){
        mRun = bRun;
    }

    public void run(){
        super.run();
        while(mRun){

            mCanvas = surfaceHolder.lockCanvas();

            if(mCanvas != null){
                mCoordinateFinder.doDraw(mCanvas);
                surfaceHolder.unlockCanvasAndPost(mCanvas);
            }
        }
    }



}
