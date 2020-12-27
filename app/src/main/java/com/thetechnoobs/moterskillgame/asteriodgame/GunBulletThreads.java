package com.thetechnoobs.moterskillgame.asteriodgame;

import android.content.Context;

import com.thetechnoobs.moterskillgame.weapons.AssaultRifle;
import com.thetechnoobs.moterskillgame.weapons.BasicGun;
import com.thetechnoobs.moterskillgame.weapons.RayGun;
import com.thetechnoobs.moterskillgame.weapons.ShotGun;

public class GunBulletThreads {
    SpawnRegularBulletThread spawnRegularBulletThread;
    AssaultRifleShootThread assaultRifleShootThread;
    ShotGunShootThread shotGunShootThread;
    RayGunShootThread rayGunShootThread;

    GunBulletThreads(AsteroidGameView asteroidGameView, Context context) {
        spawnRegularBulletThread = new SpawnRegularBulletThread(asteroidGameView, context);
        assaultRifleShootThread = new AssaultRifleShootThread(asteroidGameView, context);
        shotGunShootThread = new ShotGunShootThread(asteroidGameView, context);
        rayGunShootThread = new RayGunShootThread(asteroidGameView, context);
    }

    public void startBasicGunShot() {
        Thread basicShoot = new Thread(spawnRegularBulletThread);
        basicShoot.start();
    }

    public void startAssultRifleGunShot() {
        Thread assaultRifleShoot = new Thread(assaultRifleShootThread);
        assaultRifleShoot.start();
    }

    public void startShotGunShot() {
        Thread shotGunShoot = new Thread(shotGunShootThread);
        shotGunShoot.start();
    }

    public void startRayGunShot() {
        Thread rayGunShot = new Thread(rayGunShootThread);
        rayGunShot.start();
    }

}

class ShotGunShootThread implements Runnable {
    AsteroidGameView asteroidGameView;
    Context context;
    boolean running = false;

    public ShotGunShootThread(AsteroidGameView asteroidGameView, Context context) {
        this.asteroidGameView = asteroidGameView;
        this.context = context;
    }

    @Override
    public void run() {
        ShotGun shotGun = new ShotGun(context);
        while (asteroidGameView.getShouldShoot()) {
            running = true;
            asteroidGameView.spawnShotGunBulletSet();

            try {
                Thread.sleep(shotGun.getFireRate());
            } catch (InterruptedException e) {
                return;
            }

            if (asteroidGameView.getAmmoLeft() == 0) {
                asteroidGameView.asteroidAudioThread.startShotgunReloadSound();
                try {
                    Thread.sleep(2000);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                asteroidGameView.setAmmoLeft(shotGun.getMaxAmmo());
            }
        }
        running = false;
    }
}

class RayGunShootThread implements Runnable {
    AsteroidGameView asteroidGameView;
    Context context;
    boolean running = false;

    public RayGunShootThread(AsteroidGameView asteroidGameView, Context context) {
        this.asteroidGameView = asteroidGameView;
        this.context = context;
    }

    @Override
    public void run() {
        RayGun rayGun = new RayGun(context);

        while (asteroidGameView.getShouldShoot()) {
            running = true;

            if (asteroidGameView.getAmmoLeft() == 0) {
                try{
                    asteroidGameView.rayGunBeam = null;
                    Thread.sleep(2000);
                }catch (InterruptedException e){
                    e.printStackTrace();
                }

                asteroidGameView.setAmmoLeft(rayGun.getMaxAmmo());
            } else {

                if (asteroidGameView.rayGunBeam == null) {
                    asteroidGameView.spawnRayBeam(true);
                }else{
                    asteroidGameView.setAmmoLeft(asteroidGameView.getAmmoLeft()-1);
                }


                try{
                    Thread.sleep(50);
                }catch (InterruptedException e){
                    e.printStackTrace();
                }
            }

        }
        asteroidGameView.spawnRayBeam(false);
        running = false;
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
