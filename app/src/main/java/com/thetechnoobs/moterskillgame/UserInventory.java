package com.thetechnoobs.moterskillgame;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.List;

public class UserInventory {
    Context context;

    public UserInventory(Context context){
        this.context = context;
    }

    //0 = none, 1 = basic gun, 2 = assault rifle, 3 = shot gun, 4 = ray gun
    public int getEquippedWeapon() {
        SharedPreferences sharedPreferences = context.getSharedPreferences("inventory", Context.MODE_PRIVATE);
        return sharedPreferences.getInt("equippedWeapon", 1);
    }


    //0 = none, 1 = basic gun, 2 = assault rifle, 3 = shot gun, 4 = ray gun
    public void setEquippedWeapon(int weapon){
        SharedPreferences sharedPreferences = context.getSharedPreferences("inventory", Context.MODE_PRIVATE);
        SharedPreferences.Editor prefE = sharedPreferences.edit();

        prefE.putInt("equippedWeapon", weapon);
        prefE.apply();
    }

    public int getGoldCoins() {
        SharedPreferences pref = context.getSharedPreferences("inventory", Context.MODE_PRIVATE);
        return pref.getInt("goldCoins", 0);
    }

    public void removeGoldCoins(int Amount){
        SharedPreferences pref = context.getSharedPreferences("inventory", Context.MODE_PRIVATE);
        SharedPreferences.Editor prefE = pref.edit();

        int totalCoins = getGoldCoins()-Amount;

        prefE.putInt("goldCoins", totalCoins);
        prefE.apply();
    }

    public void addGoldCoins(int NewGoldCoins) {
        SharedPreferences pref = context.getSharedPreferences("inventory", Context.MODE_PRIVATE);
        SharedPreferences.Editor prefE = pref.edit();

        int totalCoins = getGoldCoins()+NewGoldCoins;

        prefE.putInt("goldCoins", totalCoins);
        prefE.apply();
    }

    public int getMoney() {
        SharedPreferences pref = context.getSharedPreferences("inventory", Context.MODE_PRIVATE);
        return pref.getInt("money", 0);
    }

    public void removeMoney(int Amount){
        SharedPreferences pref = context.getSharedPreferences("inventory", Context.MODE_PRIVATE);
        SharedPreferences.Editor prefE = pref.edit();

        int totalMoney = getMoney()-Amount;

        prefE.putInt("money", totalMoney);
        prefE.apply();
    }

    public void addMoney(int NewMoney) {
        SharedPreferences pref = context.getSharedPreferences("inventory", Context.MODE_PRIVATE);
        SharedPreferences.Editor prefE = pref.edit();

        int totalMoney = getMoney()+NewMoney;

        prefE.putInt("money", totalMoney);
        prefE.apply();
    }

    public int getKills() {
        SharedPreferences pref = context.getSharedPreferences("inventory", Context.MODE_PRIVATE);
        return pref.getInt("kills", 0);
    }

    public void addKills(int Newkills) {
        SharedPreferences pref = context.getSharedPreferences("inventory", Context.MODE_PRIVATE);
        SharedPreferences.Editor prefE = pref.edit();

        int totalKills = getKills()+Newkills;

        prefE.putInt("kills", totalKills);
        prefE.apply();
    }
}
