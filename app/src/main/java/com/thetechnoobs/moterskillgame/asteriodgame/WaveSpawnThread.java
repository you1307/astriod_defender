package com.thetechnoobs.moterskillgame.asteriodgame;

import android.content.Context;

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

    public WaveSpawnThread(AsteroidGameView asteroidGameView, Context context) {
        this.asteroidGameView = asteroidGameView;
        userData = new UserData(context);
        numberOfWavesCompleted = userData.getCurrentWaveCount();

        spawnEasyEnemy = new SpawnEasyEnemy(getAmountOfEnemyToSpawn(), asteroidGameView, numberOfWavesCompleted);
        spawnAsteroid = new SpawnAsteroid(getAmountOfAsteroidToSpawn(), asteroidGameView, numberOfWavesCompleted);

        enemyThread = new Thread(spawnEasyEnemy);
        asteroidThread = new Thread(spawnAsteroid);

    }

    private int getAmountOfEnemyToSpawn() {
        return (int) (numberOfWavesCompleted * 1.5);
    }

    private int getAmountOfAsteroidToSpawn() {
        return numberOfWavesCompleted * 5;
    }

    @Override
    public void run() {
        enemyThread.start();
        asteroidThread.start();
        waveDone();
    }


    private void waveDone() {
        asteroidGameView.waveDone();
    }

    private void delay(int milli) {
        try {
            Thread.sleep(milli);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}


class SpawnAsteroid extends Thread {
    int totalAmountToSpawn;
    int curWave;
    AsteroidGameView asteroidGameView;

    SpawnAsteroid(int amount, AsteroidGameView asteroidGameView, int curWave) {
        totalAmountToSpawn = amount;
        this.curWave = curWave;
        this.asteroidGameView = asteroidGameView;
    }

    @Override
    public void run() {

        while (totalAmountToSpawn > 0) {
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
}


class SpawnEasyEnemy extends Thread {
    int totalAmountToSpawn;
    int curWave;
    AsteroidGameView asteroidGameView;

    SpawnEasyEnemy(int amount, AsteroidGameView asteroidGameView, int curWave) {
        totalAmountToSpawn = amount;
        this.curWave = curWave;
        this.asteroidGameView = asteroidGameView;
    }

    @Override
    public void run() {

        while (totalAmountToSpawn > 0) {
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
}
