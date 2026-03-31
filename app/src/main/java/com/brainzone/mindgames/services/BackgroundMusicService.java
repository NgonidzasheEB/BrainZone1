package com.brainzone.mindgames.services;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.util.Log;

import com.brainzone.mindgames.R;
import com.brainzone.mindgames.utils.PreferenceManager;

/**
 * Background music service.
 * Plays res/raw/bg_music.mp3 in a loop during gameplay.
 *
 * NOTE: Add a file named bg_music.mp3 to app/src/main/res/raw/
 *       to enable background music.
 */
public class BackgroundMusicService extends Service {

    private static final String TAG = "BGMusicService";
    private MediaPlayer mediaPlayer;
    private PreferenceManager prefManager;

    @Override
    public void onCreate() {
        super.onCreate();
        prefManager = new PreferenceManager(this);
        Log.d(TAG, "BackgroundMusicService created");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (prefManager.isMusicEnabled()) {
            startMusic();
        }
        return START_STICKY;
    }

    private void startMusic() {
        try {
            if (mediaPlayer == null) {
                // Replace R.raw.bg_music with your actual audio resource
                mediaPlayer = MediaPlayer.create(this, R.raw.bg_music);
                if (mediaPlayer != null) {
                    mediaPlayer.setLooping(true);
                    mediaPlayer.setVolume(0.5f, 0.5f);
                    mediaPlayer.start();
                    Log.d(TAG, "Music started");
                }
            } else if (!mediaPlayer.isPlaying()) {
                mediaPlayer.start();
            }
        } catch (Exception e) {
            // Audio file not found or error — service continues silently
            Log.w(TAG, "Could not play music: " + e.getMessage());
        }
    }

    private void stopMusic() {
        if (mediaPlayer != null) {
            try {
                if (mediaPlayer.isPlaying()) mediaPlayer.stop();
            } catch (Exception e) {
                Log.w(TAG, "Error stopping music: " + e.getMessage());
            }
        }
    }

    /** Called when music toggle changes in Settings. */
    public void refreshMusicState() {
        if (prefManager.isMusicEnabled()) {
            startMusic();
        } else {
            stopMusic();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            try {
                mediaPlayer.stop();
                mediaPlayer.release();
            } catch (Exception e) {
                Log.w(TAG, "Error releasing MediaPlayer: " + e.getMessage());
            }
            mediaPlayer = null;
        }
        Log.d(TAG, "BackgroundMusicService destroyed");
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null; // Started service only
    }
}
