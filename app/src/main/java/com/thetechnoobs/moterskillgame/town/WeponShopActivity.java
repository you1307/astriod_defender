package com.thetechnoobs.moterskillgame.town;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.thetechnoobs.moterskillgame.R;
import com.thetechnoobs.moterskillgame.UserInventory;
import com.thetechnoobs.moterskillgame.weapons.AssaultRifle;
import com.thetechnoobs.moterskillgame.weapons.BasicGun;
import com.thetechnoobs.moterskillgame.weapons.RayGun;
import com.thetechnoobs.moterskillgame.weapons.ShotGun;

public class WeponShopActivity extends AppCompatActivity {
    ImageView BasicGunIMG, AssaultRifleIMG, ShotGunIMG, RayGunIMG;
    TextView GunNameTXT, GunDescriptionTXT, GunDamageTXT, GunFireRateTXT, projectileTXT, UserGoldTXT, UserMoneyTXT;
    Button BuyUpgradeBTN;
    UserInventory userInventory = new UserInventory(this);
    RayGun rayGun = new RayGun(this);
    ShotGun shotGun = new ShotGun(this);
    AssaultRifle assaultRifle = new AssaultRifle(this);
    BasicGun basicGun = new BasicGun(this);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wepon_shop);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        settup();
    }

    private void settup() {
        BasicGunIMG = findViewById(R.id.BasicGunIMG);
        AssaultRifleIMG = findViewById(R.id.AssaltRifleIMG);
        ShotGunIMG = findViewById(R.id.ShotGunIMG);
        RayGunIMG = findViewById(R.id.RayGunIMG);

        //data views
        GunNameTXT = findViewById(R.id.GunNameTXT);
        GunDescriptionTXT = findViewById(R.id.gunShortDiscTXT);
        GunDamageTXT = findViewById(R.id.damageTXT);
        GunFireRateTXT = findViewById(R.id.RateOfFireTXT);
        projectileTXT = findViewById(R.id.projectileTXT);
        UserGoldTXT = findViewById(R.id.UserGoldTXT);
        UserMoneyTXT = findViewById(R.id.UserMoneyTXT);

        BuyUpgradeBTN = findViewById(R.id.BuyUpgradeBTN);


        LoadEquippedWeapon();
        LoadUserCurrency();
    }

    private void LoadEquippedWeapon() {
        //0 = none, 1 = basic gun, 2 = assault rifle, 3 = shot gun, 4 = ray gun
        SharedPreferences sharedPreferences = getSharedPreferences("inventory", MODE_PRIVATE);
        int weapon = sharedPreferences.getInt("equippedWeapon", 1);

        switch (weapon) {
            case 1:
                loadBasicGunData();
                break;
            case 2:
                loadAssaultRifleData();
                break;
            case 3:
                loadShotGunData();
                break;
            case 4:
                loadRayGunData();
                break;
        }
    }

    private void loadRayGunData() {
        RayGun rayGun = new RayGun(getApplicationContext());

        GunNameTXT.setText(getString(R.string.ray_gun_title));
        GunDescriptionTXT.setText(getString(R.string.ray_gun_description));
        GunDamageTXT.setText(String.valueOf(rayGun.getDamage()));
        GunFireRateTXT.setText(String.valueOf(rayGun.getFireRate()));
        projectileTXT.setText(String.valueOf(rayGun.getNumberOfProjectiles()));
        setBoarderOn(4);

        if (rayGun.isPurchased()) {
            userInventory.setEquippedWeapon(4);
            BuyUpgradeBTN.setText(R.string.upgrade_btn);
        }else{
            BuyUpgradeBTN.setText(R.string.buy_button);
        }
    }

    private void loadShotGunData() {
        ShotGun shotGun = new ShotGun(getApplicationContext());

        GunNameTXT.setText(getString(R.string.shot_gun_title));
        GunDescriptionTXT.setText(getString(R.string.shot_gun_description));
        GunDamageTXT.setText(String.valueOf(shotGun.getDamage()));
        GunFireRateTXT.setText(String.valueOf(shotGun.getFireRate()));
        projectileTXT.setText(String.valueOf(shotGun.getNumberOfProjectiles()));
        setBoarderOn(3);

        if (shotGun.isPurchased()) {
            userInventory.setEquippedWeapon(3);
            BuyUpgradeBTN.setText(R.string.upgrade_btn);
        }else{
            BuyUpgradeBTN.setText(R.string.buy_button);
        }
    }

    private void loadAssaultRifleData() {
        AssaultRifle assaultRifle = new AssaultRifle(getApplicationContext());

        GunNameTXT.setText(getString(R.string.assault_rifle_title));
        GunDescriptionTXT.setText(getString(R.string.assault_rifle_description));
        GunDamageTXT.setText(String.valueOf(assaultRifle.getDamage()));
        GunFireRateTXT.setText(String.valueOf(assaultRifle.getFireRate()));
        projectileTXT.setText(String.valueOf(assaultRifle.getNumberOfProjectiles()));
        setBoarderOn(2);

        if (assaultRifle.isPurchased()) {
            userInventory.setEquippedWeapon(2);
            BuyUpgradeBTN.setText(R.string.upgrade_btn);
        }else{
            BuyUpgradeBTN.setText(R.string.buy_button);
        }
    }

    private void loadBasicGunData() {
        BasicGun basicGun = new BasicGun(getApplicationContext());

        GunNameTXT.setText(getString(R.string.basic_gun_title));
        GunDescriptionTXT.setText(getString(R.string.basic_gun_description));
        GunDamageTXT.setText(String.valueOf(basicGun.getDamage()));
        GunFireRateTXT.setText(String.valueOf(basicGun.getFireRate()));
        projectileTXT.setText(String.valueOf(basicGun.getNumberOfProjectiles()));
        setBoarderOn(1);

        userInventory.setEquippedWeapon(1);
        BuyUpgradeBTN.setText(R.string.upgrade_btn);

    }

    private void setBoarderOn(int weapon) {
        //0 = none, 1 = basic gun, 2 = assault rifle, 3 = shot gun, 4 = ray gun
        switch (weapon) {
            case 1:
                BasicGunIMG.setBackground(ContextCompat.getDrawable(this, R.drawable.bordered_background));
                AssaultRifleIMG.setBackground(null);
                ShotGunIMG.setBackground(null);
                RayGunIMG.setBackground(null);
                break;
            case 2:
                AssaultRifleIMG.setBackground(ContextCompat.getDrawable(this, R.drawable.bordered_background));
                BasicGunIMG.setBackground(null);
                ShotGunIMG.setBackground(null);
                RayGunIMG.setBackground(null);
                break;
            case 3:
                ShotGunIMG.setBackground(ContextCompat.getDrawable(this, R.drawable.bordered_background));
                AssaultRifleIMG.setBackground(null);
                BasicGunIMG.setBackground(null);
                RayGunIMG.setBackground(null);
                break;
            case 4:
                RayGunIMG.setBackground(ContextCompat.getDrawable(this, R.drawable.bordered_background));
                AssaultRifleIMG.setBackground(null);
                ShotGunIMG.setBackground(null);
                BasicGunIMG.setBackground(null);
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent GoToTown = new Intent(this, TownActivity.class);
        startActivity(GoToTown);
        finish();
    }

    public void BuyUpgradeBTNPressed(View v){

    }

    public void gunPressed(View view) {//TODO finish up section
        int id = view.getId();

        if (id == BasicGunIMG.getId()) {
            loadBasicGunData();
        } else if (id == AssaultRifleIMG.getId()) {
            loadAssaultRifleData();
        } else if (id == ShotGunIMG.getId()) {
            loadShotGunData();
        } else if (id == RayGunIMG.getId()) {
            loadRayGunData();
        }
    }



    private void LoadUserCurrency() {
        UserMoneyTXT.setText(String.valueOf(userInventory.getMoney()));
        UserGoldTXT.setText(String.valueOf(userInventory.getGoldCoins()));
    }
}