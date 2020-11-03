package com.thetechnoobs.moterskillgame;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.RectF;

public class UserCharecter {
    float CurX, CurY;
    int[] screenSize;
    Bitmap bitmap;


    public UserCharecter(Resources resources, int[] screenSize, float CurX, float CurY) {
        this.screenSize = screenSize;
        this.CurX = CurX;
        this.CurY = CurY;

        bitmap = BitmapFactory.decodeResource(resources, R.drawable.spaceman);
        bitmap = Bitmap.createScaledBitmap(bitmap,
                screenSize[0] / Constants.SCALE_RATIO_NUM_X_USER,
                screenSize[1] / Constants.SCALE_RATIO_NUM_Y_USER,
                false);

    }

    public RectF getHitBox() {
        return new RectF(CurX, CurY, CurX + bitmap.getWidth(), CurY + bitmap.getHeight());
    }

    public float getCurX() {
        return CurX;
    }

    public void setCurX(float curX) {
        CurX = curX;
    }

    public float getCurY() {
        return CurY;
    }

    public void setCurY(float curY) {
        CurY = curY;
    }
}
