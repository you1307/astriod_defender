package com.thetechnoobs.moterskillgame;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.RectF;

import java.util.Random;

public class Astriod {
    int CurX = 0, CurY = 0, Speed = 0;
    int[] screenSize = {0, 0};
    Bitmap bitmap;
    private RectF CollisionBox;


    public Astriod(int x, int y, int speed, int[] screenSize, Resources resources) {
        CurX = x;
        CurY = y;
        Speed = speed;
        this.screenSize = screenSize;

        bitmap = BitmapFactory.decodeResource(resources, R.drawable.astroid_gray);
        bitmap = rotateBitmap(bitmap, new Random().nextInt(360));
        bitmap = Bitmap.createScaledBitmap(bitmap,
                screenSize[0] / Constants.SCALE_RATIO_NUM_X_FOREGROUND,
                screenSize[1] / Constants.SCALE_RATIO_NUM_Y_FOREGROUND,
                false);

    }

    public Bitmap rotateBitmap(Bitmap original, float degrees) {
        int width = original.getWidth();
        int height = original.getHeight();

        Matrix matrix = new Matrix();
        matrix.preRotate(degrees);

        Bitmap rotatedBitmap = Bitmap.createBitmap(original, 0, 0, width, height, matrix, true);

        return rotatedBitmap;
    }

    public RectF getCollisionBox() {
        CollisionBox = new RectF(CurX, CurY, CurX + bitmap.getWidth(), CurY + bitmap.getHeight());
        return CollisionBox;
    }

    public int getSpeed() {
        return Speed;
    }

    public void setSpeed(int speed) {
        Speed = speed;
    }

    public int getCurX() {
        return CurX;
    }

    public void setCurX(int curX) {
        CurX = curX;
    }

    public int getCurY() {
        return CurY;
    }

    public void setCurY(int curY) {
        CurY = curY;
    }
}
