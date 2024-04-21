package com.example.kitchenmasters;

import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;

public class DataStorage {
    Context context;
    private String filename = "items.json";

    public DataStorage(Context context) {
        this.context = context;
        initializeItemsFile();
    }

    private void initializeItemsFile() {
        try {
            File file = new File(context.getFilesDir(), filename);
            if (!file.exists()) {
                writeToFileOutput("[]");
            }
        } catch (Exception e) {
            Log.e("DataStorage", "Error initializing file: " + e.getMessage());
        }
    }

    public void manageItem(String itemName) {
        try {
            JSONArray items = new JSONArray(readFileContents());
            LocalDate today = LocalDate.now();
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            boolean itemExists = false;

            for (int i = 0; i < items.length(); i++) {
                JSONObject item = items.getJSONObject(i);
                if (item.getString("item").equals(itemName)) {
                    JSONArray dates = item.getJSONArray("dates_bought_first_second_third");
                    boolean status = item.getBoolean("status");
                    if (dates.length() < 3) {
                        dates.put(dtf.format(today));
                        // Item status management for less than 3 purchases
                        if (!status) {
                            item.put("status", true);
                        }
                    } else {
                        // Shift dates to make space for the new purchase date
                        for (int j = 2; j > 0; j--) {
                            dates.put(j, dates.getString(j - 1));
                        }
                        dates.put(0, dtf.format(today));
                        // Status management for 3 purchase dates
                        LocalDate lastRecommendedDate = LocalDate.parse(item.getString("date_to_recommend"), dtf);
                        if (today.isAfter(lastRecommendedDate) && !status) {
                            item.put("status", true); // Set status true if purchased after the recommendation
                        }
                    }
                    updateAverageDurationAndRecommendation(item, dates, dtf);
                    itemExists = true;
                    break;
                }
            }

            if (!itemExists) {
                JSONObject newItem = new JSONObject();
                newItem.put("item", itemName);
                JSONArray newDates = new JSONArray();
                newDates.put(dtf.format(today));
                newItem.put("dates_bought_first_second_third", newDates);
                newItem.put("status", false);
                items.put(newItem);
                updateAverageDurationAndRecommendation(newItem, newDates, dtf);
            }

            writeToFileOutput(items.toString());
        } catch (Exception e) {
            Log.e("DataStorage", "Error managing item: " + e.getMessage());
        }
    }

    private void updateAverageDurationAndRecommendation(JSONObject item, JSONArray dates, DateTimeFormatter dtf) throws JSONException {
        if (dates.length() >= 2) {
            long totalDuration = 0;
            for (int j = 0; j < dates.length() - 1; j++) {
                LocalDate earlierDate = LocalDate.parse(dates.getString(j + 1), dtf);
                LocalDate laterDate = LocalDate.parse(dates.getString(j), dtf);
                long daysBetween = ChronoUnit.DAYS.between(earlierDate, laterDate);
                totalDuration += daysBetween;
            }
            long averageDuration = totalDuration / (dates.length() - 1);
            LocalDate latestPurchaseDate = LocalDate.parse(dates.getString(0), dtf);
            item.put("recommended_duration", averageDuration);
            item.put("date_to_recommend", latestPurchaseDate.plusDays(averageDuration).toString());
        }
    }

    private void writeToFileOutput(String text) {
        File directory = context.getFilesDir();
        File file = new File(directory, filename);
        try {
            if (!file.exists()) {
                file.createNewFile();
            }
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, false))) {
                writer.write(text);
                writer.newLine();
            } catch (IOException e) {
                Log.e("DataStorage", "Error writing to file: " + e.getMessage());
            }
        } catch (IOException e) {
            Log.e("DataStorage", "Error creating file: " + e.getMessage());
        }
    }

    private String readFileContents() {
        StringBuilder contentBuilder = new StringBuilder();
        try {
            File file = new File(context.getFilesDir(), filename);
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    contentBuilder.append(line);
                }
            }
        } catch (IOException e) {
            Log.e("DataStorage", "Error reading file: " + e.getMessage());
        }
        return contentBuilder.toString();
    }

    public ArrayList<String> getItemsWithMatchingRecommendedDate() {
        ArrayList<String> matchingItems = new ArrayList<>();
        try {
            JSONArray items = new JSONArray(readFileContents());
            LocalDate today = LocalDate.now();
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");

            for (int i = 0; i < items.length(); i++) {
                JSONObject item = items.getJSONObject(i);
                String recommendedDateStr = item.getString("date_to_recommend");
                LocalDate recommendedDate = LocalDate.parse(recommendedDateStr, dtf);

                if (recommendedDate.equals(today)) {
                    matchingItems.add(item.getString("item"));
                }
            }
        } catch (Exception e) {
            Log.e("DataStorage", "Error getting items with matching recommended date: " + e.getMessage());
        }
        return matchingItems;
    }
}