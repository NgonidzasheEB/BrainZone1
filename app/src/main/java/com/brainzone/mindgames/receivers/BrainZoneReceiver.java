package com.brainzone.mindgames.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

/**
 * BroadcastReceiver handling:
 *  1. ACTION_BATTERY_LOW  — pause game and notify user
 *  2. ACTION_SCREEN_OFF   — pause active game timer
 *  3. com.brainzone.mindgames.GAME_OVER — custom game-over event
 *
 * Registration:
 *  - Static (manifest) for BATTERY_LOW
 *  - Dynamic (registerReceiver) for SCREEN_OFF and GAME_OVER inside game activities
 */
public class BrainZoneReceiver extends BroadcastReceiver {

    public static final String ACTION_GAME_OVER = "com.brainzone.mindgames.GAME_OVER";
    public static final String EXTRA_GAME_NAME  = "game_name";
    public static final String EXTRA_SCORE      = "score";

    private static final String TAG = "BrainZoneReceiver";

    /** Callback interface so activities can react to broadcasts. */
    public interface GameEventListener {
        void onBatteryLow();
        void onScreenOff();
        void onGameOver(String gameName, int score);
    }

    private GameEventListener listener;

    public BrainZoneReceiver() {}

    public BrainZoneReceiver(GameEventListener listener) {
        this.listener = listener;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent == null || intent.getAction() == null) return;

        switch (intent.getAction()) {

            case Intent.ACTION_BATTERY_LOW:
                Log.d(TAG, "Battery low — pause game");
                Toast.makeText(context,
                    "⚡ Low battery! Please charge your device.", Toast.LENGTH_LONG).show();
                if (listener != null) listener.onBatteryLow();
                break;

            case Intent.ACTION_SCREEN_OFF:
                Log.d(TAG, "Screen off — pause game timer");
                if (listener != null) listener.onScreenOff();
                break;

            case ACTION_GAME_OVER:
                String gameName = intent.getStringExtra(EXTRA_GAME_NAME);
                int score       = intent.getIntExtra(EXTRA_SCORE, 0);
                Log.d(TAG, "Game over: " + gameName + " score=" + score);
                if (listener != null) listener.onGameOver(gameName, score);
                break;

            default:
                Log.d(TAG, "Unknown action: " + intent.getAction());
        }
    }
}
