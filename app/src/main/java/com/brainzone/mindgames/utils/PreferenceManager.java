package com.brainzone.mindgames.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Centralised manager for all SharedPreferences read/write operations.
 */
public class PreferenceManager {

    private static final String PREF_NAME = "BrainZonePrefs";

    // ── Keys ──────────────────────────────────────────────────────────────────
    public static final String KEY_PLAYER_NAME    = "player_name";
    public static final String KEY_MUSIC_ENABLED  = "music_enabled";
    public static final String KEY_SFX_ENABLED    = "sfx_enabled";
    public static final String KEY_DIFFICULTY     = "difficulty";

    // Difficulty constants
    public static final String DIFF_EASY   = "easy";
    public static final String DIFF_MEDIUM = "medium";
    public static final String DIFF_HARD   = "hard";

    // Game keys
    public static final String GAME_MULT    = "multiplication";
    public static final String GAME_MEMORY  = "memory";
    public static final String GAME_SCRAMBLE = "scramble";

    private final SharedPreferences prefs;
    private final SharedPreferences.Editor editor;

    public PreferenceManager(Context context) {
        prefs  = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = prefs.edit();
    }

    // ── Player ────────────────────────────────────────────────────────────────
    public void setPlayerName(String name) { editor.putString(KEY_PLAYER_NAME, name).apply(); }
    public String getPlayerName()          { return prefs.getString(KEY_PLAYER_NAME, ""); }

    // ── Sound & Music ─────────────────────────────────────────────────────────
    public void setMusicEnabled(boolean on) { editor.putBoolean(KEY_MUSIC_ENABLED, on).apply(); }
    public boolean isMusicEnabled()         { return prefs.getBoolean(KEY_MUSIC_ENABLED, true); }

    public void setSfxEnabled(boolean on)   { editor.putBoolean(KEY_SFX_ENABLED, on).apply(); }
    public boolean isSfxEnabled()           { return prefs.getBoolean(KEY_SFX_ENABLED, true); }

    // ── Difficulty ────────────────────────────────────────────────────────────
    public void setDifficulty(String d) { editor.putString(KEY_DIFFICULTY, d).apply(); }
    public String getDifficulty()       { return prefs.getString(KEY_DIFFICULTY, DIFF_EASY); }

    // ── Scores (Multiplication & Scramble — higher is better) ─────────────────
    public void saveHighScore(String game, String difficulty, int score) {
        String key = buildScoreKey(game, difficulty);
        if (score > prefs.getInt(key, 0)) {
            editor.putInt(key, score).apply();
        }
    }

    public int getHighScore(String game, String difficulty) {
        return prefs.getInt(buildScoreKey(game, difficulty), 0);
    }

    // ── Memory Match (lower moves = better) ───────────────────────────────────
    public void saveMemoryMoves(String difficulty, int moves) {
        String key = buildScoreKey(GAME_MEMORY, difficulty);
        int current = prefs.getInt(key, Integer.MAX_VALUE);
        if (moves < current) {
            editor.putInt(key, moves).apply();
        }
    }

    public int getBestMemoryMoves(String difficulty) {
        return prefs.getInt(buildScoreKey(GAME_MEMORY, difficulty), 0);
    }

    // ── Overall high score for home screen ────────────────────────────────────
    public int getOverallHighScore() {
        int best = 0;
        for (String diff : new String[]{DIFF_EASY, DIFF_MEDIUM, DIFF_HARD}) {
            best = Math.max(best, getHighScore(GAME_MULT, diff));
            best = Math.max(best, getHighScore(GAME_SCRAMBLE, diff));
        }
        return best;
    }

    // ── Reset helpers ─────────────────────────────────────────────────────────
    public void clearAllScores() {
        for (String game : new String[]{GAME_MULT, GAME_MEMORY, GAME_SCRAMBLE}) {
            for (String diff : new String[]{DIFF_EASY, DIFF_MEDIUM, DIFF_HARD}) {
                editor.remove(buildScoreKey(game, diff));
            }
        }
        editor.apply();
    }

    public void clearAllData() {
        editor.clear().apply();
    }

    // ── Internal ──────────────────────────────────────────────────────────────
    private String buildScoreKey(String game, String difficulty) {
        return "score_" + game + "_" + difficulty;
    }
}
