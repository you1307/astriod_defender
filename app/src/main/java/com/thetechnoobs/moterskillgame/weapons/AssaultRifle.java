package com.thetechnoobs.moterskillgame.weapons;

import android.content.Context;
import android.content.SharedPreferences;

public class AssaultRifle {
    Context context;

    public AssaultRifle(Context context) {
        this.context = context;

    }

    public int getNextLevelCost() {
        return getlvl() * 130;
    }

    public int getBuyPrice() {
        return 200;
    }

    public void setLvl(int lvl) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("AssaultRifle", Context.MODE_PRIVATE);
        SharedPreferences.Editor prefE = sharedPreferences.edit();

        prefE.putInt("lvl", lvl);
        prefE.apply();
    }

    public int getlvl() {
        SharedPreferences sharedPreferences = context.getSharedPreferences("AssaultRifle", Context.MODE_PRIVATE);
        return sharedPreferences.getInt("lvl", 1);
    }

    public int getDamage() {
        SharedPreferences sharedPreferences = context.getSharedPreferences("AssaultRifle", Context.MODE_PRIVATE);
        return sharedPreferences.getInt("damage", 2);
    }

    public void setDamage(int damage) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("AssaultRifle", Context.MODE_PRIVATE);
        SharedPreferences.Editor prefE = sharedPreferences.edit();

        prefE.putInt("damage", damage);
        prefE.apply();
    }

    public int getFireRate() {
        SharedPreferences sharedPreferences = context.getSharedPreferences("AssaultRifle", Context.MODE_PRIVATE);
        return sharedPreferences.getInt("fireRate", 400);
    }

    public void setFireRate(int fireRate) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("AssaultRifle", Context.MODE_PRIVATE);
        SharedPreferences.Editor prefE = sharedPreferences.edit();

        prefE.putInt("fireRate", fireRate);
        prefE.apply();
    }

    public int getNumberOfProjectiles() {
        SharedPreferences sharedPreferences = context.getSharedPreferences("AssaultRifle", Context.MODE_PRIVATE);
        return sharedPreferences.getInt("projectiles", 1);
    }

    public void setNumberOfProjectiles(int projectiles) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("AssaultRifle", Context.MODE_PRIVATE);
        SharedPreferences.Editor prefE = sharedPreferences.edit();

        prefE.putInt("projectiles", projectiles);
        prefE.apply();
    }

    public void setPurchased(Boolean isPurchased) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("AssaultRifle", Context.MODE_PRIVATE);
        SharedPreferences.Editor prefE = sharedPreferences.edit();

        prefE.putBoolean("purchased", isPurchased);
        prefE.apply();
    }

    public Boolean isPurchased() {
        SharedPreferences sharedPreferences = context.getSharedPreferences("AssaultRifle", Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean("purchased", false);
    }
}
