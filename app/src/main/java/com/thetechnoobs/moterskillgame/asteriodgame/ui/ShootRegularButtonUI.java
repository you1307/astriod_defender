package com.thetechnoobs.moterskillgame.asteriodgame.ui;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.RectF;
import android.util.DisplayMetrics;

import com.thetechnoobs.moterskillgame.R;

public class ShootRegularButtonUI {
    public Bitmap bitmap;
    int x = 0, y = 0;
    boolean side;
    int[] screenSize = {0, 0};

    public ShootRegularButtonUI(int x, int y, int[] screenSize, Resources resources) {
        this.x = x;
        this.y = y;
        this.side = side;
        this.screenSize = screenSize;

        bitmap = BitmapFactory.decodeResource(resources, R.drawable.shoot_button);
        bitmap = Bitmap.createScaledBitmap(bitmap, (int) convertDpToPixel(55), (int) convertDpToPixel(55), false);
    }

    public static float convertDpToPixel(float dp) {
        DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();
        return Math.round(dp * (metrics.densityDpi / 160f));
    }

    public RectF getCollitionBox() {
        return new RectF(x - bitmap.getWidth(), y - bitmap.getHeight(), x, y);
    }

    public int getX() {
        return x - bitmap.getWidth();
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y - bitmap.getHeight();
    }

    public void setY(int y) {
        this.y = y;
    }
}
