package com.thetechnoobs.moterskillgame.asteriodgame.entites;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.RectF;

import com.thetechnoobs.moterskillgame.R;
import com.thetechnoobs.moterskillgame.asteriodgame.Constants;

public class EasyEnemyBullet {
    float CurX = 0, CurY = 0;
    int speed;
    int[] screenSize = {0, 0};
    public Bitmap bitmap;
    RectF Hitbox;

    public EasyEnemyBullet(Resources resources, int speed, int[] screenSize , float curX, float curY){
        CurX = curX;
        CurY = curY;
        this.speed = speed;
        screenSize = screenSize;

        bitmap = BitmapFactory.decodeResource(resources, R.drawable.easy_enemy_bullet);
        bitmap = Bitmap.createScaledBitmap(bitmap,
                screenSize[0]/ Constants.SCALE_RATIO_NUM_X_EASY_ENEMY_BULLET,
                screenSize[0]/Constants.SCALE_RATIO_NUM_Y_EASY_ENEMY_BULLET,
                false);

        bitmap = RotateBitmap(bitmap, 90.0f);
    }

    public RectF getHitbox() {
        return new RectF(getCurX(), getCurY(), getCurX()+bitmap.getWidth(), getCurY()+bitmap.getHeight());
    }

    public static Bitmap RotateBitmap(Bitmap source, float angle)
    {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
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
