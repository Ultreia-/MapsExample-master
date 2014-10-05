package com.marvin.mapsexample.DialClasses;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.util.Log;

import com.marvin.mapsexample.HelperPackage.Game;

public class DialModel {
    public interface Listener {
        void onDialPositionChanged(DialModel sender, int nicksChanged);
    }

    private List<Listener> listeners = new ArrayList<Listener>();

    private int currentNick = 0;
    private boolean countUp = false;
    private boolean countDown = false;
    public DialModel() {

    }

    public final void rotate(int nicks) {

        if(currentNick >= Game.totalNicks)
        {
            countDown = true;
        }
        if(currentNick <= -Game.totalNicks)
        {
            countUp = true;
        }
        if(currentNick == 0 || (countDown && nicks < 0) || (countUp && nicks > 0))
        {
            countDown = false;
            countUp = false;
        }

        if(countDown && nicks > 0)
        {
            currentNick--;
        }
        else if(countUp && nicks < 0)
        {
            currentNick++;
        }
        else
        {
            currentNick = (currentNick + nicks);
        }

        Log.v("nicks", Integer.toString(nicks));

        /*if(countDown)
        {
            currentNick--;
        }
        else if(countUp)
        {
            currentNick++;
        }
        else
        {
            currentNick += (currentNick + Game.totalNicks);
        }

        //if(currentNick == 15) countDown = true;

        if (currentNick >= Game.totalNicks) {
            countDown = true;
            //currentNick %= Game.totalNicks;
        } else if (currentNick < -Game.totalNicks+1) {
            countUp = true;
            //currentNick += (currentNick + Game.totalNicks);
        } else if(currentNick == 0) {
            countDown = false;
            countUp = false;
        }
        */
        Game.currentRotation = currentNick;

        //Log.v("CurrentNick: ",Integer.toString(currentNick));

        for (Listener listener : listeners) {
            listener.onDialPositionChanged(this, nicks);
        }
    }

    public final float getRotationInDegrees() {
        return (360.0f / Game.totalNicks) * currentNick;
    }

    public final List<Listener> getListeners() {
        return listeners;
    }

    public final int getTotalNicks() {
        return Game.totalNicks;
    }

    public final int getCurrentNick() {
        return currentNick;
    }

    public final void addListener(Listener listener) {
        listeners.add(listener);
    }

    public final void removeListener(Listener listener) {
        listeners.remove(listener);
    }

    private static String getBundlePrefix() {
        return DialModel.class.getSimpleName() + ".";
    }

    public final void save(Bundle bundle) {
        String prefix = getBundlePrefix();

        bundle.putInt(prefix + "totalNicks", Game.totalNicks);
        bundle.putInt(prefix + "currentNick", currentNick);
    }

    public static DialModel restore(Bundle bundle) {
        DialModel model = new DialModel();

        String prefix = getBundlePrefix();
        //model.totalNicks = bundle.getInt(prefix + "totalNicks");
        model.currentNick = bundle.getInt(prefix + "currentNick");

        return model;
    }
}
