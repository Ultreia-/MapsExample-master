package com.marvin.mapsexample.SignalRedirect;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.os.Vibrator;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;

import com.marvin.mapsexample.OSScreen;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by christianheldingsrensen on 03/10/14.
 */
public class PaintView extends View {

    private Bitmap mBitmap;
    private Canvas mCanvas;
    private Path mPath;
    private Paint mPaint;
    private static final int TOUCH_TOLERANCE_DP = 24;
   // private static final int BACKGROUND = 0xFFDDDDDD;
    private List<Point> mPoints = new ArrayList<Point>();
    private int mLastPointIndex = 0;
    private int mTouchTolerance;
    private boolean isPathStarted = false;
    private Point p1;
    private Context context;

    public PaintView(Context context) {
        super(context);
        mCanvas = new Canvas();
        mPath = new Path();
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setColor(Color.WHITE);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStrokeWidth(10);
        mTouchTolerance = dp2px(TOUCH_TOLERANCE_DP);

        // TODO just test points
        p1 = new Point(20, 20);
        Point p2 = new Point(20, 400);
        Point p3 = new Point(300, 250);
        Point p4 = new Point(780, 400);
        Point p5 = new Point(780, 20);
        Point p6 = new Point(500, 150);
        mPoints.add(p1);
        mPoints.add(p2);
        mPoints.add(p3);
        mPoints.add(p4);
        mPoints.add(p5);
        mPoints.add(p6);
    }

    @Override
    protected void onSizeChanged(int width, int height, int oldWidth, int oldHeight) {
        super.onSizeChanged(width, height, oldWidth, oldHeight);
        clear();

    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawColor(Color.BLACK);
        canvas.drawBitmap(mBitmap, 0, 0, null);
        canvas.drawPath(mPath, mPaint);

        canvas.drawCircle(p1.x, p1.y, 1, mPaint);
        canvas.drawCircle(p1.x, p1.y, 35, mPaint);
        canvas.drawCircle(p1.x, p1.y, 50, mPaint);

        // TODO remove if you dont want points to be drawn
        for (Point point : mPoints) {
            canvas.drawCircle(point.x, point.y, 20, mPaint);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                touch_start(x, y);
                invalidate();
                break;
            case MotionEvent.ACTION_MOVE:
                touch_move(x, y);
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                touch_up(x, y);
                invalidate();
                break;
        }
        return true;
    }

    private void touch_start(float x, float y) {

        if (checkPoint(x, y, mLastPointIndex)) {
            mPath.reset();
            // user starts from given point so path can beis started
            isPathStarted = true;
        } else {
            // user starts move from point which doen's belongs to mPinst list
            isPathStarted = false;
        }

    }

    //ADDED WITH LAST EDIT
    private void touch_move(float x, float y) {
        // draw line with finger move
        if (isPathStarted) {
            mPath.reset();
            Point p = mPoints.get(mLastPointIndex);
            mPath.moveTo(p.x, p.y);
            mPath.lineTo(x, y);
        }
    }

    /**
     * Draws line.
     */
    private void touch_up(float x, float y) {
        mPath.reset();
        context = getContext();
        Vibrator v = (Vibrator) this.context.getSystemService(Context.VIBRATOR_SERVICE);

        if (checkPoint(x, y, mLastPointIndex + 1) && isPathStarted) {
            // move finished at valid point so draw whole line

            // start point
            Point p = mPoints.get(mLastPointIndex);
            mPath.moveTo(p.x, p.y);
            // end point
            p = mPoints.get(mLastPointIndex + 1);
            mPath.lineTo(p.x, p.y);
            mCanvas.drawPath(mPath, mPaint);
            mPath.reset();
            // increment point index
            ++mLastPointIndex;
            isPathStarted = false;
        } else{
            mLastPointIndex = 0;
            mPath.reset();
            isPathStarted = false;
            // Vibrate for 500 milliseconds
            v.vibrate(600);
            onDraw(mCanvas);
        }

        if (mLastPointIndex+1 == mPoints.size()){
            Intent i = new Intent(context, OSScreen.class);
            context.startActivity(i);
        }
    }

    /**
     * Sets paint
     *
     * @param paint
     */
    public void setPaint(Paint paint) {
        this.mPaint = paint;
    }

    /**
     * Returns image as bitmap
     *
     * @return
     */
    public Bitmap getBitmap() {
        return mBitmap;
    }

    /**
     * Clears canvas
     */
    public void clear() {
        mBitmap = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
        mBitmap.eraseColor(Color.BLACK);
        mCanvas.setBitmap(mBitmap);
        invalidate();
    }

    /**
     * Checks if user touch point with some tolerance
     */
    private boolean checkPoint(float x, float y, int pointIndex) {
        if (pointIndex == mPoints.size()) {
            // out of bounds
            return false;
        }
        Point point = mPoints.get(pointIndex);
        //EDIT changed point.y to poin.x in the first if statement
        if (x > (point.x - mTouchTolerance) && x < (point.x + mTouchTolerance)) {
            if (y > (point.y - mTouchTolerance) && y < (point.y + mTouchTolerance)) {
                return true;
            }
        }
        return false;
    }

    public List<Point> getPoints() {
        return mPoints;
    }

    public void setPoints(List<Point> points) {
        this.mPoints = points;
    }

    private int dp2px(int dp) {
        Resources r = getContext().getResources();
        float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics());
        return (int) px;
    }
}
