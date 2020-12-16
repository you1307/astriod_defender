package com.thetechnoobs.moterskillgame.weapons;

import android.content.Context;
import android.content.SharedPreferences;

public class BasicGun {
    Context context;

    public BasicGun(Context context) {
        this.context = context;
    }

    public int getNextLevelCost() {
        return getlvl() * 2;
    }

    public void setLvl(int lvl) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("BasicGun", Context.MODE_PRIVATE);
        SharedPreferences.Editor prefE = sharedPreferences.edit();

        prefE.putInt("lvl", lvl);
        prefE.apply();
    }

    public int getlvl() {
        SharedPreferences sharedPreferences = context.getSharedPreferences("BasicGun", Context.MODE_PRIVATE);
        return sharedPreferences.getInt("lvl", 1);
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

    public void setMaxAmmo(int newMaxAmmo){
        SharedPreferences sharedPreferences = context.getSharedPreferences("BasicGun", Context.MODE_PRIVATE);
        SharedPreferences.Editor prefE = sharedPreferences.edit();

        prefE.putInt("maxAmmo", newMaxAmmo);
        prefE.apply();
    }

    public int getMaxAmmo(){
        SharedPreferences sharedPreferences = context.getSharedPreferences("BasicGun", Context.MODE_PRIVATE);
        return sharedPreferences.getInt("maxAmmo", 6);
    }

    public int getNumberOfProjectiles() {
        SharedPreferences sharedPreferences = context.getSharedPreferences("BasicGun", Context.MODE_PRIVATE);
        return sharedPreferences.getInt("projectiles", 1);
    }

    public void setNumberOfProjectiles(int projectiles) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("BasicGun", Context.MODE_PRIVATE);
        SharedPreferences.Editor prefE = sharedPreferences.edit();

        prefE.putInt("projectiles", projectiles);
        prefE.apply();
    }

    public int getFireRate() {
        SharedPreferences sharedPreferences = context.getSharedPreferences("BasicGun", Context.MODE_PRIVATE);
        return sharedPreferences.getInt("fireRate", 800);
    }

    public void setFireRate(int fireRate) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("BasicGun", Context.MODE_PRIVATE);
        SharedPreferences.Editor prefE = sharedPreferences.edit();

        prefE.putInt("fireRate", fireRate);
        prefE.apply();
    }
}
