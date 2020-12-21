package com.thetechnoobs.moterskillgame.asteriodgame;

import android.content.Context;

import com.thetechnoobs.moterskillgame.weapons.AssaultRifle;
import com.thetechnoobs.moterskillgame.weapons.BasicGun;

public class GunBulletThreads {
    SpawnRegularBulletThread spawnRegularBulletThread;
    AssaultRifleShootThread assaultRifleShootThread;

    GunBulletThreads(AsteroidGameView asteroidGameView, Context context) {
        spawnRegularBulletThread = new SpawnRegularBulletThread(asteroidGameView, context);
        assaultRifleShootThread = new AssaultRifleShootThread(asteroidGameView, context);

    }

    public void startBasicGunShot() {
        Thread basicShoot = new Thread(spawnRegularBulletThread);
        basicShoot.start();
    }

    public void startAssultRifleGunShot() {
        Thread assaultRifleShoot = new Thread(assaultRifleShootThread);
        assaultRifleShoot.start();
    }

}

class AssaultRifleShootThread implements Runnable {
    AsteroidGameView asteroidGameView;
    Context context;
    boolean running = false;

    AssaultRifleShootThread(AsteroidGameView asteroidGameView, Context context) {
        this.asteroidGameView = asteroidGameView;
        this.context = context;
    }

    @Override
    public void run() {
        AssaultRifle assaultRifle = new AssaultRifle(context);

        while (asteroidGameView.getShouldShoot()) {
            running = true;
            burstFire();

            if (asteroidGameView.getAmmoLeft() == 0) {
                try {
                    asteroidGameView.asteroidAudioThread.startAssaultRifleReloadSound();
                    Thread.sleep(3000);
                    asteroidGameView.setAmmoLeft(assaultRifle.getMaxAmmo());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                try {
                    Thread.sleep(assaultRifle.getFireRate());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        running = false;
    }

    private void burstFire() {
        asteroidGameView.spawnAssaultBullet();

        try {
            Thread.sleep(100);
        } catch (Exception e) {
            e.printStackTrace();
        }

        asteroidGameView.spawnAssaultBullet();

        try {
            Thread.sleep(100);
        } catch (Exception e) {
            e.printStackTrace();
        }

        asteroidGameView.spawnAssaultBullet();
    }
}


class SpawnRegularBulletThread implements Runnable {
    AsteroidGameView asteroidGameView;
    Context context;
    boolean running = false;

    SpawnRegularBulletThread(AsteroidGameView asteroidGameView, Context context) {
        this.asteroidGameView = asteroidGameView;
        this.context = context;
    }

    @Override
    public void run() {
        BasicGun basicGun = new BasicGun(context);
        while (asteroidGameView.getShouldShoot()) {
            running = true;
            asteroidGameView.spawnRegularBullet();

            if (asteroidGameView.getAmmoLeft() == 0) {
                try {
                    asteroidGameView.asteroidAudioThread.startBasicGunReload();
                    Thread.sleep(3500);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                asteroidGameView.setAmmoLeft(basicGun.getMaxAmmo());
            } else {
                try {
                    Thread.sleep(basicGun.getFireRate());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        running = false;
    }
}
