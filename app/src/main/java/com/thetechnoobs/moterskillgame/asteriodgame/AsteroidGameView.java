package com.thetechnoobs.moterskillgame.asteriodgame;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceView;

import com.thetechnoobs.moterskillgame.BackendSettings;
import com.thetechnoobs.moterskillgame.UserData;
import com.thetechnoobs.moterskillgame.UserInventory;
import com.thetechnoobs.moterskillgame.asteriodgame.entites.Astriod;
import com.thetechnoobs.moterskillgame.asteriodgame.entites.BadGuy;
import com.thetechnoobs.moterskillgame.asteriodgame.entites.GoldCoin;
import com.thetechnoobs.moterskillgame.asteriodgame.entites.RegularBullet;
import com.thetechnoobs.moterskillgame.asteriodgame.entites.UserCharecter;
import com.thetechnoobs.moterskillgame.asteriodgame.ui.BackgroundStar;
import com.thetechnoobs.moterskillgame.asteriodgame.ui.Explosion;
import com.thetechnoobs.moterskillgame.asteriodgame.ui.HeathHeart;
import com.thetechnoobs.moterskillgame.asteriodgame.ui.ShootRegularButtonUI;
import com.thetechnoobs.moterskillgame.weapons.BasicGun;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class AsteroidGameView extends SurfaceView implements Runnable {
    private final Paint paint, BackgroundRectPaint, scoreTextPaint;
    UserCharecter userCharecter;
    UserInventory userInventory;
    UserData userData;
    ShootRegularButtonUI shootRegularButtonUI;
    RectF backgroundRect;
    ArrayList<BackgroundStar> backgroundStars = new ArrayList<>();
    ArrayList<Astriod> astriods = new ArrayList<>();
    ArrayList<RegularBullet> bullets = new ArrayList<>();
    ArrayList<BadGuy> EasyEnemy = new ArrayList<>();
    ArrayList<GoldCoin> GoldCoins = new ArrayList<>();
    int[] screenSize = {0, 0};
    Canvas canvas;
    AsteroidAudioThread asteroidAudioThread;
    WaveSpawnThread waveSpawnThread;
    GunBulletThreads gunBulletThreads;
    boolean waveDoneSending = false;
    private Thread thread;
    private boolean isPlaying = false;
    private boolean shooting = false;
    private int ammoLeft;


    public AsteroidGameView(Context context, int screenx, int screeny) {
        super(context);
        userData = new UserData(context);

        screenSize[0] = screenx;
        screenSize[1] = screeny;

        backgroundRect = new RectF(0, 0, screenx, screeny);

        userCharecter = new UserCharecter(getResources(), screenSize, (float) screenx / 2, (float) screeny - 300);
        userInventory = new UserInventory(context);

        settupButtonUI(context);

        asteroidAudioThread = new AsteroidAudioThread(context);
        waveSpawnThread = new WaveSpawnThread(this, getContext());
        gunBulletThreads = new GunBulletThreads(this, context);

        paint = new Paint();
        paint.setColor(Color.BLUE);

        scoreTextPaint = new Paint();
        scoreTextPaint.setColor(Color.GRAY);
        scoreTextPaint.setTextSize((float) screeny / 30);

        BackgroundRectPaint = new Paint();
        BackgroundRectPaint.setColor(Color.BLACK);

        waveSpawnThread.start();

    }

    @Override
    public void run() {
        while (isPlaying) {
            draw();
            update();
            checkForWaveCompletion();
            //sleep(5);
        }
    }

    private void draw() {
        if (userCharecter.getHeath() < 1) {//check if user is dead
            gameOver();
        }

        if (getHolder().getSurface().isValid()) {
            canvas = getHolder().lockCanvas();
            canvas.drawRect(backgroundRect, BackgroundRectPaint);

            drawUI();

            drawBackgrounStars();
            drawAstroids();
            drawBullets();
            drawEasyEnemy();
            drawCoins();

            checkForCollision();//needs to be in draw for collision explosion

            canvas.drawBitmap(shootRegularButtonUI.bitmap, shootRegularButtonUI.getX(), shootRegularButtonUI.getY(), null);
            canvas.drawBitmap(userCharecter.bitmap, userCharecter.getCurX(), userCharecter.getCurY(), null);
            getHolder().unlockCanvasAndPost(canvas);
        }
    }

    private void drawUI() {
        drawUserHealth();
        drawUserScore();
        drawAmmoLeft();
    }

    private void update() {
        //update coins position
        moveCoins();

        //check for dead enemy's
        checkForDeadEnemys();

        //update Bullets position
        moveBullets();
        DespawnBullets();

        //deal with astroid and astroid hitbox stuff
        moveAstroids();
        checkAsteroidsOutOfBounds();

        //Deal with background stuff
        moveBackgroundStars();
        spawnAndDeleteBackgroundStars();
    }

    private void checkForWaveCompletion() {
        if (EasyEnemy.size() < 1 && astriods.size() < 1 && waveDoneSending && GoldCoins.size() < 1) {
            userData.addOneToCurrentWaveCount();
            gameOver();
        }
    }

    public void waveDone() {
        waveDoneSending = true;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        // get pointer index from the event object
        int pointerIndex = event.getActionIndex();

        // get pointer ID
        int pointerId = event.getPointerId(pointerIndex);

        // get masked (not specific to a pointer) action
        int maskedAction = event.getActionMasked();

        switch (maskedAction) {
            case MotionEvent.ACTION_DOWN:
                if (checkUIButtons(event.getX(), event.getY(), true)) {
                    shooting = true;
                }
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                if (checkUIButtons(event.getX(pointerIndex), event.getY(pointerIndex), true)) {
                    shooting = true;
                }

                break;
            case MotionEvent.ACTION_MOVE:
                if (!checkUIButtons(event.getX(), event.getY(), false)) {
                    updateUserPos(event.getX(), event.getY());
                }
                break;
            case MotionEvent.ACTION_UP:
                if (checkUIButtons(event.getX(), event.getY(), false)) {
                    shooting = false;
                }
                break;
            case MotionEvent.ACTION_POINTER_UP:
                if (checkUIButtons(event.getX(pointerIndex), event.getY(pointerIndex), false)) {
                    shooting = false;
                }
                break;
            case MotionEvent.ACTION_CANCEL:
                break;
        }
        invalidate();

        return true;
    }

    private boolean checkUIButtons(float x, float y, boolean actOn) {
        if (x > shootRegularButtonUI.getX() && y > shootRegularButtonUI.getY()) {
            if (actOn) {
                switch (userInventory.getEquippedWeapon()) {
                    case 1:
                        if (!gunBulletThreads.spawnRegularBulletThread.running) {
                            gunBulletThreads.startBasicGunShot();
                        }
                        break;
                    case 2:
                        if (!gunBulletThreads.assaultRifleShootThread.running) {
                            gunBulletThreads.startAssultRifleGunShot();
                        }
                        break;
                }
            }
            return true;
        }
        return false;
    }

    public boolean getShouldShoot() {
        return shooting;
    }

    public void spawnAssaultBullet() {
        bullets.add(new RegularBullet(getResources(), 50, screenSize, userCharecter.getCurX() + (float) userCharecter.bitmap.getWidth() / 2, userCharecter.getCurY()));
        asteroidAudioThread.assaultRifleShootSound.run();
    }

    public void spawnRegularBullet() {
        bullets.add(new RegularBullet(getResources(), 50, screenSize, userCharecter.getCurX() + (float) userCharecter.bitmap.getWidth() / 2, userCharecter.getCurY()));
        asteroidAudioThread.simpleShootSound.run();
    }

    public void gameOver() {
        isPlaying = false;
        Context context = getContext();
        Intent GoToEndGameScreen = new Intent(context, EndGameScreenAsteroids.class);
        GoToEndGameScreen.putExtra("score", userCharecter.getUserScore());
        GoToEndGameScreen.putExtra("gold", userCharecter.getGold());
        GoToEndGameScreen.putExtra("enemysKilled", userCharecter.getEnemysKilled());
        GoToEndGameScreen.putExtra("damageTaken", userCharecter.getDamageTaken());
        context.startActivity(GoToEndGameScreen);
        Cleanup();
    }

    private void Cleanup() {
        waveSpawnThread.stopAll();
        backgroundStars.clear();
        astriods.clear();
        bullets.clear();
        GoldCoins.clear();

        for (int e = 0; e < EasyEnemy.size(); e++) {
            EasyEnemy.get(e).cleanup();
            //EasyEnemy.remove(e);
        }

        EasyEnemy.clear();
    }

    public void setAmmoLeft(int ammoLeft) {
        this.ammoLeft = ammoLeft;
    }

    public void drawAmmoLeft(){
        Paint ammoText = new Paint();
        ammoText.setTextSize(30);
        ammoText.setColor(Color.RED);
        canvas.drawText(String.valueOf(ammoLeft), 300, 300, ammoText);
    }

    public void drawUserHealth() {
        int yOffset = screenSize[1] / 20;//needed for screens with round corners so that the heath heart doesnt get hidden
        int xOffset = screenSize[0] / 100;//added to make nice spaceing between hearts
        List<HeathHeart> heathHearts = new ArrayList<>();

        for (int i = 0; i < userCharecter.getMaxHeath(); i++) {
            HeathHeart heart = new HeathHeart(getResources(), screenSize, xOffset, yOffset);
            heathHearts.add(heart);

            if (i < userCharecter.getHeath()) {//if position i should be full heart
                if (i != 0) {//if position i is not first spot
                    heathHearts.get(i).xLoc = heathHearts.get(i).xLoc + heathHearts.get(i - 1).xLoc + heathHearts.get(i - 1).fullHeartBitmap.getWidth();
                }
                canvas.drawBitmap(heathHearts.get(i).fullHeartBitmap, heathHearts.get(i).xLoc, heathHearts.get(i).yLoc, paint);
            } else {//if position i should be empty heart
                if (i != 0) {//if position i is not first
                    heathHearts.get(i).xLoc = heathHearts.get(i).xLoc + heathHearts.get(i - 1).xLoc + heathHearts.get(i - 1).emptyHeartBitmap.getWidth();
                }
                canvas.drawBitmap(heathHearts.get(i).emptyHeartBitmap, heathHearts.get(i).xLoc, heathHearts.get(i).yLoc, paint);
            }
        }
    }

    private void drawUserScore() {
        canvas.drawText(String.valueOf(userCharecter.getUserScore()), (float) screenSize[0] / 2, (float) screenSize[1] / 20, scoreTextPaint);
    }

    private void updateUserPos(float x, float y) {
        userCharecter.setCurX((int) (x - userCharecter.bitmap.getWidth() / 2));
        userCharecter.setCurY((int) (y - userCharecter.bitmap.getHeight() / 2));
    }


    private void checkForCollision() {
        BulletHitboxDetection();

        for (int i = 0; i < astriods.size(); i++) {//check if astriod hits user
            if (astriods.get(i).getCollisionBox().intersect(userCharecter.getHitBox())) {
                explosionFrame(astriods.get(i).getCurX(), astriods.get(i).getCurY());
                astriods.remove(i);
                userCharecter.setHeath(userCharecter.getHeath() - 1);
                userCharecter.setDamageTaken(userCharecter.getDamageTaken() + 1);
                asteroidAudioThread.asteriodHitUserThread.run();
                break;
            }
        }

        //check if coins are collected
        for (int c = 0; c < GoldCoins.size(); c++) {
            if (GoldCoins.get(c).getHitbox().intersect(userCharecter.getHitBox())) {
                GoldCoins.remove(c);
                asteroidAudioThread.coinCollectSound.run();
                userCharecter.setGold(userCharecter.getGold() + 1);
            }
        }
    }

    private void moveBullets() {
        for (int i = 0; i < bullets.size(); i++) {
            bullets.get(i).setCurY((int) (bullets.get(i).getCurY() - bullets.get(i).getSpeed()));
        }
    }

    private void drawBullets() {
        for (int i = 0; i < bullets.size(); i++) {
            canvas.drawBitmap(bullets.get(i).bitmap, bullets.get(i).getCurX(), bullets.get(i).getCurY(), null);
        }
    }

    private void explosionFrame(float eventx, float eventy) {
        Explosion explosion = new Explosion((int) eventx, (int) eventy, screenSize, getResources());
        canvas.drawBitmap(explosion.bitmap, explosion.getX(), explosion.getY(), null);
    }

    private void drawBackgrounStars() {
        for (int i = 0; i < backgroundStars.size(); i++) {
            canvas.drawBitmap(backgroundStars.get(i).stareBitmap, backgroundStars.get(i).getCurX(), backgroundStars.get(i).getCurY(), paint);
        }
    }

    private void drawAstroids() {
        for (int i = 0; i < astriods.size(); i++) {
            canvas.drawBitmap(astriods.get(i).bitmap, astriods.get(i).getCurX(), astriods.get(i).getCurY(), paint);
        }
    }

    private void moveAstroids() {
        for (int i = 0; i < astriods.size(); i++) {
            astriods.get(i).setCurY(astriods.get(i).getCurY() + astriods.get(i).getSpeed());
        }
    }

    public void drawEasyEnemy() {
        for (int i = 0; i < EasyEnemy.size(); i++) {
            canvas.drawBitmap(EasyEnemy.get(i).EasyEnemyAlive, EasyEnemy.get(i).getX(), EasyEnemy.get(i).getY(), null);
            EasyEnemy.get(i).update(canvas);
        }
    }

    public void spawnEasyEnemy(int numOfEnemy) {
        for (int i = 0; i < numOfEnemy; i++) {
            BadGuy badGuy = new BadGuy(getResources(), getContext(), userCharecter, screenSize, randomNum(screenSize[0] - (screenSize[0] / Constants.SCALE_RATIO_NUM_X_EASY_ENEMY), 0), 300);
            EasyEnemy.add(badGuy);
        }
    }

    public void spawnAsteroids(int numOfAsteroids) {
        for (int i = 0; i < numOfAsteroids; i++) {
            Astriod astriod = new Astriod(randomNum(screenSize[0] - (screenSize[0] / Constants.SCALE_RATIO_NUM_X_FOREGROUND), 0), 0, randomNum(Constants.asteriodMaxSpeed, 5), screenSize, getResources());
            astriods.add(astriod);
        }
    }

    private void checkAsteroidsOutOfBounds() {
        for (int i = 0; i < astriods.size(); i++) {
            if (astriods.get(i).getCurY() > screenSize[1]) {
                astriods.remove(i);
                DeductScorePoints(1);
                break;
            }
        }
    }

    private void DeductScorePoints(int i) {
        userCharecter.setUserScore(userCharecter.getUserScore() - i);
    }

    private void spawnAndDeleteBackgroundStars() {

        for (int i = 0; i < backgroundStars.size(); i++) {
            if (backgroundStars.get(i).getCurY() > screenSize[1] - 40) {
                backgroundStars.remove(i);
            }
        }

        if (backgroundStars.size() < 10) {
            BackgroundStar backgroundStar = new BackgroundStar(randomNum(screenSize[0] - 20, 20), 0, randomNum(Constants.BACKGROUND_STAR_MAX_SPEED, 5), screenSize, getResources());
            backgroundStars.add(backgroundStar);
        }
    }

    private void moveBackgroundStars() {
        for (int i = 0; i < backgroundStars.size(); i++) {
            backgroundStars.get(i).setCurY(backgroundStars.get(i).getCurY() + backgroundStars.get(i).getSpeed());
        }
    }

    private void settupButtonUI(Context context) {
        BackendSettings backendSettings = new BackendSettings();

        //settup shoot button
        shootRegularButtonUI = new ShootRegularButtonUI(getResources(), screenSize);
        if (backendSettings.getSavedShootButtonSide(context)) {
            shootRegularButtonUI = new ShootRegularButtonUI(0, shootRegularButtonUI.bitmap.getHeight(), true, screenSize, getResources());
        } else {
            shootRegularButtonUI = new ShootRegularButtonUI(screenSize[0] - shootRegularButtonUI.bitmap.getWidth(), screenSize[1] - shootRegularButtonUI.bitmap.getHeight(), true, screenSize, getResources());
        }
    }

    private void DespawnBullets() {
        for (int i = 0; i < bullets.size(); i++) {
            if (bullets.get(i).getCurY() < 0) {
                bullets.remove(i);
                break;
            }
        }
    }

    private void BulletHitboxDetection() {

        //check for asteroid collision
        for (int a = 0; a < astriods.size(); a++) {
            boolean brake = false;
            for (int b = 0; b < bullets.size(); b++) {
                if (bullets.get(b).getHitbox().intersect(astriods.get(a).getCollisionBox())) {
                    explosionFrame(astriods.get(a).getCurX(), astriods.get(a).getCurY());
                    astriods.remove(a);
                    bullets.remove(b);
                    userCharecter.setUserScore(userCharecter.getUserScore() + 1);
                    asteroidAudioThread.simpleShootAsteriodSound.run();
                    brake = true;
                    break;
                }
            }
            if (brake) {
                break;
            }
        }

        //check for Enemy Collision
        for (int e = 0; e < EasyEnemy.size(); e++) {
            boolean brake = false;
            for (int b = 0; b < bullets.size(); b++) {
                if (bullets.get(b).getHitbox().intersect(EasyEnemy.get(e).getHitBox())) {
                    EasyEnemy.get(e).setCurHeath(EasyEnemy.get(e).getCurHeath() - new BasicGun(getContext()).getDamage());
                    bullets.remove(b);
                    asteroidAudioThread.easyEnemyHitSound.run();
                    brake = true;
                    break;
                }
            }
            if (brake) {
                break;
            }
        }

    }

    public void checkForDeadEnemys() {
        for (int i = 0; i < EasyEnemy.size(); i++) {
            if (EasyEnemy.get(i).getCurHeath() < 1) {
                SpawnCoin(EasyEnemy.get(i));
                userCharecter.setEnemysKilled(userCharecter.getEnemysKilled() + 1);
                EasyEnemy.get(i).cleanup();
                EasyEnemy.remove(i);
                asteroidAudioThread.easyEnemyDeathSound.run();
            }
        }
    }

    private void SpawnCoin(BadGuy badGuy) {
        GoldCoin goldCoin = new GoldCoin(userCharecter, canvas, screenSize, getResources(), 5, badGuy.getX() + badGuy.EasyEnemyAlive.getWidth() / 2, badGuy.getY() + badGuy.EasyEnemyAlive.getHeight() / 2);
        GoldCoins.add(goldCoin);
    }

    public void moveCoins() {
        for (int c = 0; c < GoldCoins.size(); c++) {
            GoldCoins.get(c).setCurY(GoldCoins.get(c).getCurY() + GoldCoins.get(c).getSpeed());
            GoldCoins.get(c).draw();

            if (GoldCoins.get(c).getCurY() > screenSize[1]) {
                GoldCoins.remove(c);
            }
        }


    }

    public void drawCoins() {
        for (int c = 0; c < GoldCoins.size(); c++) {
            canvas.drawBitmap(GoldCoins.get(c).GoldCoinBitmap, GoldCoins.get(c).getCurx(), GoldCoins.get(c).getCurY(), null);
        }
    }

    public int randomNum(int max, int min) {
        Random random = new Random();
        int ran = random.nextInt(max);

        if (ran < min) {
            ran = min;
        }
        return ran;
    }

    private void sleep(int time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    public void resume() {
        thread = new Thread(this);
        thread.start();
        isPlaying = true;
    }

    public void pause() {
        try {
            Cleanup();
            isPlaying = false;
            thread.join();

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
