package com.thetechnoobs.moterskillgame.town;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.util.DisplayMetrics;

import com.thetechnoobs.moterskillgame.R;

import java.util.ArrayList;
import java.util.List;

public class ButtonUI {
    Bitmap RightBitmap, LeftBitmap, UpBitmap, DownBitmap, shopBtn, shopBtnPushed;
    int UpX, UpY, DownX, DownY, LeftX, LeftY, RightX, RightY;
    int ShopBtnPosX, ShopBtnPosY;
    int[] screenSize;
    int shopToGoTo = 1;
    boolean drawShopBtnUI = false;

    public ButtonUI(Resources resources, int[] screenSize) {
        this.screenSize = screenSize;

        RightBitmap = BitmapFactory.decodeResource(resources, R.drawable.arrow);
        LeftBitmap = rotateBitmap(RightBitmap, 180);
        DownBitmap = rotateBitmap(RightBitmap, 90);
        UpBitmap = rotateBitmap(RightBitmap, -90);

        shopBtn = BitmapFactory.decodeResource(resources, R.drawable.coverscreen_button_shop);
        shopBtn = Bitmap.createScaledBitmap(shopBtn, (int) convertDpToPixel(70), (int) convertDpToPixel(35), false);
        shopBtnPushed = BitmapFactory.decodeResource(resources, R.drawable.coverscreen_button_shop_pressed);
        shopBtnPushed = Bitmap.createScaledBitmap(shopBtnPushed, (int) convertDpToPixel(70), (int) convertDpToPixel(35), false);

        SetArrowPos();
        setShopBtnPos();

    }

    public static float convertPixelsToDp(float px) {
        DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();
        return Math.round(px / (metrics.densityDpi / 160f));
    }

    public static float convertDpToPixel(float dp) {
        DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();
        return Math.round(dp * (metrics.densityDpi / 160f));
    }

    private void setShopBtnPos() {
        setShopBtnPosX((int) convertDpToPixel(635));
        setShopBtnPosY((int) convertDpToPixel(315));
    }

    public void draw(Canvas canvas) {
        canvas.drawBitmap(UpBitmap, getUpX(), getUpY(), null);
        canvas.drawBitmap(DownBitmap, getDownX(), getDownY(), null);
        canvas.drawBitmap(LeftBitmap, getLeftX(), getLeftY(), null);
        canvas.drawBitmap(RightBitmap, getRightX(), getRightY(), null);


        if (getShopBtnHitbox() != null) {
            canvas.drawBitmap(shopBtn, getShopBtnPosX(), getShopBtnPosY(), null);
        }

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
        RectF RightArrow = new RectF(getRightX(), getRightY(), getRightX() + RightBitmap.getWidth(), getRightY() + RightBitmap.getHeight());

        ArrowBox.add(UpArrow);
        ArrowBox.add(RightArrow);
        ArrowBox.add(DownArrow);
        ArrowBox.add(LeftArrow);


        return ArrowBox;
    }

    public RectF getShopBtnHitbox() {
        if (drawShopBtnUI) {
            return new RectF(getShopBtnPosX(), getShopBtnPosY(), getShopBtnPosX() + shopBtn.getWidth(), getShopBtnPosY() + shopBtn.getHeight());
        } else {
            return null;
        }
    }

    public void drawShopBtnUI(boolean drawShopBtnUI, int ShopToGoTo) {//ShopToGoTo 1 = chem, 2 = wepons
        this.drawShopBtnUI = drawShopBtnUI;
        this.shopToGoTo = ShopToGoTo;
    }

    public void drawShopBtnUI(boolean drawShopBtnUI) {//ShopToGoTo 1 = chem, 2 = wepons
        this.drawShopBtnUI = drawShopBtnUI;
    }

    public int getShopBtnPosX() {
        return ShopBtnPosX;
    }

    public void setShopBtnPosX(int shopBtnPosX) {
        ShopBtnPosX = shopBtnPosX;
    }

    public int getShopBtnPosY() {
        return ShopBtnPosY;
    }

    public void setShopBtnPosY(int shopBtnPosY) {
        ShopBtnPosY = shopBtnPosY;
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
