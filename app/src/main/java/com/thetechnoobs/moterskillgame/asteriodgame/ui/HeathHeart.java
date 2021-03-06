package com.thetechnoobs.moterskillgame.asteriodgame.ui;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.RectF;

import com.thetechnoobs.moterskillgame.R;
import com.thetechnoobs.moterskillgame.asteriodgame.Constants;

public class HeathHeart {
    public Bitmap emptyHeartBitmap, fullHeartBitmap;
    public int xLoc, yLoc;
    int speed = 5;

    public HeathHeart(Resources resources,int[] screenSize , int xLoc, int yLoc) {
        this.xLoc = xLoc;
        this.yLoc = yLoc;

        emptyHeartBitmap = BitmapFactory.decodeResource(resources, R.drawable.heart_empty);
        emptyHeartBitmap = Bitmap.createScaledBitmap(emptyHeartBitmap,
                screenSize[0] / Constants.SCALE_RATIO_NUM_X_HEATH,
                screenSize[1] / Constants.SCALE_RATIO_NUM_Y_HEATH,
                false);

        fullHeartBitmap = BitmapFactory.decodeResource(resources, R.drawable.heart_full);
        fullHeartBitmap = Bitmap.createScaledBitmap(fullHeartBitmap,
                screenSize[0] / Constants.SCALE_RATIO_NUM_X_HEATH,
                screenSize[1] / Constants.SCALE_RATIO_NUM_Y_HEATH,
                false);
    }

    public RectF getHitbox(){
        return new RectF(getxLoc(), getyLoc(), getxLoc()+fullHeartBitmap.getWidth(), getyLoc()+fullHeartBitmap.getHeight());
    }

    public int getxLoc() {
        return xLoc;
    }

    public void setxLoc(int xLoc) {
        this.xLoc = xLoc;
    }

    public int getyLoc() {
        return yLoc;
    }

    public void setyLoc(int yLoc) {
        this.yLoc = yLoc;
    }

    public void move() {
        setyLoc(getyLoc()+speed);
    }
}
