package com.thetechnoobs.moterskillgame.town;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.RectF;

import com.thetechnoobs.moterskillgame.R;

public class UserCharacter {
    Bitmap User;
    int[] screenSize;
    int CurX, CurY;
    int Spritewidth, Spriteheight;
    int speed = TownConstants.USER_SPEED;
    int xSpriteSheetLoc = 1;
    int ySpriteSheetLoc = 4;

    public UserCharacter(int curX, int curY, int[] screenSize, Resources resources) {
        CurX = curX;
        CurY = curY;
        this.screenSize = screenSize;

        User = BitmapFactory.decodeResource(resources, R.drawable.male_blue_spritesheet);

        settupSpriteSheet();

    }

    private void settupSpriteSheet() {
        Spritewidth = User.getWidth() / 4;
        Spriteheight = User.getHeight() / 4;
    }

    public void update() {

    }

    public void moveUser(int direction) {
        //up=0 right=1 down=2 left=3

        switch (direction) {
            case 0:
                if (getCurY() > 0) {
                    setCurY(getCurY() - speed);
                    updateSprite(direction);
                }
                return;
            case 1:
                if (getCurX() + Spritewidth < screenSize[0]) {
                    setCurX(getCurX() + speed);
                    updateSprite(direction);
                }
                return;
            case 2:
                if (getCurY() + Spriteheight <= screenSize[1]) {
                    setCurY(getCurY() + speed);
                    updateSprite(direction);
                }
                return;
            case 3:
                if (getCurX() >= 0) {
                    setCurX(getCurX() - speed);
                    updateSprite(direction);
                }
                return;
        }

        updateSprite(direction);
    }

    public void updateSprite(int direction) {
        //up=0 right=1 down=2 left=3
        xSpriteSheetLoc++;
        if (xSpriteSheetLoc > 4) {
            xSpriteSheetLoc = 1;
        }


        switch (direction) {
            case 0:
                ySpriteSheetLoc = 2;
                return;
            case 1:
                ySpriteSheetLoc = 4;
                return;
            case 2:
                ySpriteSheetLoc = 1;
                return;
            case 3:
                ySpriteSheetLoc = 3;
                return;
        }


    }

    public void draw(Canvas canvas) {
        int x = Spritewidth * xSpriteSheetLoc - Spritewidth;
        int y = Spriteheight * ySpriteSheetLoc - Spriteheight;

        Rect spriteFrame = new Rect(x, y, x + Spritewidth, y + Spriteheight);
        RectF destination = new RectF(CurX, CurY, (float) (CurX + Spritewidth * 1.5), (float) (CurY + Spriteheight * 1.5));
        canvas.drawBitmap(User, spriteFrame, destination, null);
    }

    public RectF getHitbox() {
        return new RectF(CurX, CurY, CurX + Spritewidth, CurY + Spriteheight);
    }

    public int getCurX() {
        return CurX;
    }

    public void setCurX(int curX) {
        CurX = curX;
    }

    public int getCurY() {
        return CurY;
    }

    public void setCurY(int curY) {
        CurY = curY;
    }
}
