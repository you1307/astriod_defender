package com.thetechnoobs.moterskillgame.town;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;

import com.thetechnoobs.moterskillgame.R;
import com.thetechnoobs.moterskillgame.UserInventory;

public class CurrencyUI {
    int Money, Gold;
    int CoinBitmapPosX, GoldTXTPosX, CoinBitmapPosY, GoldTXTPosY, MoneyBitmapPosX, MoneyTXTPosX, MoneyBitmapPosY, MoneyTXTPosY;
    Paint TextPaint = new Paint();
    UserInventory userInventory;
    Bitmap GoldCoinBitmap, MoneyBitmap;

    public CurrencyUI(Context context, Resources resources, int[] screenSize) {
        userInventory = new UserInventory(context);

        TextPaint.setColor(Color.BLACK);
        TextPaint.setTextAlign(Paint.Align.CENTER);
        TextPaint.setTextSize(40);


        GoldCoinBitmap = BitmapFactory.decodeResource(resources, R.drawable.gold_coin);
        GoldCoinBitmap = Bitmap.createScaledBitmap(GoldCoinBitmap,
                screenSize[0] / TownConstants.GOLD_COIN_UI_SCALE_X,
                screenSize[1] / TownConstants.GOLD_COIN_UI_SCALE_Y,
                false);

        MoneyBitmap = BitmapFactory.decodeResource(resources, R.drawable.dollar_sign);
        MoneyBitmap = Bitmap.createScaledBitmap(MoneyBitmap,
                screenSize[0] / TownConstants.DOLLAR_SIGN_UI_SCALE_X,
                screenSize[1] / TownConstants.DOLLAR_SIGN_UI_SCALE_Y,
                false);

        settupUI(screenSize);

    }

    private void settupUI(int[] screenSize) {

        setCoinBitmapPosX(screenSize[0] / 60);
        setCoinBitmapPosY(screenSize[1] / 60);

        setGoldTXTPosX(getCoinBitmapPosX() + GoldCoinBitmap.getWidth());
        setGoldTXTPosY(getCoinBitmapPosY() + (GoldCoinBitmap.getHeight() / 2));

        setMoneyBitmapPosX(getCoinBitmapPosX());
        setMoneyBitmapPosY(getCoinBitmapPosY() + GoldCoinBitmap.getHeight());

        setMoneyTXTPosX(getMoneyBitmapPosX() + MoneyBitmap.getWidth());
        setMoneyTXTPosY(getMoneyBitmapPosY() + (MoneyBitmap.getHeight() / 2));
    }

    public void draw(Canvas canvas) {
        canvas.drawBitmap(GoldCoinBitmap, getCoinBitmapPosX(), getCoinBitmapPosY(), null);
        canvas.drawBitmap(MoneyBitmap, getMoneyBitmapPosX(), getMoneyBitmapPosY(), null);

        canvas.drawText(String.valueOf(getGold()), (float) (getGoldTXTPosX()+GoldCoinBitmap.getWidth()/1.7), (float) (getGoldTXTPosY()+GoldCoinBitmap.getHeight()/3.2), TextPaint);
        canvas.drawText(String.valueOf(getMoney()), (float) (getMoneyTXTPosX()+MoneyBitmap.getWidth()), (float) (getMoneyTXTPosY()+MoneyBitmap.getHeight()/3.2), TextPaint);
    }

    public int getMoney() {
        Money = userInventory.getMoney();
        return Money;
    }

    public int getGold() {
        Gold = userInventory.getGoldCoins();
        return Gold;
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
