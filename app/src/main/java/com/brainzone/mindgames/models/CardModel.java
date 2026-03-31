package com.brainzone.mindgames.models;

/**
 * Represents a single card in the Memory Match game.
 */
public class CardModel {

    public enum State { FACE_DOWN, FACE_UP, MATCHED }

    private final int  id;          // unique position index
    private final int  pairId;      // shared by two matching cards
    private final String symbol;    // displayed symbol/emoji when face-up
    private State state;

    public CardModel(int id, int pairId, String symbol) {
        this.id     = id;
        this.pairId = pairId;
        this.symbol = symbol;
        this.state  = State.FACE_DOWN;
    }

    // ── Getters ──────────────────────────────────────────────────────────────
    public int    getId()     { return id; }
    public int    getPairId() { return pairId; }
    public String getSymbol() { return symbol; }
    public State  getState()  { return state; }

    // ── State helpers ────────────────────────────────────────────────────────
    public boolean isFaceDown() { return state == State.FACE_DOWN; }
    public boolean isFaceUp()   { return state == State.FACE_UP; }
    public boolean isMatched()  { return state == State.MATCHED; }

    public void flipUp()    { state = State.FACE_UP; }
    public void flipDown()  { state = State.FACE_DOWN; }
    public void setMatched() { state = State.MATCHED; }
}
