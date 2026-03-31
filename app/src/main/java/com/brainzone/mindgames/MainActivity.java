package com.brainzone.mindgames;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.card.MaterialCardView;

public class MainActivity extends AppCompatActivity {

    private TextView tvWelcome, tvHighScore;
    private View cardGame1, cardGame2, cardGame3;
    private Button btnLeaderboard, btnSettings;
    private SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        prefs = getSharedPreferences("BrainZonePrefs", MODE_PRIVATE);

        tvWelcome = findViewById(R.id.tvWelcome);
        tvHighScore = findViewById(R.id.tvHighScore);
        
        // Using IDs from the new card-based layout
        cardGame1 = findViewById(R.id.cardGame1);
        cardGame2 = findViewById(R.id.cardGame2);
        cardGame3 = findViewById(R.id.cardGame3);
        
        btnLeaderboard = findViewById(R.id.btnLeaderboard);
        btnSettings = findViewById(R.id.btnSettings);

        updateUI();

        cardGame1.setOnClickListener(v -> startGame(Game1Activity.class));
        cardGame2.setOnClickListener(v -> startGame(Game2Activity.class));
        cardGame3.setOnClickListener(v -> startGame(Game3Activity.class));

        btnLeaderboard.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, LeaderboardActivity.class)));
        btnSettings.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, SettingsActivity.class)));

        startBackgroundMusic();
    }

    private void updateUI() {
        String name = prefs.getString("player_name", "Player");
        tvWelcome.setText(getString(R.string.welcome_message, name));

        int totalHigh = prefs.getInt("score_multiplication_easy", 0) +
                        prefs.getInt("score_multiplication_medium", 0) +
                        prefs.getInt("score_multiplication_hard", 0) +
                        prefs.getInt("score_scramble_easy", 0) +
                        prefs.getInt("score_scramble_medium", 0) +
                        prefs.getInt("score_scramble_hard", 0);
        
        tvHighScore.setText(getString(R.string.all_time_high_score, totalHigh));
    }

    private void startGame(Class<?> activityClass) {
        Intent intent = new Intent(this, activityClass);
        startActivity(intent);
    }

    private void startBackgroundMusic() {
        if (prefs.getBoolean("music_enabled", true)) {
            Intent intent = new Intent(this, BackgroundMusicService.class);
            startService(intent);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateUI();
    }
}