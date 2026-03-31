package com.brainzone.mindgames;

import android.content.Intent;
import android.content.IntentFilter;
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
import java.util.Random;

public class Game1Activity extends AppCompatActivity {

    private TextView tvScore, tvTimer, tvQuestionCount, tvEquation, tvFeedback;
    private EditText etAnswer;
    private Button btnSubmit;
    private ImageButton btnBack;
    private SharedPreferences prefs;
    private String difficulty;
    private int score = 0;
    private int questionNumber = 1;
    private int totalQuestions = 10;
    private int correctAnswer;
    private CountDownTimer countDownTimer;
    private long timeLimit;
    private Random random = new Random();
    private BatteryLowReceiver batteryLowReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game1);

        prefs = getSharedPreferences("BrainZonePrefs", MODE_PRIVATE);
        difficulty = prefs.getString("difficulty", "easy");

        tvScore = findViewById(R.id.tvScore);
        tvTimer = findViewById(R.id.tvTimer);
        tvQuestionCount = findViewById(R.id.tvQuestionCount);
        tvEquation = findViewById(R.id.tvEquation);
        tvFeedback = findViewById(R.id.tvFeedback);
        etAnswer = findViewById(R.id.etAnswer);
        btnSubmit = findViewById(R.id.btnSubmit);
        btnBack = findViewById(R.id.btnBack);

        btnBack.setOnClickListener(v -> finish());

        setTimeLimit();
        generateQuestion();

        btnSubmit.setOnClickListener(v -> checkAnswer());

        batteryLowReceiver = new BatteryLowReceiver();
        registerReceiver(batteryLowReceiver, new IntentFilter(Intent.ACTION_BATTERY_LOW));
    }

    private void setTimeLimit() {
        switch (difficulty) {
            case "easy": timeLimit = 20000; break;
            case "medium": timeLimit = 12000; break;
            case "hard": timeLimit = 7000; break;
            default: timeLimit = 20000;
        }
    }

    private void generateQuestion() {
        if (questionNumber > totalQuestions) {
            endGame();
            return;
        }

        tvQuestionCount.setText(questionNumber + "/" + totalQuestions);
        etAnswer.setText("");
        tvFeedback.setVisibility(View.INVISIBLE);

        int a, b;
        int range = (difficulty.equals("easy")) ? 10 : (difficulty.equals("medium") ? 20 : 50);
        a = random.nextInt(range) + 1;
        b = random.nextInt(range) + 1;

        int product = a * b;
        
        if (difficulty.equals("medium") && random.nextBoolean()) {
             tvEquation.setText(product + " ÷ " + a + " = ?");
             correctAnswer = b;
        } else if (difficulty.equals("hard") && random.nextBoolean()) {
            tvEquation.setText("? × " + b + " = " + product);
            correctAnswer = a;
        } else {
            tvEquation.setText(a + " × " + b + " = ?");
            correctAnswer = product;
        }

        startTimer();
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
        countDownTimer.cancel();
        String input = etAnswer.getText().toString();
        if (!input.isEmpty()) {
            int userAnswer = Integer.parseInt(input);
            if (userAnswer == correctAnswer) {
                score += 10;
                showFeedback(true);
            } else {
                showFeedback(false);
            }
        } else {
            showFeedback(false);
        }
        
        tvScore.setText(getString(R.string.score_label, score));
        nextQuestion();
    }

    private void handleTimeout() {
        score -= 5;
        tvScore.setText(getString(R.string.score_label, score));
        showFeedback(false);
        nextQuestion();
    }

    private void showFeedback(boolean correct) {
        tvFeedback.setVisibility(View.VISIBLE);
        if (correct) {
            tvFeedback.setText(R.string.correct);
            tvFeedback.setTextColor(getResources().getColor(R.color.correct_color));
        } else {
            tvFeedback.setText(getString(R.string.wrong, String.valueOf(correctAnswer)));
            tvFeedback.setTextColor(getResources().getColor(R.color.wrong_color));
        }
    }

    private void nextQuestion() {
        btnSubmit.setEnabled(false);
        new Handler().postDelayed(() -> {
            questionNumber++;
            btnSubmit.setEnabled(true);
            generateQuestion();
        }, 1500);
    }

    private void endGame() {
        Intent intent = new Intent(this, GameResultActivity.class);
        intent.putExtra("game_name", "Multiplication Puzzle");
        intent.putExtra("score", score);
        intent.putExtra("difficulty", difficulty);
        startActivity(intent);
        
        Intent broadcastIntent = new Intent("com.brainzone.GAME_OVER");
        broadcastIntent.putExtra("game", "Multiplication Puzzle");
        broadcastIntent.putExtra("score", score);
        sendBroadcast(broadcastIntent);
        
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (countDownTimer != null) countDownTimer.cancel();
        unregisterReceiver(batteryLowReceiver);
    }
}