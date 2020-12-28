package com.thetechnoobs.moterskillgame;

import android.content.Context;
import android.content.SharedPreferences;

import java.sql.Struct;

public class UserData {
    Context context;

    public UserData(Context context) {
        this.context = context;
    }

    public int getCurrentWaveCount(){
        SharedPreferences sharedPreferences = context.getSharedPreferences("AsteroidGameData", Context.MODE_PRIVATE);
        return sharedPreferences.getInt("WaveCount", 1);
    }

    public void addOneToCurrentWaveCount(){
        SharedPreferences sharedPreferences = context.getSharedPreferences("AsteroidGameData", Context.MODE_PRIVATE);
        SharedPreferences.Editor prefE = sharedPreferences.edit();

        prefE.putInt("WaveCount", getCurrentWaveCount()+1);
        prefE.apply();
    }

    public void subtractOneFromWaveCount(){
        SharedPreferences sharedPreferences = context.getSharedPreferences("AsteroidGameData", Context.MODE_PRIVATE);
        SharedPreferences.Editor prefE = sharedPreferences.edit();

        prefE.putInt("WaveCount", getCurrentWaveCount()-1);
        prefE.apply();
    }

    public void saveNewPlayer(boolean isNew){
        SharedPreferences sharedPreferences = context.getSharedPreferences("AsteroidGameData", Context.MODE_PRIVATE);
        SharedPreferences.Editor prefE = sharedPreferences.edit();

        prefE.putBoolean("newUser", isNew);
        prefE.apply();
    }

    public boolean isNewPLayer() {
        SharedPreferences sharedPreferences = context.getSharedPreferences("AsteroidGameData", Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean("newUser", true);
    }
}
