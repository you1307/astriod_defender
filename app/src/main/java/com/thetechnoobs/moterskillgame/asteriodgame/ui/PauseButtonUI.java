package com.thetechnoobs.moterskillgame.asteriodgame.ui;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.RectF;
import android.util.DisplayMetrics;

import com.thetechnoobs.moterskillgame.R;

public class PauseButtonUI {
    public Bitmap pauseButtonBitmap, playButtonBitmap;
    Resources resources;
    int locX, locY;

    public PauseButtonUI(Resources resources, int locX, int locY) {
        this.resources = resources;
        this.locX = locX;
        this.locY = locY;

        pauseButtonBitmap = BitmapFactory.decodeResource(resources, R.drawable.circle_pause);
        Bitmap.createScaledBitmap(pauseButtonBitmap, (int) convertDpToPixel(50), (int) convertDpToPixel(50), false);

        playButtonBitmap = BitmapFactory.decodeResource(resources, R.drawable.circle_play_button);
        Bitmap.createScaledBitmap(playButtonBitmap, (int) convertDpToPixel(50), (int) convertDpToPixel(50), false);
    }

    public static float convertDpToPixel(float dp) {
        DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();
        return Math.round(dp * (metrics.densityDpi / 160f));
    }

    public RectF getHitBox(){
        return new RectF(locX, locY, locX+pauseButtonBitmap.getWidth(), locY+pauseButtonBitmap.getHeight());
    }

    public int getLocX() {
        return locX;
    }

    public void setLocX(int locX) {
        this.locX = locX;
    }

    public int getLocY() {
        return locY;
    }

    public void setLocY(int locY) {
        this.locY = locY;
    }
}
