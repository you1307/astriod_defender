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
        int shots = 0;

        while (asteroidGameView.getShouldShoot()) {
            running = true;
            burstFire();
            shots += 3;

            if (shots == 33) {
                shots = 0;
                try {
                    asteroidGameView.asteroidAudioThread.assaultRifleReloadSound.run();
                    Thread.sleep(3000);
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
        int shots = 0;
        while (asteroidGameView.getShouldShoot()) {
            running = true;
            shots++;
            asteroidGameView.spawnRegularBullet();
            asteroidGameView.setAmmoLeft(basicGun.getMaxAmmo() - shots);

            if (shots == 6) {
                shots = 0;
                try {
                    asteroidGameView.asteroidAudioThread.basicGunReloadSound.run();
                    Thread.sleep(3500);
                } catch (Exception e) {
                    e.printStackTrace();
                }
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
