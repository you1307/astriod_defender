package com.thetechnoobs.moterskillgame.town;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.thetechnoobs.moterskillgame.R;
import com.thetechnoobs.moterskillgame.UserInventory;
import com.thetechnoobs.moterskillgame.asteriodgame.AsteroidGameActivity;
import com.thetechnoobs.moterskillgame.weapons.AssaultRifle;
import com.thetechnoobs.moterskillgame.weapons.BasicGun;
import com.thetechnoobs.moterskillgame.weapons.RayGun;
import com.thetechnoobs.moterskillgame.weapons.ShotGun;

public class WeponShopActivity extends AppCompatActivity {
    ImageView BasicGunIMG, AssaultRifleIMG, ShotGunIMG, RayGunIMG;
    TextView GunNameTXT, GunDescriptionTXT, GunDamageTXT, GunFireRateTXT, projectileTXT, UserGoldTXT, UserMoneyTXT, gunlevelTXT;
    TextView assualtRiflePriceTXT, shotGunPriceTXT;
    ImageView goldCoinIMG;
    Button BuyUpgradeBTN;
    int GunInView = 1;//0 = none, 1 = basic gun, 2 = assault rifle, 3 = shot gun, 4 = ray gun
    UserInventory userInventory = new UserInventory(this);
    RayGun rayGun = new RayGun(this);
    ShotGun shotGun = new ShotGun(this);
    AssaultRifle assaultRifle = new AssaultRifle(this);
    BasicGun basicGun = new BasicGun(this);

    Button test;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wepon_shop);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        settup();
    }

    private void settup() {
        test = findViewById(R.id.button);

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

        //prices text view
        assualtRiflePriceTXT = findViewById(R.id.assualtRifleBuyAmountTXT);
        shotGunPriceTXT = findViewById(R.id.ShotGunBuyAmountTXT);

        BuyUpgradeBTN = findViewById(R.id.BuyUpgradeBTN);

        loadData();
    }

    public void test(View v) {
        startActivity(new Intent(this, AsteroidGameActivity.class));//TODO remove and make game start menu
        finish();

        userInventory.addGoldCoins(9999999);
        loadData();
    }

    private void loadData() {
        LoadPrices();
        LoadEquippedWeapon();
        LoadUserCurrency();
    }

    @SuppressLint("SetTextI18n")
    private void LoadPrices() {

        if (assaultRifle.isPurchased()) {
            assualtRiflePriceTXT.setVisibility(View.INVISIBLE);
            AssaultRifleIMG.setImageResource(R.drawable.assalt_rifle);
        } else {
            assualtRiflePriceTXT.setText("$" + assaultRifle.getBuyPrice());
        }

        if (shotGun.isPurchased()) {
            shotGunPriceTXT.setVisibility(View.INVISIBLE);
            ShotGunIMG.setImageResource(R.drawable.shot_gun);
        } else {
            shotGunPriceTXT.setText("$" + shotGun.getBuyPrice());
        }

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
        gunlevelTXT.setText(String.valueOf(rayGun.getlvl()));
        setBoarderOn(4);
        GunInView = 4;

        if (rayGun.isPurchased()) {
            userInventory.setEquippedWeapon(4);
            BuyUpgradeBTN.setText(R.string.upgrade_btn);
        } else {
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
        gunlevelTXT.setText(String.valueOf(shotGun.getlvl()));
        setBoarderOn(3);
        GunInView = 3;

        if (shotGun.isPurchased()) {
            userInventory.setEquippedWeapon(3);
            BuyUpgradeBTN.setText(R.string.upgrade_btn);
        } else {
            BuyUpgradeBTN.setText(R.string.buy_button);
        }
    }

    @SuppressLint("SetTextI18n")
    private void loadAssaultRifleData() {
        AssaultRifle assaultRifle = new AssaultRifle(getApplicationContext());

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
            goldCoinIMG.setVisibility(View.VISIBLE);
        } else {
            BuyUpgradeBTN.setText(R.string.buy_button);
            AssaultRifleIMG.setImageResource(R.drawable.assalt_rifle_locked);
            assualtRiflePriceTXT.setVisibility(View.VISIBLE);
            goldCoinIMG.setVisibility(View.GONE);
        }
    }

    private void loadBasicGunData() {
        BasicGun basicGun = new BasicGun(getApplicationContext());

        GunNameTXT.setText(getString(R.string.basic_gun_title));
        GunDescriptionTXT.setText(getString(R.string.basic_gun_description));
        GunDamageTXT.setText(String.valueOf(basicGun.getDamage()));
        GunFireRateTXT.setText(String.valueOf(basicGun.getFireRate()));
        projectileTXT.setText(String.valueOf(basicGun.getNumberOfProjectiles()));
        gunlevelTXT.setText(String.valueOf(basicGun.getlvl()));
        setBoarderOn(1);
        GunInView = 1;

        userInventory.setEquippedWeapon(1);

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
        Intent GoToTown = new Intent(this, TownActivity.class);
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
                    //upgradeAssaultRifle(); TODO implement upgrade feature
                } else {
                    purchaseAssaultRifle();
                }
                break;
            case 3:

                break;
            case 4:

                break;
        }

        loadData();
    }

    private void upgradeAssaultRifle() {
        if (userInventory.getMoney() >= assaultRifle.getNextLevelCost()) {
            userInventory.removeMoney(assaultRifle.getNextLevelCost());

            if (assaultRifle.getlvl() == 9) {
                assaultRifle.setDamage(assaultRifle.getDamage() + 1);
                assaultRifle.setFireRate(assaultRifle.getFireRate() - 50);//TODO change values to something more appropriate
                assaultRifle.setLvl(assaultRifle.getlvl() + 1);
            } else {
                assaultRifle.setDamage(assaultRifle.getDamage() + 1);
                assaultRifle.setFireRate(assaultRifle.getFireRate() - 50);
                assaultRifle.setLvl(assaultRifle.getlvl() + 1);
            }

        } else {
            Toast.makeText(this, "Not enough money", Toast.LENGTH_SHORT).show();
        }
    }

    private void upgradeBasicGun() {
        if (userInventory.getGoldCoins() >= basicGun.getNextLevelCost()) {
            userInventory.removeGoldCoins(basicGun.getNextLevelCost());

            if (basicGun.getlvl() == 10) {//add damage
                Toast.makeText(this, "Gun is max level", Toast.LENGTH_SHORT).show();
            } else {
                if(basicGun.getlvl() == 5){//add damage
                    basicGun.setDamage(basicGun.getDamage() + 1);
                }else if(basicGun.getlvl() == 9){
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


    private void LoadUserCurrency() {
        UserMoneyTXT.setText(String.valueOf(userInventory.getMoney()));
        UserGoldTXT.setText(String.valueOf(userInventory.getGoldCoins()));
    }
}