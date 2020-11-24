package com.thetechnoobs.moterskillgame.asteriodgame;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.view.MotionEvent;
import android.view.SurfaceView;

import com.thetechnoobs.moterskillgame.BackendSettings;
import com.thetechnoobs.moterskillgame.asteriodgame.entites.Astriod;
import com.thetechnoobs.moterskillgame.asteriodgame.entites.BadGuy;
import com.thetechnoobs.moterskillgame.asteriodgame.entites.GoldCoin;
import com.thetechnoobs.moterskillgame.asteriodgame.entites.RegularBullet;
import com.thetechnoobs.moterskillgame.asteriodgame.ui.BackgroundStar;
import com.thetechnoobs.moterskillgame.asteriodgame.ui.Explosion;
import com.thetechnoobs.moterskillgame.asteriodgame.ui.HeathHeart;
import com.thetechnoobs.moterskillgame.asteriodgame.ui.ShootRegularButtonUI;
import com.thetechnoobs.moterskillgame.UserCharecter;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class AsteroidGameView extends SurfaceView implements Runnable {
    private final Paint paint, BackgroundRectPaint, scoreTextPaint;
    UserCharecter userCharecter;
    ShootRegularButtonUI shootRegularButtonUI;
    RectF backgroundRect;
    ArrayList<BackgroundStar> backgroundStars = new ArrayList<>();
    ArrayList<Astriod> astriods = new ArrayList<>();
    ArrayList<RegularBullet> bullets = new ArrayList<>();
    ArrayList<BadGuy> EasyEnemy = new ArrayList<>();
    ArrayList<GoldCoin> GoldCoins = new ArrayList<>();
    int[] screenSize = {0, 0};
    Canvas canvas;
    AudioThread audioThread;
    private Thread thread;
    private boolean isPlaying = false;


    public AsteroidGameView(Context context, int screenx, int screeny) {
        super(context);

        screenSize[0] = screenx;
        screenSize[1] = screeny;

        backgroundRect = new RectF(0, 0, screenx, screeny);

        userCharecter = new UserCharecter(getResources(), screenSize, (float) screenx / 2, (float) screeny - 300);

        settupButtonUI(context);

        audioThread = new AudioThread(context);

        paint = new Paint();
        paint.setColor(Color.BLUE);

        scoreTextPaint = new Paint();
        scoreTextPaint.setColor(Color.GRAY);
        scoreTextPaint.setTextSize((float) screeny / 30);

        BackgroundRectPaint = new Paint();
        BackgroundRectPaint.setColor(Color.BLACK);


    }


    @Override
    public void run() {
        while (isPlaying) {
            draw();
            //sleep(5);
        }
    }

    private void draw() {
        if (userCharecter.getHeath() < 1) {//check if user is dead
            GameOver();
        }

        if (getHolder().getSurface().isValid()) {
            canvas = getHolder().lockCanvas();
            canvas.drawRect(backgroundRect, BackgroundRectPaint);
            canvas.drawBitmap(userCharecter.bitmap, userCharecter.getCurX(), userCharecter.getCurY(), null);
            canvas.drawBitmap(shootRegularButtonUI.bitmap, shootRegularButtonUI.getX(), shootRegularButtonUI.getY(), null);

            SawnAnDrawEasyEnemy(3);

            DrawUI();


            drawBackgrounStars();
            drawAstroids();
            drawBullets();

            update();
            CheckForCollision();
            getHolder().unlockCanvasAndPost(canvas);
        }
    }

    private void DrawUI() {
        drawUserHealth();
        drawUserScore();
    }

    private void update() {

        //update coins position
        MoveCoins();

        //check for dead enemy's
        CheckForDeadEnemys();

        //update Bullets position
        MoveBullets();
        DespawnBullets();

        //deal with astroid and astroid hitbox stuff
        SpawnAndDeleteAstroids();
        MoveAstroids();

        //Deal with background stuff
        moveBackgroundStars();
        SpawnAndDeleteBackgroundStars();
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
                checkUIButtons(event.getX(), event.getY(), true);
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                checkUIButtons(event.getX(pointerIndex), event.getY(pointerIndex), true);
                break;
            case MotionEvent.ACTION_MOVE:
                if (!checkUIButtons(event.getX(), event.getY(), false)) {
                    updateUserPos(event.getX(), event.getY());
                }
                break;
            case MotionEvent.ACTION_UP:
                break;
            case MotionEvent.ACTION_POINTER_UP:
                break;
            case MotionEvent.ACTION_CANCEL:
                break;
        }
        invalidate();

        return true;
    }

    public void GameOver() {
        isPlaying = false;
        Context context = getContext();
        Intent GoToEndGameScreen = new Intent(context, EndGameScreenAsteroids.class);
        GoToEndGameScreen.putExtra("score", userCharecter.getUserScore());
        GoToEndGameScreen.putExtra("gold", userCharecter.getGold());
        GoToEndGameScreen.putExtra("enemysKilled", userCharecter.getEnemysKilled());
        context.startActivity(GoToEndGameScreen);

        Cleanup();
    }

    private void Cleanup() {
        backgroundStars.clear();
        astriods.clear();
        bullets.clear();
        GoldCoins.clear();


        for (int e = 0; e < EasyEnemy.size(); e++) {
            EasyEnemy.get(e).shootThread.stopThread();
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


    private void CheckForCollision() {
        BulletHitboxDetection();

        for (int i = 0; i < astriods.size(); i++) {//check if astriod hits user
            if (astriods.get(i).getCollisionBox().intersect(userCharecter.getHitBox())) {
                int Eventx, Eventy;
                Eventx = astriods.get(i).getCurX();
                Eventy = astriods.get(i).getCurY();
                astriods.remove(i);
                ExplosionFrame(Eventx, Eventy);
                userCharecter.setHeath(userCharecter.getHeath() - 1);
                audioThread.asteriodHitUserThread.run();
                break;
            }
        }

        //check if coins are collected
        for (int c = 0; c < GoldCoins.size(); c++) {
            if (GoldCoins.get(c).getHitbox().intersect(userCharecter.getHitBox())) {
                GoldCoins.remove(c);
                audioThread.coinCollectSound.run();
                userCharecter.setGold(userCharecter.getGold() + 1);
            }
        }
    }

    private void MoveBullets() {
        for (int i = 0; i < bullets.size(); i++) {
            bullets.get(i).setCurY((int) (bullets.get(i).getCurY() - bullets.get(i).getSpeed()));
        }
    }

    private void drawBullets() {
        for (int i = 0; i < bullets.size(); i++) {
            canvas.drawBitmap(bullets.get(i).bitmap, bullets.get(i).getCurX(), bullets.get(i).getCurY(), null);
        }
    }

    private void ExplosionFrame(float eventx, float eventy) {
        Explosion explosion = new Explosion((int) eventx, (int) eventy, screenSize, getResources());
        canvas.drawBitmap(explosion.bitmap, explosion.x, explosion.y, null);
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

    private void MoveAstroids() {
        for (int i = 0; i < astriods.size(); i++) {
            astriods.get(i).setCurY(astriods.get(i).getCurY() + astriods.get(i).getSpeed());
        }
    }

    public void SawnAnDrawEasyEnemy(int NumOfEnemy) {
        if (EasyEnemy.size() < NumOfEnemy) {
            SpawnEnemys();
        }


        for (int i = 0; i < EasyEnemy.size(); i++) {
            canvas.drawBitmap(EasyEnemy.get(i).EasyEnemyAlive, EasyEnemy.get(i).getX(), EasyEnemy.get(i).getY(), null);
            EasyEnemy.get(i).update(canvas);
        }

    }

    public void SpawnEnemys() {
        BadGuy badGuy = new BadGuy(getResources(), getContext(), userCharecter, screenSize, randomNum(screenSize[0] - (screenSize[0] / Constants.SCALE_RATIO_NUM_X_EASY_ENEMY), 0), 300);
        EasyEnemy.add(badGuy);
    }

    private void SpawnAndDeleteAstroids() {
        int maxAstroidSpawn = 6;

        if (astriods.size() < maxAstroidSpawn) {
            Astriod astriod = new Astriod(randomNum(screenSize[0] - (screenSize[0] / Constants.SCALE_RATIO_NUM_X_FOREGROUND), 0), 0, randomNum(Constants.ASTROID_MAX_SPEED, 5), screenSize, getResources());
            astriods.add(astriod);
        }

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

    private void SpawnAndDeleteBackgroundStars() {

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

    private boolean checkUIButtons(float x, float y, boolean ActOn) {
        if (x > shootRegularButtonUI.getX() && y > shootRegularButtonUI.getY()) {
            if (ActOn) {
                SpawnRegularBullet();
                audioThread.simpleShootSound.run();
            }
            return true;
        }
        return false;
    }

    private void SpawnRegularBullet() {
        bullets.add(new RegularBullet(getResources(), 50, screenSize, userCharecter.getCurX() + (float) userCharecter.bitmap.getWidth() / 2, userCharecter.getCurY()));
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
                    ExplosionFrame(astriods.get(a).getCurX(), astriods.get(a).getCurY());
                    astriods.remove(a);
                    bullets.remove(b);
                    userCharecter.setUserScore(userCharecter.getUserScore() + 1);
                    audioThread.simpleShootAsteriodSound.run();
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
                    EasyEnemy.get(e).setCurHeath(EasyEnemy.get(e).getCurHeath() - 1);
                    bullets.remove(b);
                    audioThread.easyEnemyHitSound.run();
                    brake = true;
                    break;
                }
            }
            if (brake) {
                break;
            }
        }

    }

    public void CheckForDeadEnemys() {
        for (int i = 0; i < EasyEnemy.size(); i++) {
            if (EasyEnemy.get(i).getCurHeath() < 1) {
                SpawnCoin(EasyEnemy.get(i));
                userCharecter.setEnemysKilled(userCharecter.getEnemysKilled() + 1);
                EasyEnemy.remove(i);
            }
        }
    }

    private void SpawnCoin(BadGuy badGuy) {
        GoldCoin goldCoin = new GoldCoin(userCharecter, canvas, screenSize, getResources(), 5, badGuy.getX() + badGuy.EasyEnemyAlive.getWidth() / 2, badGuy.getY() + badGuy.EasyEnemyAlive.getHeight() / 2);
        GoldCoins.add(goldCoin);
    }

    public void MoveCoins() {
        for (int c = 0; c < GoldCoins.size(); c++) {
            GoldCoins.get(c).setCurY(GoldCoins.get(c).getCurY() + GoldCoins.get(c).getSpeed());
            GoldCoins.get(c).draw();

            if (GoldCoins.get(c).getCurY() > screenSize[1]) {
                GoldCoins.remove(c);
            }
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
            isPlaying = false;
            thread.join();
            Cleanup();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}