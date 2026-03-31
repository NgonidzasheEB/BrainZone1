package com.brainzone.mindgames;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;

public class BackgroundMusicService extends Service {
    private MediaPlayer mediaPlayer;

    @Override
    public void onCreate() {
        super.onCreate();
        // Assuming a raw resource background_music.mp3 exists. 
        // For now, I'll use a placeholder or handle it gracefully if missing.
        // mediaPlayer = MediaPlayer.create(this, R.raw.background_music);
        // if (mediaPlayer != null) {
        //     mediaPlayer.setLooping(true);
        //     mediaPlayer.setVolume(0.5f, 0.5f);
        // }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (mediaPlayer != null && !mediaPlayer.isPlaying()) {
            mediaPlayer.start();
        }
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
        }
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}