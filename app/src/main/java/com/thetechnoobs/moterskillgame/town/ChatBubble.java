package com.thetechnoobs.moterskillgame.town;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.thetechnoobs.moterskillgame.R;

public class ChatBubble {
    Bitmap chatBubbleCemWelcomBitmap;

    public ChatBubble(Resources resources, int[] screenSize) {

    chatBubbleCemWelcomBitmap = BitmapFactory.decodeResource(resources, R.drawable.chat_bubble_potions);

    }

    public Bitmap getChatBubbleCemWelcomBitmap() {
        return chatBubbleCemWelcomBitmap;
    }
}
