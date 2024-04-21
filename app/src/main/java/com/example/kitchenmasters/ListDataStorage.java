package com.example.kitchenmasters;

import java.util.ArrayList;

public class ListDataStorage {
    private static final ListDataStorage instance = new ListDataStorage();
    private final ArrayList<String> detectedTexts = new ArrayList<>();

    private ListDataStorage() {

    }

    public static ListDataStorage getInstance() {
        return instance;
    }

    public synchronized void addDetectedText(String text) {
        if (!detectedTexts.contains(text)) {
            detectedTexts.add(text);
        }
    }

    public synchronized ArrayList<String> getDetectedTexts() {
        return new ArrayList<>(detectedTexts); // Return a copy to prevent modification
    }

    public synchronized void clearDetectedTexts() {
        detectedTexts.clear();
    }
}
