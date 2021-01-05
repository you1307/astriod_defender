package com.thetechnoobs.moterskillgame.asteriodgame.entites;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.util.DisplayMetrics;

import com.thetechnoobs.moterskillgame.R;

public class HardEnemyBullet {
    public Bitmap bitmap;
    float CurX = 0, CurY = 0;
    int speed;
    int[] screenSize = {0, 0};
    RectF Hitbox;

    public HardEnemyBullet(Resources resources, int speed, int[] screenSize, float curX, float curY) {
        CurX = curX;
        CurY = curY;
        this.speed = speed;
        screenSize = screenSize;

        bitmap = BitmapFactory.decodeResource(resources, R.drawable.hard_enemy_bomb);
        bitmap = Bitmap.createScaledBitmap(bitmap, (int) convertDpToPixel(25), (int) convertDpToPixel(13), false);

        //bitmap = RotateBitmap(bitmap, 90.0f);
    }

    public static float convertDpToPixel(float dp) {
        DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();
        return Math.round(dp * (metrics.densityDpi / 160f));
    }

    public static Bitmap RotateBitmap(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
    }

    public RectF getHitbox() {
        return new RectF(getCurX(), getCurY(), getCurX() + bitmap.getWidth(), getCurY() + bitmap.getHeight());
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

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }
}
