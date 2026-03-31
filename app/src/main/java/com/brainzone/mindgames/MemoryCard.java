package com.brainzone.mindgames;

public class MemoryCard {
    private int value;
    private boolean isFlipped;
    private boolean isMatched;

    public MemoryCard(int value) {
        this.value = value;
        this.isFlipped = false;
        this.isMatched = false;
    }

    public int getValue() { return value; }
    public boolean isFlipped() { return isFlipped; }
    public void setFlipped(boolean flipped) { isFlipped = flipped; }
    public boolean isMatched() { return isMatched; }
    public void setMatched(boolean matched) { isMatched = matched; }
}