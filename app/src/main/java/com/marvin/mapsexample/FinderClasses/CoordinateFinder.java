package com.marvin.mapsexample.FinderClasses;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import android.graphics.Point;
import android.os.Handler;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Toast;

import com.marvin.mapsexample.DialClasses.DialTest;
import com.marvin.mapsexample.HelperPackage.Game;
import com.marvin.mapsexample.HelperPackage.RestCallbackInterface;
import com.marvin.mapsexample.HelperPackage.RestServer;
import com.marvin.mapsexample.MapsScreen;
import com.marvin.mapsexample.SignalRedirect.InitiatePaintView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by Rasmus on 10/6/2014.
 */
public class CoordinateFinder extends SurfaceView implements SurfaceHolder.Callback{

    Context context;
    SurfaceHolder holder;
    Paint gPaint;
    Paint bPaint;
    Paint fPaint;
    Paint goalPaint;
    CoordinateFinderThread coordinateFinderThread;

    int gridNumberX;
    int gridNumberY;

    int width;
    int height;

    private RestServer restServer = new RestServer();

    public CoordinateFinder(Context ctx){
        super(ctx);

        context = ctx;
        holder = getHolder();
        holder.addCallback(this);
        gPaint = new Paint();
        gPaint.setColor(Color.GREEN);
        gPaint.setStrokeWidth(2);

        bPaint = new Paint();
        bPaint.setColor(Color.GREEN);
        bPaint.setStyle(Paint.Style.STROKE);
        bPaint.setStrokeWidth(4);

        fPaint = new Paint();
        fPaint.setColor(Color.GREEN);
        fPaint.setStyle(Paint.Style.STROKE);
        fPaint.setStrokeWidth(10);

        goalPaint = new Paint();
        goalPaint.setColor(Color.RED);
        goalPaint.setStyle(Paint.Style.STROKE);
        goalPaint.setStrokeWidth(20);

        checkForNewScrambleData();
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        coordinateFinderThread = new CoordinateFinderThread(holder, context, this);
        coordinateFinderThread.setRunning(true);
        coordinateFinderThread.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i2, int i3) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        coordinateFinderThread.setRunning(false);
        boolean retry = true;
        while (retry){
            try {
                coordinateFinderThread.join();
                retry = false;
            }
            catch (Exception e){
                Log.v("Exception!", e.getMessage());
            }
        }
    }

    void doDraw(Canvas canvas){
        canvas.drawColor(Color.GRAY);
        drawGrid(canvas);
        drawFinder(Game.coorFinder, canvas);
        drawGoalPoint(Game.coorGoalPoint, canvas);
    }

    private void checkForNewScrambleData()
    {
        restServer.requestGet("http://marvin.idyia.dk/game/checkForNewScrambleData/",
                new checkForNewScrambleDataCallback());
    }

    private class checkForNewScrambleDataCallback implements RestCallbackInterface
    {
        public void onEndRequest(JSONObject result)
        {
            try
            {
                String status = result.getString("status");

                if (status.equals("200"))
                {
                    JSONObject data = result.getJSONObject("data");
                    String newData = data.getString("newData");

                    Log.v("NewData", newData);

                    if(newData.equals("1"))
                    {
                        String scramble = data.getString("scramble");
                        String amplitude = data.getString("amplitude");

                        double scrambleDouble = Double.parseDouble(scramble);
                        int scrambleInt = (int) Math.round(scrambleDouble*20);

                        Game.coorGoalPoint = new Point(
                                scrambleInt,
                                Integer.parseInt(amplitude)+32);

                        int xPyth = Game.coorGoalPoint.x-Game.coorFinder.x;
                        int yPyth = Game.coorGoalPoint.y-Game.coorFinder.y;

                        if(Math.sqrt((xPyth*xPyth)+(yPyth*yPyth))> 10) //Game.coorGoalPoint.equals(Game.coorFinder.x, Game.coorFinder.y)
                        {
                            new AlertDialog.Builder(context)
                                .setTitle("You have arrived at the RFID scanner")
                                .setMessage("Press ok to interact")
                                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        Intent i;
                                        i = new Intent(context, InitiatePaintView.class);
                                        context.startActivity(i);
                                    }
                                })
                                .setIcon(android.R.drawable.ic_dialog_info)
                                .show();

                            restServer.requestPost("http://marvin.idyia.dk/game/signalFrequencyFound",
                                new HashMap<String, String>() {{
                                }},
                                new DeadCallback());
                        }
                        else
                        {
                            restServer.requestPost("http://marvin.idyia.dk/game/checkScrambleData",
                                new HashMap<String, String>() {{
                                }},
                                new DeadCallback());
                        }

                    }

                    new Handler().postDelayed(new Runnable() {
                        public void run() {
                            restServer.requestGet("http://marvin.idyia.dk/game/checkForNewScrambleData/",
                                    new checkForNewScrambleDataCallback());

                        }
                    }, 500);


                } else throw new Exception(status);
            }
            catch (JSONException e)
            {
                e.printStackTrace();
                Toast.makeText(context, "checkForNewScrambleData; JSON " + e, Toast.LENGTH_LONG).show();
            }
            catch (Exception e)
            {
                e.printStackTrace();
                Toast.makeText(context, "checkForNewScrambleData; status " + e, Toast.LENGTH_LONG).show();
            }
        }
    }

    private class DeadCallback implements RestCallbackInterface
    {
        public void onEndRequest(JSONObject result)
        {
            try
            {
                String status = result.getString("status");

                if (status.equals("200"))
                {

                } else throw new Exception(status);
            }
            catch (JSONException e)
            {
                e.printStackTrace();
                Toast.makeText(context, "DeadCallback; JSON " + e, Toast.LENGTH_LONG).show();
            }
            catch (Exception e)
            {
                e.printStackTrace();
                Toast.makeText(context, "DeadCallback; status " + e, Toast.LENGTH_LONG).show();
            }
        }
    }

    public void drawGrid(Canvas finderCanvas){

        width =  finderCanvas.getWidth();
        height = finderCanvas.getHeight();

        gridNumberX = 64;
        gridNumberY = 20;

        for (int i = 0; i < (height/(gridNumberX/4)); i++) {
            finderCanvas.drawLine(0, (height / gridNumberX) * i, width, (height / gridNumberX) * i, gPaint);
        }

        for (int i = 0; i < (width/gridNumberY); i++) {
            finderCanvas.drawLine(((width / gridNumberY)+1) * i, 0, ((width / gridNumberY)+1) * i, height, gPaint);
        }

    }

    public void drawFinder(Point coorF, Canvas canvas){
        int yInterval = (height/64)+1;
        int xInterval = (width/20)+1;

        canvas.drawCircle(coorF.x*xInterval, coorF.y*yInterval, 20, fPaint);
    }

    public void drawGoalPoint(Point coorG, Canvas canvas){
        //Create method to fetch currentRotation and scramble from server
        int yInterval = (height/64)+1;
        int xInterval = (width/20)+1;

        canvas.drawPoint((float) (coorG.x * xInterval), (float) (coorG.y * yInterval), goalPaint);
    }

    private class getFinderCoordinatesCallback implements RestCallbackInterface{

        @Override
        public void onEndRequest(JSONObject result) {

        }
    }
}
