package com.thetechnoobs.moterskillgame.town;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.DisplayMetrics;

import com.thetechnoobs.moterskillgame.R;
import com.thetechnoobs.moterskillgame.UserInventory;

public class CurrencyUI {
    int Money, Gold;
    int CoinBitmapPosX, GoldTXTPosX, CoinBitmapPosY, GoldTXTPosY, MoneyBitmapPosX, MoneyTXTPosX, MoneyBitmapPosY, MoneyTXTPosY;
    Paint TextPaint = new Paint();
    UserInventory userInventory;
    Bitmap GoldCoinUIBitmap, MoneyUIBitmap;

    public CurrencyUI(Context context, Resources resources, int[] screenSize) {
        userInventory = new UserInventory(context);

        TextPaint.setColor(Color.WHITE);
        TextPaint.setTextAlign(Paint.Align.CENTER);
        TextPaint.setTextSize(convertDpToPixel(15));


        GoldCoinUIBitmap = BitmapFactory.decodeResource(resources, R.drawable.ui_gold);
        GoldCoinUIBitmap = Bitmap.createScaledBitmap(GoldCoinUIBitmap,
                TownConstants.GOLD_COIN_UI_SCALE_X,
                TownConstants.GOLD_COIN_UI_SCALE_Y,
                false);

        MoneyUIBitmap = BitmapFactory.decodeResource(resources, R.drawable.ui_money);
        MoneyUIBitmap = Bitmap.createScaledBitmap(MoneyUIBitmap,
                TownConstants.DOLLAR_SIGN_UI_SCALE_X,
                TownConstants.DOLLAR_SIGN_UI_SCALE_Y,
                false);

        settupUI();

    }


    private void settupUI() {
        setCoinBitmapPosX((int) convertDpToPixel(4));
        setCoinBitmapPosY((int) convertDpToPixel(4));

        setGoldTXTPosX((int) convertDpToPixel(46));
        setGoldTXTPosY((int) convertDpToPixel(22));

        setMoneyBitmapPosX((int) convertDpToPixel(135));
        setMoneyBitmapPosY((int) convertDpToPixel(4));

        setMoneyTXTPosX((int) convertDpToPixel(182));
        setMoneyTXTPosY((int) convertDpToPixel(22));
    }

    public void draw(Canvas canvas) {
        canvas.drawBitmap(GoldCoinUIBitmap, getCoinBitmapPosX(), getCoinBitmapPosY(), null);
        canvas.drawBitmap(MoneyUIBitmap, getMoneyBitmapPosX(), getMoneyBitmapPosY(), null);

        canvas.drawText(String.valueOf(getGold()), getGoldTXTPosX(), getGoldTXTPosY(), TextPaint);
        canvas.drawText(String.valueOf(getMoney()), getMoneyTXTPosX(), getMoneyTXTPosY(), TextPaint);
    }

    public int getMoney() {
        Money = userInventory.getMoney();
        return Money;
    }

    public int getGold() {
        Gold = userInventory.getGoldCoins();
        return Gold;
    }

    public static float convertPixelsToDp(float px) {
        DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();
        return Math.round(px / (metrics.densityDpi / 160f));
    }

    public static float convertDpToPixel(float dp) {
        DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();
        return Math.round( dp * (metrics.densityDpi / 160f));
    }

    public int getCoinBitmapPosX() {
        return CoinBitmapPosX;
    }

    public void setCoinBitmapPosX(int coinBitmapPosX) {
        CoinBitmapPosX = coinBitmapPosX;
    }

    public int getGoldTXTPosX() {
        return GoldTXTPosX;
    }

    public void setGoldTXTPosX(int goldTXTPosX) {
        GoldTXTPosX = goldTXTPosX;
    }

    public int getCoinBitmapPosY() {
        return CoinBitmapPosY;
    }

    public void setCoinBitmapPosY(int coinBitmapPosY) {
        CoinBitmapPosY = coinBitmapPosY;
    }

    public int getGoldTXTPosY() {
        return GoldTXTPosY;
    }

    public void setGoldTXTPosY(int goldTXTPosY) {
        GoldTXTPosY = goldTXTPosY;
    }

    public int getMoneyBitmapPosX() {
        return MoneyBitmapPosX;
    }

    public void setMoneyBitmapPosX(int moneyBitmapPosX) {
        MoneyBitmapPosX = moneyBitmapPosX;
    }

    public int getMoneyTXTPosX() {
        return MoneyTXTPosX;
    }

    public void setMoneyTXTPosX(int moneyTXTPosX) {
        MoneyTXTPosX = moneyTXTPosX;
    }

    public int getMoneyBitmapPosY() {
        return MoneyBitmapPosY;
    }

    public void setMoneyBitmapPosY(int moneyBitmapPosY) {
        MoneyBitmapPosY = moneyBitmapPosY;
    }

    public int getMoneyTXTPosY() {
        return MoneyTXTPosY;
    }

    public void setMoneyTXTPosY(int moneyTXTPosY) {
        MoneyTXTPosY = moneyTXTPosY;
    }
}
