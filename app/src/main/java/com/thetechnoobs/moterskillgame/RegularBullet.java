package com.thetechnoobs.moterskillgame;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.RectF;

public class RegularBullet {
    int CurX = 0, CurY = 0, Speed = 2;
    Bitmap bitmap;
    RectF Hitbox;

    public RegularBullet(Resources resources, int speed, int curX, int curY) {
        CurX = curX;
        CurY = curY;

        //bitmap = BitmapFactory.decodeResource(resources, );//TODO get bullet asset

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
        return Speed;
    }

    public void setSpeed(int speed) {
        Speed = speed;
    }
}
