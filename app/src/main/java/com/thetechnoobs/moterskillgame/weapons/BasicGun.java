package com.thetechnoobs.moterskillgame.weapons;

import android.content.Context;
import android.content.SharedPreferences;

public class BasicGun {
    Context context;

    public BasicGun(Context context){
        this.context = context;
    }

    public int getDamage() {
        SharedPreferences sharedPreferences = context.getSharedPreferences("BasicGun", Context.MODE_PRIVATE);
        return sharedPreferences.getInt("damage", 1);
    }

    public void setDamage(int damage) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("BasicGun", Context.MODE_PRIVATE);
        SharedPreferences.Editor prefE = sharedPreferences.edit();

        prefE.putInt("damage", damage);
        prefE.apply();
    }

    public void setNumberOfProjectiles(int projectiles){
        SharedPreferences sharedPreferences = context.getSharedPreferences("BasicGun", Context.MODE_PRIVATE);
        SharedPreferences.Editor prefE = sharedPreferences.edit();

        prefE.putInt("projectiles", projectiles);
        prefE.apply();
    }

    public int getNumberOfProjectiles(){
        SharedPreferences sharedPreferences = context.getSharedPreferences("BasicGun", Context.MODE_PRIVATE);
        return sharedPreferences.getInt("projectiles", 1);
    }

    public int getFireRate() {
        SharedPreferences sharedPreferences = context.getSharedPreferences("BasicGun", Context.MODE_PRIVATE);
        return sharedPreferences.getInt("fireRate", 2);
    }

    public void setFireRate(int fireRate) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("BasicGun", Context.MODE_PRIVATE);
        SharedPreferences.Editor prefE = sharedPreferences.edit();

        prefE.putInt("fireRate", fireRate);
        prefE.apply();
    }
}
