package com.thetechnoobs.moterskillgame.town;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.RectF;

import com.thetechnoobs.moterskillgame.R;

import java.util.ArrayList;
import java.util.List;

public class ControllerUI {
    Bitmap RightBitmap, LeftBitmap, UpBitmap, DownBitmap;
    int UpX, UpY, DownX, DownY, LeftX, LeftY, RightX, RightY;
    int[] screenSize;

    public ControllerUI(Resources resources, int[] screenSize) {
        this.screenSize = screenSize;

        RightBitmap = BitmapFactory.decodeResource(resources, R.drawable.arrow);
        LeftBitmap = rotateBitmap(RightBitmap, 180);
        DownBitmap = rotateBitmap(RightBitmap, 90);
        UpBitmap = rotateBitmap(RightBitmap, -90);

        SetArrowPos();

    }

    public void draw(Canvas canvas) {
        canvas.drawBitmap(UpBitmap, getUpX(), getUpY(), null);
        canvas.drawBitmap(DownBitmap, getDownX(), getDownY(), null);
        canvas.drawBitmap(LeftBitmap, getLeftX(), getLeftY(), null);
        canvas.drawBitmap(RightBitmap, getRightX(), getRightY(), null);
    }

    public Bitmap rotateBitmap(Bitmap original, float degrees) {
        int width = original.getWidth();
        int height = original.getHeight();

        Matrix matrix = new Matrix();
        matrix.preRotate(degrees);

        return Bitmap.createBitmap(original, 0, 0, width, height, matrix, true);
    }

    public void SetArrowPos() {
        setUpX(screenSize[0] / 12);
        setUpY((int) (screenSize[1] / 1.5));

        setDownY(getUpY() + UpBitmap.getHeight() + screenSize[1] / 30);
        setDownX(getUpX());

        setLeftX(getUpX() - UpBitmap.getWidth());
        setLeftY((int) (screenSize[1] / 1.35));

        setRightX((int) (screenSize[0] / 8.5));
        setRightY(getLeftY());
    }

    public List<RectF> getControllerHitBoxs() {
        List<RectF> ArrowBox = new ArrayList<>();

        RectF UpArrow = new RectF(getUpX(), getUpY(), getUpX() + UpBitmap.getWidth(), getUpY() + UpBitmap.getHeight());
        RectF DownArrow = new RectF(getDownX(), getDownY(), getDownX() + DownBitmap.getWidth(), getDownY() + DownBitmap.getHeight());
        RectF LeftArrow = new RectF(getLeftX(), getLeftY(), getLeftX() + LeftBitmap.getWidth(), getLeftY() + LeftBitmap.getHeight());
        RectF RightArrow = new RectF(getRightX(), getRightY(), getRightX()+RightBitmap.getWidth(), getRightY()+RightBitmap.getHeight());

        ArrowBox.add(UpArrow);
        ArrowBox.add(RightArrow);
        ArrowBox.add(DownArrow);
        ArrowBox.add(LeftArrow);


        return ArrowBox;
    }

    public int getUpX() {
        return UpX;
    }

    public void setUpX(int upX) {
        UpX = upX;
    }

    public int getUpY() {
        return UpY;
    }

    public void setUpY(int upY) {
        UpY = upY;
    }

    public int getDownX() {
        return DownX;
    }

    public void setDownX(int downX) {
        DownX = downX;
    }

    public int getDownY() {
        return DownY;
    }

    public void setDownY(int downY) {
        DownY = downY;
    }

    public int getLeftX() {
        return LeftX;
    }

    public void setLeftX(int leftX) {
        LeftX = leftX;
    }

    public int getLeftY() {
        return LeftY;
    }

    public void setLeftY(int leftY) {
        LeftY = leftY;
    }

    public int getRightX() {
        return RightX;
    }

    public void setRightX(int rightX) {
        RightX = rightX;
    }

    public int getRightY() {
        return RightY;
    }

    public void setRightY(int rightY) {
        RightY = rightY;
    }
}
