package com.thetechnoobs.moterskillgame.asteriodgame.entites;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.RectF;

import com.thetechnoobs.moterskillgame.R;
import com.thetechnoobs.moterskillgame.asteriodgame.Constants;

import java.util.Random;

public class ShotGunBullet {
    public Bitmap bitmap;
    float CurX = 0, CurY = 0, Speed = 2;
    int[] screenSize;
    int place;

    public ShotGunBullet(Resources resources, int speed, int place, int[] screenSize, float curX, float curY) {
        CurX = curX;
        CurY = curY;
        this.place = place;
        this.Speed = speed;
        this.screenSize = screenSize;

        bitmap = BitmapFactory.decodeResource(resources, R.drawable.shotgun_bullet);
        bitmap = Bitmap.createScaledBitmap(bitmap,
                screenSize[0] / Constants.SCALE_RATIO_NUM_X_SIMPLE_BULLIT,
                screenSize[1] / Constants.SCALE_RATIO_NUM_Y_SIMPLE_BULLIT, false);
    }

    public RectF getHitbox() {
        return new RectF(CurX, CurY, CurX + bitmap.getWidth(), CurY + bitmap.getHeight());
    }

    public void drawButtets(Canvas canvas) {
        float spread = 2f;

        if (place == 0) {
            canvas.drawBitmap(bitmap, getCurX(), getCurY(), null);
            setCurY((int) (getCurY() - getSpeed()));
        } else if (!isOdd(place)) {
            canvas.drawBitmap(bitmap, getCurX(), getCurY(), null);
            setCurX((int) (getCurX() - place * spread));
            setCurY((int) (getCurY() - getSpeed()));
        }else if(isOdd(place)){
            canvas.drawBitmap(bitmap, getCurX(), getCurY(), null);
            setCurX((int) (getCurX() + place * spread));
            setCurY((int) (getCurY() - getSpeed()));
        }
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

    public boolean isOdd(int num) {

        if (num % 2 == 0) {
            return true;
        } else
            return false;
    }

    public float getSpeed() {
        return Speed;
    }

    public void setSpeed(int speed) {
        Speed = speed;
    }
}
