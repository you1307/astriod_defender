package com.thetechnoobs.moterskillgame;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.util.DisplayMetrics;

public class BackendSettings {


    public static float convertDpToPixel(float dp) {
        DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();
        return Math.round(dp * (metrics.densityDpi / 160f));
    }

    public int[] getSavedShootButtonLoc(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("Settings", Context.MODE_PRIVATE);
        int xLoc = sharedPreferences.getInt("ShootButtonSideX", (int) convertDpToPixel(360));
        int yLoc = sharedPreferences.getInt("ShootButtonSideY", (int) convertDpToPixel(720));

        return new int[]{xLoc, yLoc};
    }

    public int[] getSavedPauseButtonLoc(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("Settings", Context.MODE_PRIVATE);
        int xLoc = sharedPreferences.getInt("PauseButtonX", (int) convertDpToPixel(320));
        int yLoc = sharedPreferences.getInt("PauseButtonY", (int) convertDpToPixel(20));

        return new int[]{xLoc, yLoc};
    }

    public int[] getSavedHealthHeartLoc(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("Settings", Context.MODE_PRIVATE);
        int xLoc = sharedPreferences.getInt("HeathBarX", (int) convertDpToPixel(10));
        int yLoc = sharedPreferences.getInt("HealthBarY", (int) convertDpToPixel(30));
        return new int[]{xLoc, yLoc};
    }

    public int[] getSavedAmmoLoc(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("Settings", Context.MODE_PRIVATE);
        int xLoc = sharedPreferences.getInt("AmmoTextX", (int) convertDpToPixel(10));
        int yLoc = sharedPreferences.getInt("AmmoTextY", (int) convertDpToPixel(20));

        return new int[]{xLoc, yLoc};
    }

    public int getCurLanguage(Context context){
        //0 = en, 1 = hindi, 2 = spanish
        SharedPreferences sharedPreferences = context.getSharedPreferences("Settings", Context.MODE_PRIVATE);
        return sharedPreferences.getInt("language", 0);
    }
}
