package com.brainzone.mindgames;

public class LeaderboardItem {
    private String gameName;
    private String difficulty;
    private int score;

    public LeaderboardItem(String gameName, String difficulty, int score) {
        this.gameName = gameName;
        this.difficulty = difficulty;
        this.score = score;
    }

    public String getGameName() { return gameName; }
    public String getDifficulty() { return difficulty; }
    public int getScore() { return score; }
}