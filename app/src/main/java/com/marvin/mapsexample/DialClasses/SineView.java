package com.marvin.mapsexample.DialClasses;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.SurfaceView;

/**
 * Created by Rasmus on 10/3/2014.
 */
public class SineView extends SurfaceView{

        private Bitmap mBitmap;
        private Canvas mCanvas;
        private Paint mPaint;
        private static final int BACKGROUND = 0xFFDDDDDD;

        public SineView(Context context) {
            super(context);
            mCanvas = new Canvas();
            mPaint = new Paint();
            mPaint.setAntiAlias(true);
            mPaint.setDither(true);
            mPaint.setColor(Color.BLACK);
            mPaint.setStyle(Paint.Style.STROKE);
            mPaint.setStrokeJoin(Paint.Join.ROUND);
            mPaint.setStrokeCap(Paint.Cap.ROUND);
            mPaint.setStrokeWidth(12);
        }

        @Override
        protected void onSizeChanged(int width, int height, int oldWidth, int oldHeight) {
            super.onSizeChanged(width, height, oldWidth, oldHeight);
            clear();

        }

        @Override
        protected void onDraw(Canvas canvas) {
            canvas.drawColor(BACKGROUND);
        }

        /**
         * Clears canvas
         */
        public void clear() {
            mBitmap = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
            mBitmap.eraseColor(BACKGROUND);
            mCanvas.setBitmap(mBitmap);
            invalidate();
        }
    }
