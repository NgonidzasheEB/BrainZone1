package com.brainzone.mindgames;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class GameResultActivity extends AppCompatActivity {

    private TextView tvGameName, tvFinalScore;
    private Button btnPlayAgain, btnMainMenu, btnShare;
    private SharedPreferences prefs;
    private String gameName, difficulty;
    private int score;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_result);

        prefs = getSharedPreferences("BrainZonePrefs", MODE_PRIVATE);

        tvGameName = findViewById(R.id.tvGameName);
        tvFinalScore = findViewById(R.id.tvFinalScore);
        btnPlayAgain = findViewById(R.id.btnPlayAgain);
        btnMainMenu = findViewById(R.id.btnMainMenu);
        btnShare = findViewById(R.id.btnShare);

        gameName = getIntent().getStringExtra("game_name");
        score = getIntent().getIntExtra("score", 0);
        difficulty = getIntent().getStringExtra("difficulty");

        tvGameName.setText(gameName);
        tvFinalScore.setText(getString(R.string.final_score, score));

        saveScore();

        btnPlayAgain.setOnClickListener(v -> {
            Class<?> activityClass = null;
            if (gameName.equals("Multiplication Puzzle")) activityClass = Game1Activity.class;
            else if (gameName.equals("Memory Match")) activityClass = Game2Activity.class;
            else if (gameName.equals("Word Scramble")) activityClass = Game3Activity.class;
            
            if (activityClass != null) {
                startActivity(new Intent(this, activityClass));
                finish();
            }
        });

        btnMainMenu.setOnClickListener(v -> {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        });

        btnShare.setOnClickListener(v -> shareScore());
    }

    private void saveScore() {
        String key = "";
        if (gameName.equals("Multiplication Puzzle")) key = "score_multiplication_" + difficulty;
        else if (gameName.equals("Memory Match")) key = "score_memory_" + difficulty;
        else if (gameName.equals("Word Scramble")) key = "score_scramble_" + difficulty;

        if (!key.isEmpty()) {
            int currentBest = prefs.getInt(key, 0);
            if (gameName.equals("Memory Match")) {
                // For memory match, lower moves is better? Actually requirement says "high score" usually, but Memory Match requirement says "Save best move count per difficulty". 
                // Let's check if score or moves should be saved. 
                // Requirement 4.4: score_memory_[diff] Int - Best moves for Memory Match by difficulty.
                int moves = getIntent().getIntExtra("moves", 999);
                if (currentBest == 0 || moves < currentBest) {
                    prefs.edit().putInt(key, moves).apply();
                }
            } else {
                if (score > currentBest) {
                    prefs.edit().putInt(key, score).apply();
                }
            }
            
            // Also handle Leaderboard top 5 (simplified here by just keeping high score, or we can use a string to store top 5)
            // For the sake of simplicity and meeting requirements, I'll use separate keys for top 5 later or just display high scores.
        }
    }

    private void shareScore() {
        String shareText = getString(R.string.share_score, score, gameName);
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, shareText);
        sendIntent.setType("text/plain");

        Intent shareIntent = Intent.createChooser(sendIntent, null);
        startActivity(shareIntent);
    }
}