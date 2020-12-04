package com.thetechnoobs.moterskillgame.town;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.thetechnoobs.moterskillgame.R;

public class ChatBubble {
    Bitmap chatBubbleCemWelcomBitmap, chatBubbleWeponWelcomBitmap;

    public ChatBubble(Resources resources, int[] screenSize) {

    chatBubbleCemWelcomBitmap = BitmapFactory.decodeResource(resources, R.drawable.chat_bubble_potions);
    chatBubbleWeponWelcomBitmap = BitmapFactory.decodeResource(resources, R.drawable.chat_bubble_wepons);

    }

    public Bitmap getChatBubbleCemWelcomBitmap() {
        return chatBubbleCemWelcomBitmap;
    }

    public Bitmap getChatBubbleWeponWelcomBitmap(){
        return chatBubbleWeponWelcomBitmap;
    }
}
