package com.thetechnoobs.moterskillgame.asteriodgame;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceView;

import com.thetechnoobs.moterskillgame.BackendSettings;
import com.thetechnoobs.moterskillgame.R;
import com.thetechnoobs.moterskillgame.UserData;
import com.thetechnoobs.moterskillgame.UserInventory;
import com.thetechnoobs.moterskillgame.asteriodgame.entites.Astriod;
import com.thetechnoobs.moterskillgame.asteriodgame.entites.BadGuy;
import com.thetechnoobs.moterskillgame.asteriodgame.entites.GoldCoin;
import com.thetechnoobs.moterskillgame.asteriodgame.entites.RayGunBeam;
import com.thetechnoobs.moterskillgame.asteriodgame.entites.RegularBullet;
import com.thetechnoobs.moterskillgame.asteriodgame.entites.ShotGunBullet;
import com.thetechnoobs.moterskillgame.asteriodgame.entites.UserCharecter;
import com.thetechnoobs.moterskillgame.asteriodgame.ui.BackgroundStar;
import com.thetechnoobs.moterskillgame.asteriodgame.ui.Explosion;
import com.thetechnoobs.moterskillgame.asteriodgame.ui.HeathHeart;
import com.thetechnoobs.moterskillgame.asteriodgame.ui.ShootRegularButtonUI;
import com.thetechnoobs.moterskillgame.weapons.AssaultRifle;
import com.thetechnoobs.moterskillgame.weapons.BasicGun;
import com.thetechnoobs.moterskillgame.weapons.ShotGun;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class AsteroidGameView extends SurfaceView implements Runnable {
    private final Paint paint, BackgroundRectPaint, scoreTextPaint, ammoTextPaint;
    UserCharecter userCharecter;
    UserInventory userInventory;
    UserData userData;
    ShootRegularButtonUI shootRegularButtonUI;
    RectF backgroundRect;
    ArrayList<BackgroundStar> backgroundStars = new ArrayList<>();
    ArrayList<Astriod> astriods = new ArrayList<>();
    ArrayList<RegularBullet> bullets = new ArrayList<>();
    ArrayList<ShotGunBullet> shotGunBullets = new ArrayList<>();
    ArrayList<BadGuy> EasyEnemy = new ArrayList<>();
    ArrayList<GoldCoin> GoldCoins = new ArrayList<>();
    int[] screenSize = {0, 0};
    Canvas canvas;
    AsteroidAudioThread asteroidAudioThread;
    WaveSpawnThread waveSpawnThread;
    GunBulletThreads gunBulletThreads;
    boolean waveDoneSending = false;
    RayGunBeam rayGunBeam;
    private Thread thread;
    private boolean isPlaying = false;
    private boolean shooting = false;
    private int ammoLeft, maxAmmoOfCurGun;


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

        ammoTextPaint = new Paint();
        ammoTextPaint.setTextSize(30);
        ammoTextPaint.setColor(Color.RED);

        scoreTextPaint = new Paint();
        scoreTextPaint.setColor(Color.GRAY);
        scoreTextPaint.setTextSize((float) screeny / 30);

        BackgroundRectPaint = new Paint();
        BackgroundRectPaint.setColor(Color.BLACK);

        //waveSpawnThread.start();

        setAmmoData();

    }

    public static float convertPixelsToDp(float px) {
        DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();
        return Math.round(px / (metrics.densityDpi / 160f));
    }

    public static float convertDpToPixel(float dp) {
        DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();
        return Math.round(dp * (metrics.densityDpi / 160f));
    }

    @Override
    public void run() {
        while (isPlaying) {
            draw();
            update();

            if (userCharecter.getHeath() > 0) {
                checkForWaveCompletion();
            }
            //sleep(5);
        }
    }

    private void draw() {
        if (userCharecter.getHeath() < 1) {//check if user is dead
            userData.subtractOneFromWaveCount();
            gameOver();
        }

        if (astriods.size() < 3) {
            spawnAsteroids(3);
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
                    case 3:
                        if (!gunBulletThreads.shotGunShootThread.running) {
                            gunBulletThreads.startShotGunShot();
                        }
                        break;
                    case 4:
                        if (!gunBulletThreads.rayGunShootThread.running) {
                            gunBulletThreads.startRayGunShot();
                        }
                        break;
                }
            }
            return true;
        }
        return false;
    }

    public void gameOver() {
        isPlaying = false;
        Context context = getContext();
        Intent GoToEndGameScreen = new Intent(context, EndGameScreenAsteroids.class);
        GoToEndGameScreen.putExtra("score", userCharecter.getUserScore());
        GoToEndGameScreen.putExtra("gold", userCharecter.getGold());
        GoToEndGameScreen.putExtra("enemysKilled", userCharecter.getEnemysKilled());
        GoToEndGameScreen.putExtra("damageTaken", userCharecter.getDamageTaken());

        //if user died send false
        if (userCharecter.getHeath() < 1) {
            GoToEndGameScreen.putExtra("WaveComplete", false);
        } else {
            GoToEndGameScreen.putExtra("WaveComplete", true);
        }

        context.startActivity(GoToEndGameScreen);
        Cleanup();
    }

    private void Cleanup() {
        backgroundStars.clear();
        astriods.clear();
        bullets.clear();
        shotGunBullets.clear();
        GoldCoins.clear();

        for (int e = 0; e < EasyEnemy.size(); e++) {
            EasyEnemy.get(e).cleanup();
        }

        EasyEnemy.clear();
        asteroidAudioThread.releaseAll();
        waveSpawnThread.stopAll();
    }

    public void drawAmmoLeft() {
        float xLoc = convertDpToPixel(10);
        float yLoc = convertDpToPixel(20);
        //set color of text depending on ammo left
        if (ammoLeft > maxAmmoOfCurGun / 2) {
            ammoTextPaint.setColor(getResources().getColor(R.color.green_600));
        } else {
            ammoTextPaint.setColor(getResources().getColor(R.color.red_800));
        }

        //set text to say reloading while reloading
        if (ammoLeft == 0) {
            ammoTextPaint.setColor(getResources().getColor(R.color.yellow_A700));
            canvas.drawText("Reloading...", xLoc, yLoc, ammoTextPaint);
        } else {
            canvas.drawText(String.valueOf(ammoLeft), xLoc, yLoc, ammoTextPaint);
        }

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

        //check if astriod hits user
        for (int i = 0; i < astriods.size(); i++) {
            if (astriods.get(i).getCollisionBox().intersect(userCharecter.getHitBox())) {
                explosionFrame(astriods.get(i).getCurX(), astriods.get(i).getCurY());
                astriods.remove(i);
                userCharecter.setHeath(userCharecter.getHeath() - 1);
                userCharecter.setDamageTaken(userCharecter.getDamageTaken() + 1);
                asteroidAudioThread.startUserHitSound();
                break;
            }
        }

        //check if coins are collected
        for (int c = 0; c < GoldCoins.size(); c++) {
            if (GoldCoins.get(c).getHitbox().intersect(userCharecter.getHitBox())) {
                GoldCoins.remove(c);
                asteroidAudioThread.startCoinCollectSound();
                userCharecter.setGold(userCharecter.getGold() + 1);
            }
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

        for (int s = 0; s < shotGunBullets.size(); s++) {
            ShotGunBullet mBullit = shotGunBullets.get(s);
            if (mBullit.getCurY() < 0 || mBullit.getCurX() < 0 || mBullit.getCurX() > screenSize[0]) {
                Log.v("testing", "despawn: " + s);
                shotGunBullets.remove(s);
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
                    asteroidAudioThread.startAsteroidExplosionSound();
                    brake = true;
                    break;
                }
            }

            for (int b = 0; b < shotGunBullets.size(); b++) {
                if (shotGunBullets.get(b).getHitbox().intersect(astriods.get(a).getCollisionBox())) {
                    explosionFrame(astriods.get(a).getCurX(), astriods.get(a).getCurY());
                    astriods.remove(a);
                    shotGunBullets.remove(b);
                    userCharecter.setUserScore(userCharecter.getUserScore() + 1);
                    asteroidAudioThread.startAsteroidExplosionSound();
                    brake = true;
                    break;
                }
            }

            if (rayGunBeam != null) {
                if (rayGunBeam.getHitbox().intersect(astriods.get(a).getCollisionBox())) {
                    explosionFrame(astriods.get(a).getCurX(), astriods.get(a).getCurY());
                    astriods.remove(a);
                    userCharecter.setUserScore(userCharecter.getUserScore() + 1);
                    asteroidAudioThread.startAsteroidExplosionSound();
                    brake = true;
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
                    userCharecter.setUserScore(userCharecter.getUserScore() + 3);
                    if (EasyEnemy.get(e).getCurHeath() > 0) {
                        asteroidAudioThread.startEasyEnemyHitSound();
                    }
                    brake = true;
                    break;
                }
            }

            for (int b = 0; b < shotGunBullets.size(); b++) {
                if (shotGunBullets.get(b).getHitbox().intersect(EasyEnemy.get(e).getHitBox())) {
                    EasyEnemy.get(e).setCurHeath(EasyEnemy.get(e).getCurHeath() - new BasicGun(getContext()).getDamage());
                    shotGunBullets.remove(b);
                    userCharecter.setUserScore(userCharecter.getUserScore() + 3);
                    if (EasyEnemy.get(e).getCurHeath() > 0) {
                        asteroidAudioThread.startEasyEnemyHitSound();
                    }
                    brake = true;
                    break;
                }
            }


            if (brake) {
                break;
            }
        }

    }

    public void spawnAssaultBullet() {
        setAmmoLeft(getAmmoLeft() - 1);
        bullets.add(new RegularBullet(getResources(), 50, screenSize, userCharecter.getCurX() + (float) userCharecter.bitmap.getWidth() / 2, userCharecter.getCurY()));
        asteroidAudioThread.startAssaultRifleSound();
    }

    public void spawnRegularBullet() {
        setAmmoLeft(getAmmoLeft() - 1);
        bullets.add(new RegularBullet(getResources(), 50, screenSize, userCharecter.getCurX() + (float) userCharecter.bitmap.getWidth() / 2, userCharecter.getCurY()));
        asteroidAudioThread.startBasicGunShootSound();
    }

    public void spawnShotGunBulletSet() {
        ShotGun shotGun = new ShotGun(getContext());
        setAmmoLeft(getAmmoLeft() - 1);
        asteroidAudioThread.startShotGunShotSound();

        for (int b = 0; b < shotGun.getNumberOfProjectiles(); b++) {
            shotGunBullets.add(new ShotGunBullet(getResources(), 20, b, screenSize, userCharecter.getCurX() + (float) userCharecter.bitmap.getWidth() / 2, userCharecter.getCurY()));
        }
    }

    public void spawnRayBeam(boolean b) {
        if (b) {
            rayGunBeam = new RayGunBeam(getContext(), getResources(),
                    (int) userCharecter.getCurX()+userCharecter.bitmap.getWidth()/2,
                    (int) (userCharecter.getCurY()+convertDpToPixel(-260)));
        } else {
            rayGunBeam = null;
        }

    }

    private void moveBullets() {
        for (int i = 0; i < bullets.size(); i++) {
            bullets.get(i).setCurY((int) (bullets.get(i).getCurY() - bullets.get(i).getSpeed()));
        }

        if (rayGunBeam != null) {
            rayGunBeam.setX((int) userCharecter.getCurX()+userCharecter.bitmap.getWidth()/2);
            rayGunBeam.setY((int) userCharecter.getCurY()-rayGunBeam.bitmap.getHeight());
        }
    }

    private void drawBullets() {
        for (int i = 0; i < bullets.size(); i++) {
            canvas.drawBitmap(bullets.get(i).bitmap, bullets.get(i).getCurX(), bullets.get(i).getCurY(), null);
        }

        for (int s = 0; s < shotGunBullets.size(); s++) {
            shotGunBullets.get(s).drawButtets(canvas);
        }

        if (rayGunBeam != null) {
            canvas.drawBitmap(rayGunBeam.bitmap, rayGunBeam.getX(), rayGunBeam.getY(), null);
            //canvas.drawRect(rayGunBeam.getHitbox(), ammoTextPaint);
        }

    }

    private void checkForDeadEnemys() {
        for (int i = 0; i < EasyEnemy.size(); i++) {
            int x = EasyEnemy.get(i).getX();
            int y = EasyEnemy.get(i).getY();

            if (EasyEnemy.get(i).getCurHeath() < 1) {
                EasyEnemy.get(i).cleanup();
                EasyEnemy.remove(i);
                userCharecter.setEnemysKilled(userCharecter.getEnemysKilled() + 1);
                asteroidAudioThread.startDeadEnemySound();
                spawnCoin(x, y);
            }
        }
    }

    public void spawnCoin(int x, int y) {
        GoldCoin goldCoin = new GoldCoin(userCharecter, canvas, screenSize, getResources(), 5, x, y);
        GoldCoins.add(goldCoin);
    }

    private void moveCoins() {
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

    public boolean getShouldShoot() {
        return shooting;
    }

    public int getAmmoLeft() {
        return ammoLeft;
    }

    public void setAmmoLeft(int ammoLeft) {
        this.ammoLeft = ammoLeft;
    }

    private void setAmmoData() {
        //0 = none, 1 = basic gun, 2 = assault rifle, 3 = shot gun, 4 = ray gun
        switch (userInventory.getEquippedWeapon()) {
            case 1:
                BasicGun basicGun = new BasicGun(getContext());
                ammoLeft = basicGun.getMaxAmmo();
                maxAmmoOfCurGun = basicGun.getMaxAmmo();
                break;
            case 2:
                AssaultRifle assaultRifle = new AssaultRifle(getContext());
                ammoLeft = assaultRifle.getMaxAmmo();
                maxAmmoOfCurGun = assaultRifle.getMaxAmmo();
                break;
            case 3:
                ShotGun shotGun = new ShotGun(getContext());
                ammoLeft = shotGun.getMaxAmmo();
                maxAmmoOfCurGun = shotGun.getMaxAmmo();
                break;
            case 4:
                //ammoLeft = new RayGun(getContext()).getMaxAmmo();
                break;
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


    public void destroy() {
        Cleanup();
    }
}
