package com.thetechnoobs.moterskillgame.asteriodgame;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.thetechnoobs.moterskillgame.R;
import com.thetechnoobs.moterskillgame.UserData;
import com.thetechnoobs.moterskillgame.UserInventory;
import com.thetechnoobs.moterskillgame.weapons.AssaultRifle;
import com.thetechnoobs.moterskillgame.weapons.BasicGun;
import com.thetechnoobs.moterskillgame.weapons.RayGun;
import com.thetechnoobs.moterskillgame.weapons.ShotGun;

public class WeponShopActivity extends AppCompatActivity {
    ImageView BasicGunIMG, AssaultRifleIMG, ShotGunIMG, RayGunIMG;
    TextView GunNameTXT, GunDescriptionTXT, GunDamageTXT, GunFireRateTXT, projectileTXT, UserGoldTXT, UserMoneyTXT, gunlevelTXT;
    ImageView goldCoinIMG;
    Button BuyUpgradeBTN, GoToGameBtn;
    private HorizontalScrollView gunScrollView;
    int GunInView = 1;//0 = none, 1 = basic gun, 2 = assault rifle, 3 = shot gun, 4 = ray gun
    UserInventory userInventory = new UserInventory(this);
    RayGun rayGun = new RayGun(this);
    ShotGun shotGun = new ShotGun(this);
    AssaultRifle assaultRifle = new AssaultRifle(this);
    BasicGun basicGun = new BasicGun(this);
    UserData userData = new UserData(this);

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
        goldCoinIMG = findViewById(R.id.goldCoinIMG);

        //data views
        GunNameTXT = findViewById(R.id.GunNameTXT);
        GunDescriptionTXT = findViewById(R.id.gunShortDiscTXT);
        GunDamageTXT = findViewById(R.id.damageTXT);
        GunFireRateTXT = findViewById(R.id.RateOfFireTXT);
        projectileTXT = findViewById(R.id.projectileTXT);
        UserGoldTXT = findViewById(R.id.UserGoldTXT);
        UserMoneyTXT = findViewById(R.id.UserMoneyTXT);
        gunlevelTXT = findViewById(R.id.levelTXT);

        BuyUpgradeBTN = findViewById(R.id.BuyUpgradeBTN);
        GoToGameBtn = findViewById(R.id.GoToGameBTN);

        gunScrollView = findViewById(R.id.horizontalScrollView);

