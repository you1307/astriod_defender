package com.thetechnoobs.moterskillgame;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.RectF;

public class RegularBullet {
    float CurX = 0, CurY = 0, Speed = 2;
    int[] screenSize = {0, 0};
    Bitmap bitmap;
    RectF Hitbox;

    public RegularBullet(Resources resources, int speed, int[] screenSize , float curX, float curY) {
        CurX = curX;
        CurY = curY;
        this.screenSize = screenSize;

        bitmap = BitmapFactory.decodeResource(resources, R.drawable.simple_bullet);
        bitmap = Bitmap.createScaledBitmap(bitmap,
                screenSize[0]/Constants.SCALE_RATIO_NUM_X_SIMPLE_BULLIT,
                screenSize[1]/Constants.SCALE_RATIO_NUM_Y_SIMPLE_BULLIT, false);

    }



    public float getCurX() {
        return CurX;
    }

    public void setCurX(int curX) {
        CurX = curX;
    }

    public float getCurY() {
        return CurY;
    }

    public void setCurY(int curY) {
        CurY = curY;
    }

    public float getSpeed() {
        return Speed;
    }

    public void setSpeed(int speed) {
        Speed = speed;
    }
}
