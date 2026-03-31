package com.brainzone.mindgames;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class Game2Activity extends AppCompatActivity implements MemoryAdapter.OnCardClickListener {

    private TextView tvMoves, tvTimer;
    private RecyclerView rvBoard;
    private ImageButton btnBack;
    private List<MemoryCard> cards;
    private MemoryAdapter adapter;
    private int moves = 0;
    private int pairsFound = 0;
    private int totalPairs;
    private int secondsElapsed = 0;
    private Timer timer;
    private SharedPreferences prefs;
    private String difficulty;
    private int indexOfSingleSelectedCard = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game2);

        prefs = getSharedPreferences("BrainZonePrefs", MODE_PRIVATE);
        difficulty = prefs.getString("difficulty", "easy");

        tvMoves = findViewById(R.id.tvMoves);
        tvTimer = findViewById(R.id.tvTimer);
        rvBoard = findViewById(R.id.rvBoard);
        btnBack = findViewById(R.id.btnBack);

        btnBack.setOnClickListener(v -> finish());

        setupGame();
        startTimer();
    }

    private void setupGame() {
        int rows, cols;
        switch (difficulty) {
            case "medium": rows = 4; cols = 4; totalPairs = 8; break;
            case "hard": rows = 5; cols = 4; totalPairs = 10; break;
            default: rows = 4; cols = 3; totalPairs = 6;
        }

        cards = new ArrayList<>();
        for (int i = 1; i <= totalPairs; i++) {
            cards.add(new MemoryCard(i));
            cards.add(new MemoryCard(i));
        }
        Collections.shuffle(cards);

        adapter = new MemoryAdapter(cards, this);
        rvBoard.setLayoutManager(new GridLayoutManager(this, cols));
        rvBoard.setAdapter(adapter);
    }

    private void startTimer() {
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(() -> {
                    secondsElapsed++;
                    tvTimer.setText(getString(R.string.timer_label, secondsElapsed));
                });
            }
        }, 1000, 1000);
    }

    @Override
    public void onCardClick(int position) {
        MemoryCard card = cards.get(position);
        if (card.isFlipped() || card.isMatched()) return;

        if (indexOfSingleSelectedCard == -1) {
            restoreCards();
            indexOfSingleSelectedCard = position;
            card.setFlipped(true);
        } else {
            checkForMatch(indexOfSingleSelectedCard, position);
            indexOfSingleSelectedCard = -1;
            moves++;
            tvMoves.setText(getString(R.string.moves_label, moves));
        }
        adapter.notifyDataSetChanged();
    }

    private void restoreCards() {
        for (MemoryCard card : cards) {
            if (!card.isMatched()) {
                card.setFlipped(false);
            }
        }
        adapter.notifyDataSetChanged();
    }

    private void checkForMatch(int pos1, int pos2) {
        if (cards.get(pos1).getValue() == cards.get(pos2).getValue()) {
            cards.get(pos1).setMatched(true);
            cards.get(pos2).setMatched(true);
            pairsFound++;
            if (pairsFound == totalPairs) {
                endGame();
            }
        } else {
            cards.get(pos1).setFlipped(true);
            cards.get(pos2).setFlipped(true);
            new Handler().postDelayed(() -> {
                 if (!cards.get(pos1).isMatched()) {
                     cards.get(pos1).setFlipped(false);
                     cards.get(pos2).setFlipped(false);
                     adapter.notifyDataSetChanged();
                 }
            }, 800);
        }
    }

    private void endGame() {
        timer.cancel();
        int finalScore = Math.max(0, 1000 - (moves * 15) - (secondsElapsed * 2));
        
        Intent intent = new Intent(this, GameResultActivity.class);
        intent.putExtra("game_name", "Memory Match");
        intent.putExtra("score", finalScore);
        intent.putExtra("moves", moves);
        intent.putExtra("difficulty", difficulty);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (timer != null) timer.cancel();
    }
}