package com.brainzone.mindgames.models;

import java.util.ArrayList;
import java.util.List;

/**
 * Static word bank used by Word Scramble.
 * 48 unique words across 4 categories (12 each).
 */
public class WordData {

    public static class WordEntry {
        public final String word;
        public final String category;

        public WordEntry(String word, String category) {
            this.word     = word.toUpperCase();
            this.category = category;
        }
    }

    public static List<WordEntry> getAllWords() {
        List<WordEntry> list = new ArrayList<>();

        // ── Animals (12) ──────────────────────────────────────────────────────
        String[] animals = {
            "ELEPHANT", "DOLPHIN",  "GIRAFFE",  "PENGUIN",
            "CHEETAH",  "HAMSTER",  "PANTHER",  "GORILLA",
            "KANGAROO", "FLAMINGO", "CROCODILE","BUTTERFLY"
        };
        for (String w : animals) list.add(new WordEntry(w, "Animal"));

        // ── Countries (12) ────────────────────────────────────────────────────
        String[] countries = {
            "FRANCE",   "BRAZIL",   "CANADA",   "JORDAN",
            "KENYA",    "MEXICO",   "TURKEY",   "SWEDEN",
            "GREECE",   "NORWAY",   "PORTUGAL", "THAILAND"
        };
        for (String w : countries) list.add(new WordEntry(w, "Country"));

        // ── Food & Drinks (12) ────────────────────────────────────────────────
        String[] food = {
            "PIZZA",    "MANGO",    "BURGER",   "SUSHI",
            "PASTA",    "WAFFLE",   "CHEESE",   "TOMATO",
            "BANANA",   "CARROT",   "CHOCOLATE","AVOCADO"
        };
        for (String w : food) list.add(new WordEntry(w, "Food & Drink"));

        // ── Technology (12) ───────────────────────────────────────────────────
        String[] tech = {
            "LAPTOP",   "TABLET",   "KEYBOARD", "MONITOR",
            "BLUETOOTH","SCANNER",  "ROUTER",   "PRINTER",
            "SOFTWARE", "BROWSER",  "DATABASE", "NETWORK"
        };
        for (String w : tech) list.add(new WordEntry(w, "Technology"));

        return list;
    }
}
