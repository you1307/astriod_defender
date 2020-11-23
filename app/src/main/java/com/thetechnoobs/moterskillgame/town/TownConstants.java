package com.thetechnoobs.moterskillgame.town;

import android.content.res.Resources;
import android.util.DisplayMetrics;

public class TownConstants {

    public static final int USER_SPEED = (int) convertDpToPixel(6);

    public static final int GOLD_COIN_UI_SCALE_X =30;
    public static final int GOLD_COIN_UI_SCALE_Y = GOLD_COIN_UI_SCALE_X/2;

    public static final int DOLLAR_SIGN_UI_SCALE_X = 40;
    public static final int DOLLAR_SIGN_UI_SCALE_Y = DOLLAR_SIGN_UI_SCALE_X/3;


    public static float convertPixelsToDp(float px){
        DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();
        float dp = px / (metrics.densityDpi / 160f);
        return Math.round(dp);
    }

    public static float convertDpToPixel(float dp){
        DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();
        float px = dp * (metrics.densityDpi / 160f);
        return Math.round(px);
    }
}



