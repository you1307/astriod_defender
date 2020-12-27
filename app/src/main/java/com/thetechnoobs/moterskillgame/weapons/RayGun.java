package com.thetechnoobs.moterskillgame.weapons;

import android.content.Context;
import android.content.SharedPreferences;

public class RayGun {
    Context context;

    public RayGun(Context context) {
        this.context = context;
    }

    public void setLvl(int lvl){
        SharedPreferences sharedPreferences = context.getSharedPreferences("RayGun", Context.MODE_PRIVATE);
        SharedPreferences.Editor prefE = sharedPreferences.edit();

        prefE.putInt("lvl", lvl);
        prefE.apply();
    }

    public int getlvl(){
        SharedPreferences sharedPreferences = context.getSharedPreferences("RayGun", Context.MODE_PRIVATE);
        return sharedPreferences.getInt("lvl", 1);
    }

    public int getMaxAmmo(){
        SharedPreferences sharedPreferences = context.getSharedPreferences("ShotGun", Context.MODE_PRIVATE);
        return sharedPreferences.getInt("maxAmmo", 300);
    }
    public int getDamage() {
        SharedPreferences sharedPreferences = context.getSharedPreferences("RayGun", Context.MODE_PRIVATE);
        return sharedPreferences.getInt("damage", 50);
    }

    public void setDamage(int damage) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("RayGun", Context.MODE_PRIVATE);
        SharedPreferences.Editor prefE = sharedPreferences.edit();

        prefE.putInt("damage", damage);
        prefE.apply();
    }

    public void setNumberOfProjectiles(int projectiles){
        SharedPreferences sharedPreferences = context.getSharedPreferences("RayGun", Context.MODE_PRIVATE);
        SharedPreferences.Editor prefE = sharedPreferences.edit();

        prefE.putInt("projectiles", projectiles);
        prefE.apply();
    }

    public int getNumberOfProjectiles(){
        SharedPreferences sharedPreferences = context.getSharedPreferences("RayGun", Context.MODE_PRIVATE);
        return sharedPreferences.getInt("projectiles", 1);
    }

    public int getFireRate() {
        SharedPreferences sharedPreferences = context.getSharedPreferences("RayGun", Context.MODE_PRIVATE);
        return sharedPreferences.getInt("fireRate", 0);
    }

    public void setFireRate(int fireRate) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("RayGun", Context.MODE_PRIVATE);
        SharedPreferences.Editor prefE = sharedPreferences.edit();

        prefE.putInt("fireRate", fireRate);
        prefE.apply();
    }

    public void setPurchased(Boolean isPurchased){
        SharedPreferences sharedPreferences = context.getSharedPreferences("RayGun", Context.MODE_PRIVATE);
        SharedPreferences.Editor prefE = sharedPreferences.edit();

        prefE.putBoolean("purchased", isPurchased);
        prefE.apply();
    }

    public Boolean isPurchased(){
        SharedPreferences sharedPreferences = context.getSharedPreferences("RayGun", Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean("purchased", false);
    }

    public int getBuyPrice() {
        return 20000;
    }

    public int getNextLevelCost() {
        return getlvl()*15;
    }
}
