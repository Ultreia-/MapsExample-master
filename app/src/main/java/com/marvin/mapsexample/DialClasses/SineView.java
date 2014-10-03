package com.marvin.mapsexample.DialClasses;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.marvin.mapsexample.R;

/**
 * Created by Rasmus on 10/3/2014.
 */
public class SineView extends SurfaceView implements SurfaceHolder.Callback{

    Context context;
    Canvas sineCanvas =new Canvas();
    private static final int BACKGROUND = 0xFFDDDDDD;

    public SineView(Context ctx, AttributeSet attributeSet){
        super(ctx, attributeSet);
        context = ctx;
        sineCanvas.drawColor(BACKGROUND);
        SurfaceHolder holder = getHolder();
        holder.addCallback(this);

    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {

    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i2, int i3) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {

    }
/*
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

    public void clear() {
        mBitmap = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
        mBitmap.eraseColor(BACKGROUND);
        mCanvas.setBitmap(mBitmap);
        invalidate();
    }
    */
}
