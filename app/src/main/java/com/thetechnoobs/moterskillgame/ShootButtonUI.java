package com.thetechnoobs.moterskillgame;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.RectF;

public class ShootButtonUI {
    int x = 0, y = 0;
    boolean side;
    int[] screenSize = {0, 0};
    Bitmap bitmap;

    public ShootButtonUI(int x, int y, boolean side, int[] screenSize, Resources resources) {
        this.x = x;
        this.y = y;
        this.side = side;
        this.screenSize = screenSize;

        bitmap = BitmapFactory.decodeResource(resources, R.drawable.shoot_button);
        bitmap = Bitmap.createScaledBitmap(bitmap,
                screenSize[0] / Constants.SCALE_RATIO_NUM_X_FOREGROUND,
                screenSize[1] / Constants.SCALE_RATIO_NUM_Y_FOREGROUND,
                false);


    }

    public ShootButtonUI(Resources resources, int[] screenSize) {
        bitmap = BitmapFactory.decodeResource(resources, R.drawable.shoot_button);
        bitmap = Bitmap.createScaledBitmap(bitmap,
                screenSize[0] / Constants.SCALE_RATIO_NUM_X_BUTTONS,
                screenSize[1] / Constants.SCALE_RATIO_NUM_Y_BUTTONS,
                false);
    }

    public RectF getCollitionBox() {
        return new RectF(x, y, x + bitmap.getWidth(), y + bitmap.getHeight());
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }
}
