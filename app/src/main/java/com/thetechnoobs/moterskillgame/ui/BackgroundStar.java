package com.thetechnoobs.moterskillgame.ui;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.thetechnoobs.moterskillgame.Constants;
import com.thetechnoobs.moterskillgame.R;

public class BackgroundStar {
    int CurX = 0, CurY = 10;
    int speed = 0;
    int[] screenSize;
    public Bitmap stareBitmap;

    public BackgroundStar(int curX, int curY, int speed, int[] screenSize, Resources res) {
        CurX = curX;
        CurY = curY;
        this.screenSize = screenSize;
        this.speed = speed;

        stareBitmap = BitmapFactory.decodeResource(res, R.drawable.star);
        stareBitmap = Bitmap.createScaledBitmap(stareBitmap,
                screenSize[0] / Constants.SCALE_RATIO_NUM_X_BACKGROUND,
                screenSize[1] / Constants.SCALE_RATIO_NUM_Y_BACKGROUND,
                false);
    }

    public int getCurX() {
        return CurX;
    }

    public void setCurX(int curX) {
        CurX = curX;
    }

    public int getCurY() {
        return CurY;
    }

    public void setCurY(int curY) {
        CurY = curY;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }
}
