package com.thetechnoobs.moterskillgame;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.SurfaceView;

import com.thetechnoobs.moterskillgame.asteriodgame.ui.BackgroundStar;

import java.util.ArrayList;
import java.util.Random;

public class MenuSpaceBackground implements Runnable {
    Context context;
    SurfaceView surfaceView;
    Canvas canvas;
    ArrayList<BackgroundStar> backgroundStars = new ArrayList<>();
    int[] screenSize;
    Paint paint = new Paint();
    private boolean shouldRun = true;
    private int starCount = 10;

    public MenuSpaceBackground(Context context, SurfaceView surfaceView, int[] screenSize) {
        this.context = context;
        this.screenSize = screenSize;
        this.surfaceView = surfaceView;
        paint.setColor(context.getResources().getColor(R.color.black));

        while (backgroundStars.size() < starCount) {
            BackgroundStar backgroundStar = new BackgroundStar(new Random().nextInt(screenSize[0] - 10),
                    new Random().nextInt(screenSize[1]),
                    new Random().nextInt(10) + 1,
                    screenSize,
                    context.getResources());

            backgroundStars.add(backgroundStar);
        }
    }

    @Override
    public void run() {
        while (shouldRun) {
            update();
            draw();
        }
    }

    private void update() {
        for (int s = 0; s < backgroundStars.size(); s++) {
            if (backgroundStars.get(s).getCurY() > screenSize[1]) {
                backgroundStars.remove(s);
            } else {
                backgroundStars.get(s).setCurY(backgroundStars.get(s).getCurY() + backgroundStars.get(s).getSpeed());
            }

        }

        if (backgroundStars.size() < starCount) {
            BackgroundStar backgroundStar = new BackgroundStar(new Random().nextInt(screenSize[0] - 10), 0, new Random().nextInt(15) + 1, screenSize, context.getResources());
            backgroundStars.add(backgroundStar);
        }
    }

    private void draw() {
        if (surfaceView.getHolder().getSurface().isValid()) {
            canvas = surfaceView.getHolder().lockCanvas();

            canvas.drawRect(0, 0, screenSize[0], screenSize[1], paint);

            for (int s = 0; s < backgroundStars.size(); s++) {
                canvas.drawBitmap(backgroundStars.get(s).stareBitmap, backgroundStars.get(s).getCurX(), backgroundStars.get(s).getCurY(), null);
            }


            surfaceView.getHolder().unlockCanvasAndPost(canvas);
        }
    }

    public void stopThread() {
        shouldRun = false;
    }
}
