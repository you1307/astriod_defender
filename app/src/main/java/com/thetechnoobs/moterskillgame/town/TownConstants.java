package com.thetechnoobs.moterskillgame.town;

import android.content.res.Resources;
import android.util.DisplayMetrics;

public class TownConstants {

    public static final int USER_SPEED = (int) convertDpToPixel(6);

    public static final int GOLD_COIN_UI_SCALE_X = (int) convertDpToPixel(160);
    public static final int GOLD_COIN_UI_SCALE_Y = (int) convertDpToPixel(35);

    public static final int DOLLAR_SIGN_UI_SCALE_X = (int) convertDpToPixel(160);
    public static final int DOLLAR_SIGN_UI_SCALE_Y = (int) convertDpToPixel(35);


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



