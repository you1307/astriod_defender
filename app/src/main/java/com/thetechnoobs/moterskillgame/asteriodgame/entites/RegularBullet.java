package com.thetechnoobs.moterskillgame.asteriodgame.entites;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.RectF;

import com.thetechnoobs.moterskillgame.R;
import com.thetechnoobs.moterskillgame.asteriodgame.Constants;

public class RegularBullet {
    float CurX = 0, CurY = 0, Speed = 2;
    int[] screenSize = {0, 0};
    public Bitmap bitmap;
    RectF Hitbox;

    public RegularBullet(Resources resources, int speed, int[] screenSize , float curX, float curY) {
        CurX = curX;
        CurY = curY;
        this.Speed = speed;
        this.screenSize = screenSize;

        bitmap = BitmapFactory.decodeResource(resources, R.drawable.simple_bullet);
        bitmap = Bitmap.createScaledBitmap(bitmap,
                screenSize[0]/ Constants.SCALE_RATIO_NUM_X_SIMPLE_BULLIT,
                screenSize[1]/Constants.SCALE_RATIO_NUM_Y_SIMPLE_BULLIT, false);

    }


    public RectF getHitbox() {
        return new RectF(CurX, CurY, CurX+bitmap.getWidth(), CurY+bitmap.getHeight());
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
