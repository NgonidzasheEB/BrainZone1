package com.brainzone.mindgames;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class LeaderboardActivity extends AppCompatActivity {

    private RecyclerView rvLeaderboard;
    private Button btnClearScores;
    private LeaderboardAdapter adapter;
    private List<LeaderboardItem> leaderboardItems;
    private SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaderboard);

        prefs = getSharedPreferences("BrainZonePrefs", MODE_PRIVATE);
        rvLeaderboard = findViewById(R.id.rvLeaderboard);
        btnClearScores = findViewById(R.id.btnClearScores);

        loadLeaderboard();

        btnClearScores.setOnClickListener(v -> showClearConfirmation());
    }

    private void loadLeaderboard() {
        leaderboardItems = new ArrayList<>();
        
        // Load high scores for each game and difficulty
        String[] difficulties = {"easy", "medium", "hard"};
        
        for (String diff : difficulties) {
            int score1 = prefs.getInt("score_multiplication_" + diff, 0);
            if (score1 > 0) leaderboardItems.add(new LeaderboardItem("Multiplication Puzzle", diff, score1));
            
            int score2 = prefs.getInt("score_memory_" + diff, 0);
            if (score2 > 0) leaderboardItems.add(new LeaderboardItem("Memory Match (Moves)", diff, score2));
            
            int score3 = prefs.getInt("score_scramble_" + diff, 0);
            if (score3 > 0) leaderboardItems.add(new LeaderboardItem("Word Scramble", diff, score3));
        }

        adapter = new LeaderboardAdapter(leaderboardItems);
        rvLeaderboard.setLayoutManager(new LinearLayoutManager(this));
        rvLeaderboard.setAdapter(adapter);
    }

    private void showClearConfirmation() {
        new AlertDialog.Builder(this)
                .setTitle(R.string.clear_leaderboard)
                .setMessage(R.string.confirm_clear)
                .setPositiveButton(R.string.yes, (dialog, which) -> {
                    SharedPreferences.Editor editor = prefs.edit();
                    String[] difficulties = {"easy", "medium", "hard"};
                    for (String diff : difficulties) {
                        editor.remove("score_multiplication_" + diff);
                        editor.remove("score_memory_" + diff);
                        editor.remove("score_scramble_" + diff);
                    }
                    editor.apply();
                    loadLeaderboard();
                })
                .setNegativeButton(R.string.no, null)
                .show();
    }
}