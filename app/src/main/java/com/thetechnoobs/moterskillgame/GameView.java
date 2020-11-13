package com.thetechnoobs.moterskillgame;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.view.MotionEvent;
import android.view.SurfaceView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GameView extends SurfaceView implements Runnable {
    private final Paint paint, BackgroundRectPaint, scoreTextPaint;
    UserCharecter userCharecter;
    ShootRegularButtonUI shootRegularButtonUI;
    RectF backgroundRect;
    ArrayList<BackgroundStar> backgroundStars = new ArrayList<>();
    ArrayList<Astriod> astriods = new ArrayList<>();
    ArrayList<RegularBullet> bullets = new ArrayList<>();
    int[] screenSize = {0, 0};
    Canvas canvas;
    private Thread thread;
    private boolean isPlaying = false;
    private int screenx, screeny;


    public GameView(Context context, int screenx, int screeny) {
        super(context);

        this.screenx = screenx;
        this.screeny = screeny;

        //settup public screen size for ratio calculation
        screenSize[0] = screenx;
        screenSize[1] = screeny;

        backgroundRect = new RectF(0, 0, screenx, screeny);

        userCharecter = new UserCharecter(getResources(), screenSize, (float) screenx / 2, (float) screeny - 300);

        settupButtonUI(context);

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
            //sleep(10);
        }
    }

    private void draw() {
        if (getHolder().getSurface().isValid()) {
            canvas = getHolder().lockCanvas();
            canvas.drawRect(backgroundRect, BackgroundRectPaint);//needed inorder to stop graphics from duplicating
            canvas.drawBitmap(userCharecter.bitmap, userCharecter.getCurX(), userCharecter.getCurY(), null);
            canvas.drawBitmap(shootRegularButtonUI.bitmap, shootRegularButtonUI.getX(), shootRegularButtonUI.getY(), null);


            drawUserHealth();
            drawUserScore();

            drawBackgrounStars();
            drawAstroids();
            drawBullets();

            update();
            CheckForCollision();
            getHolder().unlockCanvasAndPost(canvas);
        }
    }

    private void update() {
        //check if user should be dead
        if (userCharecter.getHeath() < 1) {
            GameOver();
        }

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
        //Context context = getContext();
        //context.startActivity(new Intent(context, EndGameScreen.class));
        //TODO make game end, display score, save score to firebase, and give option to restart or exit
    }

    public void drawUserHealth() {
        int yOffset = screeny / 20;//needed for screens with round corners so that the heath heart doesnt get hidden
        int xOffset = screenx / 100;//added to make nice spaceing between hearts
        List<HeathHeart> heathHearts = new ArrayList<>();

        for (int i = 0; i < userCharecter.getMaxHeath(); i++) {
            HeathHeart heart = new HeathHeart(getResources(), screenSize, xOffset, yOffset);
            heathHearts.add(heart);

            if (i < userCharecter.getHeath()) {//if pos i should be full heart
                if (i != 0) {//if pos i is not first spot
                    heathHearts.get(i).xLoc = heathHearts.get(i).xLoc + heathHearts.get(i - 1).xLoc + heathHearts.get(i - 1).fullHeartBitmap.getWidth();
                }
                canvas.drawBitmap(heathHearts.get(i).fullHeartBitmap, heathHearts.get(i).xLoc, heathHearts.get(i).yLoc, paint);
            } else {//if pos i should be empty heart
                if (i != 0) {//if pos i is not first
                    heathHearts.get(i).xLoc = heathHearts.get(i).xLoc + heathHearts.get(i - 1).xLoc + heathHearts.get(i - 1).emptyHeartBitmap.getWidth();
                }
                canvas.drawBitmap(heathHearts.get(i).emptyHeartBitmap, heathHearts.get(i).xLoc, heathHearts.get(i).yLoc, paint);
            }
        }
    }

    private void drawUserScore() {
        canvas.drawText(String.valueOf(userCharecter.getUserScore()), (float) screenx / 2, (float) screeny / 20, scoreTextPaint);
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

    private void SpawnAndDeleteAstroids() {
        int maxAstroidSpawn = 6;

        if (astriods.size() < maxAstroidSpawn) {
            Astriod astriod = new Astriod(randomNum(screenx - (screenx / Constants.SCALE_RATIO_NUM_X_FOREGROUND), 0), 0, randomNum(Constants.ASTROID_MAX_SPEED, 5), screenSize, getResources());
            astriods.add(astriod);
        }

        for (int i = 0; i < astriods.size(); i++) {
            if (astriods.get(i).getCurY() > screeny) {
                astriods.remove(i);
                DeductScorePoints(1);
            }
        }
    }

    private void DeductScorePoints(int i) {
        userCharecter.setUserScore(userCharecter.getUserScore() - i);
    }

    private void SpawnAndDeleteBackgroundStars() {

        for (int i = 0; i < backgroundStars.size(); i++) {
            if (backgroundStars.get(i).getCurY() > screeny - 40) {
                backgroundStars.remove(i);
            }
        }

        if (backgroundStars.size() < 10) {
            BackgroundStar backgroundStar = new BackgroundStar(randomNum(screenx - 20, 20), 0, randomNum(Constants.BACKGROUND_STAR_MAX_SPEED, 5), screenSize, getResources());
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
            shootRegularButtonUI = new ShootRegularButtonUI(screenx - shootRegularButtonUI.bitmap.getWidth(), screeny - shootRegularButtonUI.bitmap.getHeight(), true, screenSize, getResources());
        }
    }

    private boolean checkUIButtons(float x, float y, boolean ActOn) {
        if (x > shootRegularButtonUI.getX() && y > shootRegularButtonUI.getY()) {
            if (ActOn) {
                SpawnRegularBullet();
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
            }
        }
    }

    private void BulletHitboxDetection() {
        for (int i = 0; i < astriods.size(); i++) {
            for (int b = 0; b < bullets.size(); b++) {
                if (bullets.get(b).getHitbox().intersect(astriods.get(i).getCollisionBox())) {
                    ExplosionFrame(astriods.get(i).getCurX(), astriods.get(i).getCurY());
                    astriods.remove(i);
                    bullets.remove(b);
                    userCharecter.setUserScore(userCharecter.getUserScore() + 1);
                }
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
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
