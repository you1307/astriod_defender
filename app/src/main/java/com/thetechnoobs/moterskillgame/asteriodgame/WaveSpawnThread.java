package com.thetechnoobs.moterskillgame.asteriodgame;

import android.content.Context;
import android.util.Log;

import com.thetechnoobs.moterskillgame.UserData;

public class WaveSpawnThread extends Thread {
    AsteroidGameView asteroidGameView;
    int numberOfWavesCompleted;
    UserData userData;
    int numOfEnenmyToSpawn;
    int numOfAsteriodsToSpawn;
    SpawnEasyEnemy spawnEasyEnemy;
    SpawnAsteroid spawnAsteroid;
    Thread enemyThread, asteroidThread;
    private boolean goToEndScreen = true;

    public WaveSpawnThread(AsteroidGameView asteroidGameView, Context context) {
        this.asteroidGameView = asteroidGameView;
        userData = new UserData(context);
        numberOfWavesCompleted = userData.getCurrentWaveCount();

        spawnEasyEnemy = new SpawnEasyEnemy(getAmountOfEnemyToSpawn(), asteroidGameView, numberOfWavesCompleted);
        spawnAsteroid = new SpawnAsteroid(getAmountOfAsteroidToSpawn(), asteroidGameView, numberOfWavesCompleted);

        enemyThread = new Thread(spawnEasyEnemy);
        asteroidThread = new Thread(spawnAsteroid);


        Log.v("testing", "wave: "+ userData.getCurrentWaveCount());
    }

    private int getAmountOfEnemyToSpawn() {
        return (int) (numberOfWavesCompleted++);
    }

    private int getAmountOfAsteroidToSpawn() {
        int increaseCount = 1;

        if (numberOfWavesCompleted > 14) {
            increaseCount = 4;
        }

        return numberOfWavesCompleted * increaseCount;
    }

    @Override
    public void run() {
        enemyThread.start();
        asteroidThread.start();
        waveDone();
    }


    private void waveDone() {
        if (goToEndScreen) {
            asteroidGameView.waveDone();
        }
    }

    public void stopAll() {
        goToEndScreen = false;
        spawnAsteroid.stopThread();
        spawnEasyEnemy.stopThread();

        try{
            spawnAsteroid.join();
            spawnEasyEnemy.join();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}


class SpawnAsteroid extends Thread {
    int totalAmountToSpawn;
    int curWave;
    AsteroidGameView asteroidGameView;
    boolean run = true;

    SpawnAsteroid(int amount, AsteroidGameView asteroidGameView, int curWave) {
        totalAmountToSpawn = amount;
        this.curWave = curWave;
        this.asteroidGameView = asteroidGameView;
    }

    @Override
    public void run() {
        while (totalAmountToSpawn > 0 && run) {
            if (asteroidGameView.EasyEnemy.size() < 5) {
                asteroidGameView.spawnAsteroids(1);
                totalAmountToSpawn -= 1;
            }

            try {
                Thread.sleep(500);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void stopThread() {
        run = false;
    }
}


class SpawnEasyEnemy extends Thread {
    int totalAmountToSpawn;
    int curWave;
    boolean run = true;
    AsteroidGameView asteroidGameView;

    SpawnEasyEnemy(int amount, AsteroidGameView asteroidGameView, int curWave) {
        totalAmountToSpawn = amount;
        this.curWave = curWave;
        this.asteroidGameView = asteroidGameView;
    }

    @Override
    public void run() {
        while (totalAmountToSpawn > 0 && run) {
            if (asteroidGameView.EasyEnemy.size() < 4) {
                asteroidGameView.spawnEasyEnemy(1);
                totalAmountToSpawn -= 1;
            }

            try {
                Thread.sleep(1000);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void stopThread() {
        run = false;
    }
}