        loadData();
        setScrollViewLocation();
    }

    public void goToGame(View v) {
        startActivity(new Intent(this, AsteroidGameActivity.class));
        finish();
    }

    private void loadData() {
        loadImgs();
        loadEquippedWeapon();
        loadUserCurrency();
        //loadLanguageData();

        GoToGameBtn.setText(getResources().getString(R.string.go_to_wave, userData.getCurrentWaveCount()));
    }

    private void setScrollViewLocation() {
            //0 = none, 1 = basic gun, 2 = assault rifle, 3 = shot gun, 4 = ray gun
            switch (userInventory.getEquippedWeapon()) {
                case 2:
                    gunScrollView.post(() -> gunScrollView.smoothScrollTo(AssaultRifleIMG.getRight(),0));
                    break;
                case 4:
                    gunScrollView.post(() -> gunScrollView.smoothScrollTo(RayGunIMG.getMaxWidth(),0));
                    break;
            }
    }

    @SuppressLint("SetTextI18n")
    private void loadImgs() {

        if (assaultRifle.isPurchased()) {
            AssaultRifleIMG.setImageResource(R.drawable.assalt_rifle);
        }

        if (shotGun.isPurchased()) {
            ShotGunIMG.setImageResource(R.drawable.shot_gun);
        }

        if(rayGun.isPurchased()){
            RayGunIMG.setImageResource(R.drawable.ray_gun);
        }

    }

    private void loadEquippedWeapon() {
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
        GunNameTXT.setText(getString(R.string.ray_gun_title));
        GunDescriptionTXT.setText(getString(R.string.ray_gun_description));
        GunDamageTXT.setText(String.valueOf(rayGun.getDamage()));
        GunFireRateTXT.setText(String.valueOf(rayGun.getFireRate()));
        projectileTXT.setText(String.valueOf(rayGun.getNumberOfProjectiles()));
        gunlevelTXT.setText(String.valueOf(rayGun.getlvl()));
        setBoarderOn(4);
        GunInView = 4;

        if (rayGun.isPurchased()) {
            userInventory.setEquippedWeapon(4);
            goldCoinIMG.setImageResource(R.drawable.gold_coin);
            RayGunIMG.setImageResource(R.drawable.ray_gun);

            if(rayGun.getlvl() == 10){
                goldCoinIMG.setVisibility(View.GONE);
                BuyUpgradeBTN.setText(R.string.max_level);
            }else{
                goldCoinIMG.setVisibility(View.VISIBLE);
                BuyUpgradeBTN.setText(String.valueOf(rayGun.getNextLevelCost()));
            }
        } else {
            goldCoinIMG.setVisibility(View.VISIBLE);
            goldCoinIMG.setImageResource(R.drawable.dollar_sign_scaled);
            BuyUpgradeBTN.setText(String.valueOf(rayGun.getBuyPrice()));
            RayGunIMG.setImageResource(R.drawable.ray_gun_locked);
        }
    }

    private void loadShotGunData() {
        GunNameTXT.setText(getString(R.string.shot_gun_title));
        GunDescriptionTXT.setText(getString(R.string.shot_gun_description));
        GunDamageTXT.setText(String.valueOf(shotGun.getDamage()));
        GunFireRateTXT.setText(String.valueOf(shotGun.getFireRate()));
        projectileTXT.setText(String.valueOf(shotGun.getNumberOfProjectiles()));
        gunlevelTXT.setText(String.valueOf(shotGun.getlvl()));
        setBoarderOn(3);
        GunInView = 3;

        if (shotGun.isPurchased()) {
            userInventory.setEquippedWeapon(3);
            goldCoinIMG.setImageResource(R.drawable.gold_coin);

            if(shotGun.getlvl() == 10){
                goldCoinIMG.setVisibility(View.GONE);
                BuyUpgradeBTN.setText(R.string.max_level);
            }else{
                goldCoinIMG.setVisibility(View.VISIBLE);
                BuyUpgradeBTN.setText(String.valueOf(shotGun.getNextLevelCost()));
            }
        } else {
            goldCoinIMG.setVisibility(View.VISIBLE);
            goldCoinIMG.setImageResource(R.drawable.dollar_sign_scaled);
            BuyUpgradeBTN.setText(String.valueOf(shotGun.getBuyPrice()));
            ShotGunIMG.setImageResource(R.drawable.shot_gun_locked);
        }
    }

    @SuppressLint("SetTextI18n")
    private void loadAssaultRifleData() {
        GunNameTXT.setText(getString(R.string.assault_rifle_title));
        GunDescriptionTXT.setText(getString(R.string.assault_rifle_description));
        GunDamageTXT.setText(String.valueOf(assaultRifle.getDamage()));
        GunFireRateTXT.setText(String.valueOf(assaultRifle.getFireRate()));
        projectileTXT.setText(String.valueOf(assaultRifle.getNumberOfProjectiles()));
        gunlevelTXT.setText(String.valueOf(assaultRifle.getlvl()));
        setBoarderOn(2);
        GunInView = 2;


        if (assaultRifle.isPurchased()) {
            userInventory.setEquippedWeapon(2);
            BuyUpgradeBTN.setText(String.valueOf(assaultRifle.getNextLevelCost()));
            AssaultRifleIMG.setImageResource(R.drawable.assalt_rifle);

            if (assaultRifle.getlvl() == 10) {
                goldCoinIMG.setVisibility(View.GONE);
                BuyUpgradeBTN.setText(R.string.max_level);
            } else {
                goldCoinIMG.setVisibility(View.VISIBLE);
                goldCoinIMG.setImageResource(R.drawable.gold_coin);
            }
        } else {
            goldCoinIMG.setVisibility(View.VISIBLE);
            BuyUpgradeBTN.setText(String.valueOf(assaultRifle.getBuyPrice()));
            AssaultRifleIMG.setImageResource(R.drawable.assalt_rifle_locked);
            goldCoinIMG.setImageResource(R.drawable.dollar_sign_scaled);
        }
    }

    private void loadBasicGunData() {
        GunNameTXT.setText(getString(R.string.basic_gun_title));
        GunDescriptionTXT.setText(getString(R.string.basic_gun_description));
        GunDamageTXT.setText(String.valueOf(basicGun.getDamage()));
        GunFireRateTXT.setText(String.valueOf(basicGun.getFireRate()));
        projectileTXT.setText(String.valueOf(basicGun.getNumberOfProjectiles()));
        gunlevelTXT.setText(String.valueOf(basicGun.getlvl()));
        setBoarderOn(1);
        GunInView = 1;

        userInventory.setEquippedWeapon(1);
        goldCoinIMG.setImageResource(R.drawable.gold_coin);

        if (basicGun.getlvl() == 10) {
            BuyUpgradeBTN.setText(R.string.max_level);
            goldCoinIMG.setVisibility(View.GONE);
        } else {
            BuyUpgradeBTN.setText(String.valueOf(basicGun.getNextLevelCost()));
            goldCoinIMG.setVisibility(View.VISIBLE);
        }

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
        Intent GoToTown = new Intent(this, AsteroidGameActivity.class);
        startActivity(GoToTown);
        finish();
    }

    public void BuyUpgradeBTNPressed(View v) {
        switch (GunInView) {
            //0 = none, 1 = basic gun, 2 = assault rifle, 3 = shot gun, 4 = ray gun
            case 1:
                if (basicGun.getlvl() == 10) {
                    Toast.makeText(this, "Gun Max Level", Toast.LENGTH_SHORT).show();
                } else {
                    upgradeBasicGun();
                }
                break;
            case 2:
                if (assaultRifle.isPurchased()) {
                    upgradeAssaultRifle();
                } else {
                    purchaseAssaultRifle();
                }
                break;
            case 3:
                if(shotGun.isPurchased()){
                    upgradeShotGun();
                }else{
                    purchaseShotGun();
                }
                break;
            case 4:
                if(rayGun.isPurchased()){
                    upgradeRayGun();
                }else{
                    purchaseRayGun();
                }
                break;
        }

        loadData();
    }

    private void upgradeRayGun() {
        if (rayGun.getlvl() == 10) {
            Toast.makeText(this, "Ray Gun is max level", Toast.LENGTH_SHORT).show();
        } else {
            if (userInventory.getGoldCoins() >= rayGun.getNextLevelCost()) {
                userInventory.removeGoldCoins(rayGun.getNextLevelCost());
                rayGun.setLvl(rayGun.getlvl() + 1);

                rayGun.setMaxAmmo(rayGun.getMaxAmmo()+100);

            } else {
                Toast.makeText(this, "Not enough money", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void purchaseRayGun() {
        if (userInventory.getMoney() >= rayGun.getBuyPrice()) {
            userInventory.removeMoney(rayGun.getBuyPrice());
            userInventory.setEquippedWeapon(4);
            rayGun.setPurchased(true);

            loadData();
        } else {
            Toast.makeText(this, "Not enough money", Toast.LENGTH_SHORT).show();
        }
    }

    private void upgradeShotGun() {
        if (shotGun.getlvl() == 10) {
            Toast.makeText(this, "Gun is max level", Toast.LENGTH_SHORT).show();
        } else {
            if (userInventory.getGoldCoins() >= shotGun.getNextLevelCost()) {
                userInventory.removeGoldCoins(shotGun.getNextLevelCost());
                shotGun.setLvl(shotGun.getlvl() + 1);

                if(isEven(shotGun.getlvl())){
                    shotGun.setNumberOfProjectiles(shotGun.getNumberOfProjectiles()+1);
                }

                if (shotGun.getlvl() == 5) {
                    shotGun.setDamage(shotGun.getDamage() + 1);
                } else if (shotGun.getlvl() == 9) {
                    shotGun.setDamage(shotGun.getDamage() + 2);
                }
            } else {
                Toast.makeText(this, "Not enough money", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private boolean isEven(int num) {
        if(num % 2 == 0){
            return true;
        }else{
            return false;
        }
    }

    private void purchaseShotGun() {
        if (userInventory.getMoney() >= shotGun.getBuyPrice()) {
            userInventory.removeMoney(shotGun.getBuyPrice());
            userInventory.setEquippedWeapon(3);
            shotGun.setPurchased(true);

            loadData();
        } else {
            Toast.makeText(this, "Not enough money", Toast.LENGTH_SHORT).show();
        }
    }

    private void upgradeAssaultRifle() {

        if (assaultRifle.getlvl() == 10) {
            Toast.makeText(this, "Gun is max level", Toast.LENGTH_SHORT).show();
        } else {
            if (userInventory.getGoldCoins() >= assaultRifle.getNextLevelCost()) {
                userInventory.removeGoldCoins(assaultRifle.getNextLevelCost());

                if (assaultRifle.getlvl() == 5) {
                    assaultRifle.setDamage(assaultRifle.getDamage() + 1);
                } else if (assaultRifle.getlvl() == 9) {
                    assaultRifle.setDamage(assaultRifle.getDamage() + 1);
                }

                assaultRifle.setFireRate(assaultRifle.getFireRate() - 40);
                assaultRifle.setLvl(assaultRifle.getlvl() + 1);

            } else {
                Toast.makeText(this, "Not enough money", Toast.LENGTH_SHORT).show();
            }
        }

    }

    private void upgradeBasicGun() {
        if (userInventory.getGoldCoins() >= basicGun.getNextLevelCost()) {
            userInventory.removeGoldCoins(basicGun.getNextLevelCost());

            if (basicGun.getlvl() == 10) {//add damage
                Toast.makeText(this, "Gun is max level", Toast.LENGTH_SHORT).show();
            } else {
                if (basicGun.getlvl() == 5) {//add damage
                    basicGun.setDamage(basicGun.getDamage() + 1);
                } else if (basicGun.getlvl() == 9) {
                    basicGun.setDamage(basicGun.getDamage() + 1);
                }

                basicGun.setFireRate(basicGun.getFireRate() - 50);
                basicGun.setLvl(basicGun.getlvl() + 1);
            }

        } else {
            Toast.makeText(this, "Not enough money", Toast.LENGTH_SHORT).show();
        }
    }

    private void purchaseAssaultRifle() {
        if (userInventory.getMoney() >= assaultRifle.getBuyPrice()) {
            userInventory.removeMoney(assaultRifle.getBuyPrice());
            userInventory.setEquippedWeapon(2);
            assaultRifle.setPurchased(true);

            loadData();
        } else {
            Toast.makeText(this, "Not enough money", Toast.LENGTH_SHORT).show();
        }
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

    private void loadUserCurrency() {
        UserMoneyTXT.setText(String.valueOf(userInventory.getMoney()));
        UserGoldTXT.setText(String.valueOf(userInventory.getGoldCoins()));
    }
}