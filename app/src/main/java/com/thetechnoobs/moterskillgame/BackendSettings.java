package com.thetechnoobs.moterskillgame;

import android.content.Context;
import android.content.SharedPreferences;

public class BackendSettings {

    public boolean getSavedShootButtonSide(Context context) {
        boolean shootButonSide; //false is right side, true is left side

        SharedPreferences sharedPreferences = context.getSharedPreferences("Settings", Context.MODE_PRIVATE);
        shootButonSide = sharedPreferences.getBoolean("ShootButtonSide", false);

        return shootButonSide;
    }

}
