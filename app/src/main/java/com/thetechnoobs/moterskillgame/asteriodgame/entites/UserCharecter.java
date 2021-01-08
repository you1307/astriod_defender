package com.thetechnoobs.moterskillgame.asteriodgame.entites;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.RectF;

import com.thetechnoobs.moterskillgame.asteriodgame.Constants;
import com.thetechnoobs.moterskillgame.R;
import com.thetechnoobs.moterskillgame.asteriodgame.ui.HeathHeart;

import java.util.ArrayList;
import java.util.List;

public class UserCharecter {
    public int MaxHeath = Constants.BASE_USER_MAX_HEATH;
    public int CurHeath = MaxHeath;
    public int userScore = 1;
    public int EnemysKilled = 0;
    public int damageTaken;
    public Bitmap bitmap;
    int Gold = 0;
    float CurX, CurY;
    int[] screenSize;


    public UserCharecter(Resources resources, int[] screenSize, float CurX, float CurY) {
        this.screenSize = screenSize;
        this.CurX = CurX;
        this.CurY = CurY;

        bitmap = BitmapFactory.decodeResource(resources, R.drawable.spaceman);
        bitmap = Bitmap.createScaledBitmap(bitmap,
                screenSize[0] / Constants.SCALE_RATIO_NUM_X_USER,
                screenSize[1] / Constants.SCALE_RATIO_NUM_Y_USER,
                false);

    }

    public RectF getHitBox() {
        return new RectF(CurX, CurY, CurX + bitmap.getWidth(), CurY + bitmap.getHeight());
    }

    public float getCurX() {
        return CurX;
    }

    public void setCurX(float curX) {
        CurX = curX;
    }

    public float getCurY() {
        return CurY;
    }

    public void setCurY(float curY) {
        if (curY < screenSize[1] / 2f) {
            CurY = screenSize[1] / 2f;
        } else {
            CurY = curY;
        }
    }

    public void drawUserHealth(Resources resources, Canvas canvas) {
        int yOffset = screenSize[1] / 20;//needed for screens with round corners so that the heath heart doesnt get hidden
        int xOffset = screenSize[0] / 100;//added to make nice spaceing between hearts
        List<HeathHeart> heathHearts = new ArrayList<>();

        for (int i = 0; i < getMaxHeath(); i++) {
            HeathHeart heart = new HeathHeart(resources, screenSize, xOffset, yOffset);
            heathHearts.add(heart);

            if (i < getHeath()) {//if position i should be full heart
                if (i != 0) {//if position i is not first spot
                    heathHearts.get(i).xLoc = heathHearts.get(i).xLoc + heathHearts.get(i - 1).xLoc + heathHearts.get(i - 1).fullHeartBitmap.getWidth();
                }
                canvas.drawBitmap(heathHearts.get(i).fullHeartBitmap, heathHearts.get(i).xLoc, heathHearts.get(i).yLoc, null);
            } else {//if position i should be empty heart
                if (i != 0) {//if position i is not first
                    heathHearts.get(i).xLoc = heathHearts.get(i).xLoc + heathHearts.get(i - 1).xLoc + heathHearts.get(i - 1).emptyHeartBitmap.getWidth();
                }
                canvas.drawBitmap(heathHearts.get(i).emptyHeartBitmap, heathHearts.get(i).xLoc, heathHearts.get(i).yLoc, null);
            }
        }
    }

    public int getDamageTaken() {
        return damageTaken;
    }

    public void setDamageTaken(int damageTaken) {
        this.damageTaken = damageTaken;
    }

    public int getEnemysKilled() {
        return EnemysKilled;
    }

    public void setEnemysKilled(int enemysKilled) {
        EnemysKilled = enemysKilled;
    }

    public int getUserScore() {
        return userScore;
    }

    public void setUserScore(int userScore) {
        this.userScore = userScore;
    }

    public int getHeath() {
        return CurHeath;
    }

    public void setHeath(int heath) {
        //CurHeath = heath;
    }

    public int getMaxHeath() {
        return MaxHeath;
    }

    public int getGold() {
        return Gold;
    }

    public void setGold(int gold) {
        Gold = gold;
    }
}
