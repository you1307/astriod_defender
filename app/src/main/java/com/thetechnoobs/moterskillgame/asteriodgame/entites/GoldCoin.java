package com.thetechnoobs.moterskillgame.asteriodgame.entites;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.RectF;

import com.thetechnoobs.moterskillgame.R;
import com.thetechnoobs.moterskillgame.asteriodgame.Constants;

public class GoldCoin {
    public Bitmap GoldCoinBitmap;
    int[] screenSize;
    int Curx, CurY, speed;
    UserCharecter userCharecter;
    Canvas canvas;

    public GoldCoin(UserCharecter userCharecter, Canvas canvas, int[] screenSize, Resources resources, int speed, int CurX, int CurY) {
        this.Curx = CurX;
        this.CurY = CurY;
        this.screenSize = screenSize;
        this.userCharecter = userCharecter;
        this.canvas = canvas;
        this.speed = speed;


        GoldCoinBitmap = BitmapFactory.decodeResource(resources, R.drawable.gold_coin);
        GoldCoinBitmap = Bitmap.createScaledBitmap(GoldCoinBitmap,
                screenSize[0] / Constants.SCALE_RATIO_NUM_X_GOLD_COIN,
                screenSize[1] / Constants.SCALE_RATIO_NUM_Y_GOLD_COIN,
                false);

    }

    public RectF getHitbox() {
        return new RectF(getCurx(), getCurY(), getCurx() + GoldCoinBitmap.getWidth(), getCurY() + GoldCoinBitmap.getHeight());
    }

    public void draw() {
        canvas.drawBitmap(this.GoldCoinBitmap, getCurx(), getCurY(), null);
    }

    public int getCurx() {
        return Curx;
    }

    public void setCurx(int curx) {
        Curx = curx;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public int getCurY() {
        return CurY;
    }

    public void setCurY(int curY) {
        CurY = curY;
    }


}
