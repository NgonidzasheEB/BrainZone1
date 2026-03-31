package com.brainzone.mindgames;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Game3Activity extends AppCompatActivity {

    private TextView tvScore, tvTimer, tvWordCount, tvCategory, tvScrambledWord, tvFeedback;
    private EditText etAnswer;
    private Button btnSubmit, btnHint;
    private ImageButton btnBack;
    private SharedPreferences prefs;
    private String difficulty;
    private int score = 0;
    private int wordIndex = 0;
    private int totalWords = 10;
    private int attempts = 0;
    private boolean hintUsed = false;
    private List<WordData.WordItem> gameWords;
    private WordData.WordItem currentWordItem;
    private String scrambledWord;
    private CountDownTimer countDownTimer;
    private long timeLimit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game3);

        prefs = getSharedPreferences("BrainZonePrefs", MODE_PRIVATE);
        difficulty = prefs.getString("difficulty", "easy");

        tvScore = findViewById(R.id.tvScore);
        tvTimer = findViewById(R.id.tvTimer);
        tvWordCount = findViewById(R.id.tvWordCount);
        tvCategory = findViewById(R.id.tvCategory);
        tvScrambledWord = findViewById(R.id.tvScrambledWord);
        tvFeedback = findViewById(R.id.tvFeedback);
        etAnswer = findViewById(R.id.etAnswer);
        btnSubmit = findViewById(R.id.btnSubmit);
        btnHint = findViewById(R.id.btnHint);
        btnBack = findViewById(R.id.btnBack);

        btnBack.setOnClickListener(v -> finish());

        setTimeLimit();
        loadWords();
        nextWord();

        btnSubmit.setOnClickListener(v -> checkAnswer());
        btnHint.setOnClickListener(v -> useHint());
    }

    private void setTimeLimit() {
        switch (difficulty) {
            case "easy": timeLimit = 30000; break;
            case "medium": timeLimit = 20000; break;
            case "hard": timeLimit = 12000; break;
            default: timeLimit = 30000;
        }
    }

    private void loadWords() {
        gameWords = WordData.getWords();
        Collections.shuffle(gameWords);
        gameWords = gameWords.subList(0, totalWords);
    }

    private void nextWord() {
        if (wordIndex >= totalWords) {
            endGame();
            return;
        }

        attempts = 0;
        hintUsed = false;
        btnHint.setEnabled(true);
        currentWordItem = gameWords.get(wordIndex);
        scrambledWord = scrambleWord(currentWordItem.word);
        
        tvWordCount.setText((wordIndex + 1) + "/" + totalWords);
        tvCategory.setText(getString(R.string.category_hint, currentWordItem.category));
        tvScrambledWord.setText(scrambledWord);
        etAnswer.setText("");
        tvFeedback.setVisibility(View.INVISIBLE);

        startTimer();
    }

    private String scrambleWord(String word) {
        List<Character> characters = new ArrayList<>();
        for (char c : word.toCharArray()) {
            characters.add(c);
        }
        String scrambled;
        do {
            Collections.shuffle(characters);
            StringBuilder sb = new StringBuilder();
            for (char c : characters) {
                sb.append(c).append(" ");
            }
            scrambled = sb.toString().trim();
        } while (scrambled.replace(" ", "").equals(word) && word.length() > 1);
        
        return scrambled;
    }

    private void startTimer() {
        if (countDownTimer != null) countDownTimer.cancel();
        countDownTimer = new CountDownTimer(timeLimit, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                tvTimer.setText(getString(R.string.timer_label, (int) (millisUntilFinished / 1000)));
            }

            @Override
            public void onFinish() {
                handleTimeout();
            }
        }.start();
    }

    private void checkAnswer() {
        String input = etAnswer.getText().toString().trim().toUpperCase();
        if (input.equals(currentWordItem.word)) {
            countDownTimer.cancel();
            int points = 0;
            if (attempts == 0) points = 15;
            else if (attempts == 1) points = 8;
            else if (attempts == 2) points = 3;
            
            score += points;
            tvScore.setText(getString(R.string.score_label, score));
            showFeedback(true);
            moveToNextWithDelay();
        } else {
            attempts++;
            if (attempts >= 3) {
                countDownTimer.cancel();
                showFeedback(false);
                moveToNextWithDelay();
            } else {
                tvFeedback.setVisibility(View.VISIBLE);
                tvFeedback.setText("Incorrect! " + (3 - attempts) + " attempts left.");
                tvFeedback.setTextColor(getResources().getColor(R.color.wrong_color));
            }
        }
    }

    private void useHint() {
        if (!hintUsed && score >= 5) {
            score -= 5;
            hintUsed = true;
            btnHint.setEnabled(false);
            tvScore.setText(getString(R.string.score_label, score));
            char firstLetter = currentWordItem.word.charAt(0);
            etAnswer.setText(String.valueOf(firstLetter));
            etAnswer.setSelection(etAnswer.getText().length());
        }
    }

    private void handleTimeout() {
        showFeedback(false);
        moveToNextWithDelay();
    }

    private void showFeedback(boolean correct) {
        tvFeedback.setVisibility(View.VISIBLE);
        if (correct) {
            tvFeedback.setText(R.string.correct);
            tvFeedback.setTextColor(getResources().getColor(R.color.correct_color));
        } else {
            tvFeedback.setText(getString(R.string.wrong, currentWordItem.word));
            tvFeedback.setTextColor(getResources().getColor(R.color.wrong_color));
        }
    }

    private void moveToNextWithDelay() {
        btnSubmit.setEnabled(false);
        new Handler().postDelayed(() -> {
            wordIndex++;
            btnSubmit.setEnabled(true);
            nextWord();
        }, 2000);
    }

    private void endGame() {
        Intent intent = new Intent(this, GameResultActivity.class);
        intent.putExtra("game_name", "Word Scramble");
        intent.putExtra("score", score);
        intent.putExtra("difficulty", difficulty);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (countDownTimer != null) countDownTimer.cancel();
    }
}