package com.thetechnoobs.moterskillgame;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class HeathHeart {
    Bitmap emptyHeartBitmap, fullHeartBitmap;
    int xLoc, yLoc;

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
}
