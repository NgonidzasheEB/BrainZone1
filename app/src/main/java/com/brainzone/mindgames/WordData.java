package com.brainzone.mindgames;

import java.util.ArrayList;
import java.util.List;

public class WordData {
    public static class WordItem {
        String word;
        String category;

        public WordItem(String word, String category) {
            this.word = word;
            this.category = category;
        }
    }

    public static List<WordItem> getWords() {
        List<WordItem> words = new ArrayList<>();
        words.add(new WordItem("LION", "Animal"));
        words.add(new WordItem("TIGER", "Animal"));
        words.add(new WordItem("ELEPHANT", "Animal"));
        words.add(new WordItem("GIRAFFE", "Animal"));
        words.add(new WordItem("MONKEY", "Animal"));
        words.add(new WordItem("ZEBRA", "Animal"));
        words.add(new WordItem("PANDA", "Animal"));
        words.add(new WordItem("KANGAROO", "Animal"));
        words.add(new WordItem("DOLPHIN", "Animal"));
        words.add(new WordItem("CHICKEN", "Animal"));

        words.add(new WordItem("FRANCE", "Country"));
        words.add(new WordItem("BRAZIL", "Country"));
        words.add(new WordItem("CANADA", "Country"));
        words.add(new WordItem("JAPAN", "Country"));
        words.add(new WordItem("GERMANY", "Country"));
        words.add(new WordItem("ITALY", "Country"));
        words.add(new WordItem("MEXICO", "Country"));
        words.add(new WordItem("EGYPT", "Country"));
        words.add(new WordItem("AUSTRALIA", "Country"));
        words.add(new WordItem("INDIA", "Country"));

        words.add(new WordItem("PIZZA", "Food & Drink"));
        words.add(new WordItem("BURGER", "Food & Drink"));
        words.add(new WordItem("PASTA", "Food & Drink"));
        words.add(new WordItem("SUSHI", "Food & Drink"));
        words.add(new WordItem("COFFEE", "Food & Drink"));
        words.add(new WordItem("ORANGE", "Food & Drink"));
        words.add(new WordItem("BANANA", "Food & Drink"));
        words.add(new WordItem("CHEESE", "Food & Drink"));
        words.add(new WordItem("CHOCOLATE", "Food & Drink"));
        words.add(new WordItem("CHICKEN", "Food & Drink"));

        words.add(new WordItem("COMPUTER", "Technology"));
        words.add(new WordItem("INTERNET", "Technology"));
        words.add(new WordItem("SOFTWARE", "Technology"));
        words.add(new WordItem("KEYBOARD", "Technology"));
        words.add(new WordItem("MONITOR", "Technology"));
        words.add(new WordItem("LAPTOP", "Technology"));
        words.add(new WordItem("PHONE", "Technology"));
        words.add(new WordItem("ROBOT", "Technology"));
        words.add(new WordItem("NETWORK", "Technology"));
        words.add(new WordItem("CAMERA", "Technology"));

        return words;
    }
}