package com.thetechnoobs.moterskillgame.asteriodgame.entites;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.RectF;
import android.util.DisplayMetrics;

import com.thetechnoobs.moterskillgame.R;

public class RayGunBeam {
    public Bitmap bitmap;
    Context context;
    int x, y;

    public RayGunBeam(Context context, Resources resources, int x, int y) {
        this.context = context;
        this.x = x;
        this.y = y;

        bitmap = BitmapFactory.decodeResource(resources, R.drawable.easy_enemy_bullet);
        bitmap = Bitmap.createScaledBitmap(bitmap, (int) convertDpToPixel(10), (int) convertDpToPixel(300), false);

    }

    public static float convertPixelsToDp(float px) {
        DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();
        return Math.round(px / (metrics.densityDpi / 160f));
    }

    public static float convertDpToPixel(float dp) {
        DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();
        return Math.round(dp * (metrics.densityDpi / 160f));
    }

    public RectF getHitbox() {
        return new RectF(x, y, x+bitmap.getWidth(), y+bitmap.getHeight());
        //return new RectF(x+convertDpToPixel(-10), y+convertDpToPixel(-170), x+convertDpToPixel(10), y);
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
