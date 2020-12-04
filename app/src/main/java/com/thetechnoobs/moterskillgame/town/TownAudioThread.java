package com.thetechnoobs.moterskillgame.town;

import android.content.Context;
import android.media.MediaPlayer;

import com.thetechnoobs.moterskillgame.R;

public class TownAudioThread {
    Context context;
    MediaPlayer mediaPlayer;
    UserWalkingSoundThread userWalkingSoundThread = new UserWalkingSoundThread();

    public TownAudioThread(Context context) {
        this.context = context;
    }

    public class UserWalkingSoundThread implements Runnable {
        public void run() {
            mediaPlayer = MediaPlayer.create(context, R.raw.hit_by_asteroid);
            mediaPlayer.start();
        }
    }
}
