package com.thetechnoobs.moterskillgame.asteriodgame.ui;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.thetechnoobs.moterskillgame.R;
import com.thetechnoobs.moterskillgame.asteriodgame.Constants;

public class Explosion {
    public int x, y;
    public Bitmap bitmap;
    int[] screenSize;

    public Explosion(int eventx, int eventy, int[] screenSize, Resources resources) {
        this.x = eventx;
        this.y = eventy;
        this.screenSize = screenSize;

        bitmap = BitmapFactory.decodeResource(resources, R.drawable.cartoon_explosion);
        bitmap = Bitmap.createScaledBitmap(bitmap,
                screenSize[0] / Constants.SCALE_RATIO_NUM_X_FOREGROUND,
                screenSize[1] / Constants.SCALE_RATIO_NUM_Y_FOREGROUND,
                false);
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }
}
