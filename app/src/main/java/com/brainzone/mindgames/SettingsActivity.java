package com.brainzone.mindgames;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.switchmaterial.SwitchMaterial;

public class SettingsActivity extends AppCompatActivity {

    private EditText etPlayerName;
    private SwitchMaterial switchMusic, switchSFX;
    private RadioGroup rgDifficulty;
    private Button btnReset, btnSave;
    private SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        prefs = getSharedPreferences("BrainZonePrefs", MODE_PRIVATE);

        etPlayerName = findViewById(R.id.etPlayerName);
        switchMusic = findViewById(R.id.switchMusic);
        switchSFX = findViewById(R.id.switchSFX);
        rgDifficulty = findViewById(R.id.rgDifficulty);
        btnReset = findViewById(R.id.btnReset);
        btnSave = findViewById(R.id.btnSave);

        loadSettings();

        btnSave.setOnClickListener(v -> saveSettings());
        btnReset.setOnClickListener(v -> showResetConfirmation());
    }

    private void loadSettings() {
        etPlayerName.setText(prefs.getString("player_name", ""));
        switchMusic.setChecked(prefs.getBoolean("music_enabled", true));
        switchSFX.setChecked(prefs.getBoolean("sfx_enabled", true));

        String difficulty = prefs.getString("difficulty", "easy");
        if (difficulty.equals("easy")) rgDifficulty.check(R.id.rbEasy);
        else if (difficulty.equals("medium")) rgDifficulty.check(R.id.rbMedium);
        else if (difficulty.equals("hard")) rgDifficulty.check(R.id.rbHard);
    }

    private void saveSettings() {
        String name = etPlayerName.getText().toString().trim();
        boolean music = switchMusic.isChecked();
        boolean sfx = switchSFX.isChecked();
        
        String difficulty = "easy";
        int checkedId = rgDifficulty.getCheckedRadioButtonId();
        if (checkedId == R.id.rbMedium) difficulty = "medium";
        else if (checkedId == R.id.rbHard) difficulty = "hard";

        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("player_name", name);
        editor.putBoolean("music_enabled", music);
        editor.putBoolean("sfx_enabled", sfx);
        editor.putString("difficulty", difficulty);
        editor.apply();

        if (music) {
            startService(new Intent(this, BackgroundMusicService.class));
        } else {
            stopService(new Intent(this, BackgroundMusicService.class));
        }

        Toast.makeText(this, "Settings Saved", Toast.LENGTH_SHORT).show();
        finish();
    }

    private void showResetConfirmation() {
        new AlertDialog.Builder(this)
                .setTitle("Reset All Data")
                .setMessage("Are you sure you want to reset all scores and settings?")
                .setPositiveButton("Yes", (dialog, which) -> {
                    prefs.edit().clear().apply();
                    loadSettings();
                    Toast.makeText(SettingsActivity.this, "Data Reset", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("No", null)
                .show();
    }
}