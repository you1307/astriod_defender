package com.thetechnoobs.moterskillgame.weapons;

import android.content.Context;
import android.content.SharedPreferences;

public class ShotGun {
    Context context;

    public ShotGun(Context context) {
        this.context = context;
    }

    public int getDamage() {
        SharedPreferences sharedPreferences = context.getSharedPreferences("ShotGun", Context.MODE_PRIVATE);
        return sharedPreferences.getInt("damage", 3);
    }

    public void setLvl(int lvl){
        SharedPreferences sharedPreferences = context.getSharedPreferences("ShotGun", Context.MODE_PRIVATE);
        SharedPreferences.Editor prefE = sharedPreferences.edit();

        prefE.putInt("lvl", lvl);
        prefE.apply();
    }

    public int getlvl(){
        SharedPreferences sharedPreferences = context.getSharedPreferences("ShotGun", Context.MODE_PRIVATE);
        return sharedPreferences.getInt("lvl", 1);
    }

    public void setDamage(int damage) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("ShotGun", Context.MODE_PRIVATE);
        SharedPreferences.Editor prefE = sharedPreferences.edit();

        prefE.putInt("damage", damage);
        prefE.apply();
    }

    public int getMaxAmmo(){
        SharedPreferences sharedPreferences = context.getSharedPreferences("ShotGun", Context.MODE_PRIVATE);
        return sharedPreferences.getInt("maxAmmo", 2);
    }

    public void setNumberOfProjectiles(int projectiles){
        SharedPreferences sharedPreferences = context.getSharedPreferences("ShotGun", Context.MODE_PRIVATE);
        SharedPreferences.Editor prefE = sharedPreferences.edit();

        prefE.putInt("projectiles", projectiles);
        prefE.apply();
    }

    public int getNumberOfProjectiles(){
        SharedPreferences sharedPreferences = context.getSharedPreferences("ShotGun", Context.MODE_PRIVATE);
        return sharedPreferences.getInt("projectiles", 4);
    }

    public int getFireRate() {
        SharedPreferences sharedPreferences = context.getSharedPreferences("ShotGun", Context.MODE_PRIVATE);
        return sharedPreferences.getInt("fireRate", 100);
    }

    public void setFireRate(int fireRate) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("ShotGun", Context.MODE_PRIVATE);
        SharedPreferences.Editor prefE = sharedPreferences.edit();

        prefE.putInt("fireRate", fireRate);
        prefE.apply();
    }

    public void setPurchased(Boolean isPurchased){
        SharedPreferences sharedPreferences = context.getSharedPreferences("ShotGun", Context.MODE_PRIVATE);
        SharedPreferences.Editor prefE = sharedPreferences.edit();

        prefE.putBoolean("purchased", isPurchased);
        prefE.apply();
    }

    public Boolean isPurchased(){
        SharedPreferences sharedPreferences = context.getSharedPreferences("ShotGun", Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean("purchased", false);
    }

    public int getBuyPrice() {
        return 2000;
    }

    public int getNextLevelCost() {
        return getlvl()*65;
    }
}
