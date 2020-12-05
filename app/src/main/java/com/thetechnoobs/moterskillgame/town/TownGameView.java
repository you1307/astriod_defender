package com.thetechnoobs.moterskillgame.town;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.SurfaceView;

import com.thetechnoobs.moterskillgame.town.AI.AlcamistAI;
import com.thetechnoobs.moterskillgame.town.AI.WeaponSmithAI;
import com.thetechnoobs.moterskillgame.town.ui.ButtonUI;
import com.thetechnoobs.moterskillgame.town.ui.CurrencyUI;

import java.util.List;

public class TownGameView extends SurfaceView implements Runnable {

    public boolean isPlaying = false;
    int[] screenSize = {0, 0};
    Canvas canvas;
    Paint TextPaint;
    Town town;
    UserCharacter userCharacter;
    AlcamistAI alcamistAI;
    WeaponSmithAI weaponSmithAI;
    List<RectF> ArrowUI;
    ButtonUI buttonUI;
    CurrencyUI currencyUI;
    int MoveUserDirection = 2;
    TownAudioThread townAudioThread;
    private Thread thread;
    private long FPS = 0;
    private long fps;
    private boolean moving = false;

    public TownGameView(Context context, int screenx, int screeny) {
        super(context);
        screenSize[0] = screenx;
        screenSize[1] = screeny;


        userCharacter = new UserCharacter((int) convertDpToPixel(410), (int) convertDpToPixel(30), screenSize, getResources());
        userCharacter.moveUser(MoveUserDirection);//set so user is facing the right direction upon spawn

        town = new Town(getResources(), screenSize, userCharacter);

        alcamistAI = new AlcamistAI(0, 0, screenSize, getResources(), userCharacter, town);
        weaponSmithAI = new WeaponSmithAI(0, 0, screenSize, getResources(), userCharacter, town);

        buttonUI = new ButtonUI(getResources(), screenSize);
        ArrowUI = buttonUI.getControllerHitBoxs();

        currencyUI = new CurrencyUI(getContext(), getResources(), screenSize);

        townAudioThread = new TownAudioThread(context);

        TextPaint = new Paint();
        TextPaint.setTextSize(70f);
        TextPaint.setColor(Color.BLACK);

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
            long startFrameTime = System.currentTimeMillis();

            update();
            draw();

            LimitFPS(startFrameTime);
        }
    }

    public void update() {
        town.update();
        alcamistAI.update();
        userCharacter.update();
        weaponSmithAI.update();
        checkBoundres();
    }

    private void checkBoundres() {
        List<RectF> boundres = town.getBoundaries();

        for (int b = 0; b < boundres.size(); b++) {//check for unpassable bounderys
            if (userCharacter.getHitbox().intersect(boundres.get(b))) {
                moving = false;
                break;
            }
        }

        if (userCharacter.getHitbox().intersect(weaponSmithAI.getBoundry())) {
            weaponSmithAI.drawAprochedText(true);
            buttonUI.drawShopBtnUI(true, 2);
        } else if (userCharacter.getHitbox().intersect(alcamistAI.getBoudry())) {
            alcamistAI.drawAprochedText(true);
            buttonUI.drawShopBtnUI(true, 1);
        } else {
            weaponSmithAI.drawAprochedText(false);
            buttonUI.drawShopBtnUI(false);
            alcamistAI.drawAprochedText(false);
            buttonUI.drawShopBtnUI(false);
        }

    }

    public void draw() {
        if (getHolder().getSurface().isValid()) {
            canvas = getHolder().lockCanvas();

            town.draw(canvas);


            alcamistAI.draw(canvas, town);
            weaponSmithAI.draw(canvas, town);
            userCharacter.draw(canvas);


            DrawUI();


            if (moving) {
                town.updateMapView(MoveUserDirection);
                userCharacter.updateSprite(MoveUserDirection);
            }
            getHolder().unlockCanvasAndPost(canvas);
        }
    }

    private void DrawUI() {
        buttonUI.draw(canvas);
        currencyUI.draw(canvas);
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
                if (MoveUI(event.getX(), event.getY(), true)) {
                    moving = true;
                }
                CheckUI(event.getX(), event.getY());
                break;
            case MotionEvent.ACTION_POINTER_DOWN:

                break;
            case MotionEvent.ACTION_MOVE:
                if (!MoveUI(event.getX(), event.getY(), false)) {
                    moving = false;
                }
                break;
            case MotionEvent.ACTION_UP:
                if (MoveUI(event.getX(), event.getY(), true)) {
                    moving = false;
                }
                break;
            case MotionEvent.ACTION_POINTER_UP:
                break;
            case MotionEvent.ACTION_CANCEL:
                break;
        }
        invalidate();

        return true;
    }

    private boolean MoveUI(float x, float y, boolean ActOn) {
        for (int i = 0; i < ArrowUI.size(); i++) {

            if (x < ArrowUI.get(i).right && x > ArrowUI.get(i).left && y < ArrowUI.get(i).bottom && y > ArrowUI.get(i).top) {
                if (ActOn) {
                    MoveUserDirection = i;
                    userCharacter.updateSprite(i);
                    town.updateMapView(i);
                }
                return true;
            }
        }
        return false;
    }

    private boolean CheckUI(float x, float y) {
        RectF touchPoint = new RectF(x, y, x + 10, y + 10);

        if (buttonUI.getShopBtnHitbox() != null && touchPoint.intersect(buttonUI.getShopBtnHitbox())) {
            buttonUI.shopBtn = buttonUI.shopBtnPushed;

            switch (buttonUI.shopToGoTo) {//1 = chem, 2 = wepons
                case 1:
                    Intent GoToChemShop = new Intent(getContext(), ChemShopActivity.class);
                    getContext().startActivity(GoToChemShop);
                    pause();
                    break;
                case 2:
                    Intent GoToWeponShop = new Intent(getContext(), WeponShopActivity.class);
                    getContext().startActivity(GoToWeponShop);
                    pause();
                    break;
            }

        }


        return false;
    }

    public void LimitFPS(long startFrameTime) {
        long timeThisFrame = System.currentTimeMillis() - startFrameTime;
        if (timeThisFrame >= 1) {
            fps = 1000 / timeThisFrame;
        }

        if (timeThisFrame < 40) {
            try {
                Thread.sleep(33 - timeThisFrame);
            } catch (Exception e) {
                e.printStackTrace();
            }
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
