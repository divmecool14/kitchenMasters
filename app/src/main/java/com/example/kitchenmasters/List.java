package com.example.kitchenmasters;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.Type;
import java.util.ArrayList;
public class List extends AppCompatActivity {

    private EditText itemEditText;
    private Button addButton;
    private ListView shoppingList, recommendedList;
    private ArrayAdapter<String> adapter, recommendedAdapter;
    private ArrayList<String> itemList, recommendedItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        EdgeToEdge.enable(this);
        setupNavigation();
        setupShoppingList();
        setupRecommendedList();
    }

    private void setupRecommendedList() {
        recommendedList = findViewById(R.id.recommendedList);
        recommendedItems = ListDataStorage.getInstance().getDetectedTexts();
        recommendedAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, recommendedItems);
        recommendedList.setAdapter(recommendedAdapter);

        recommendedList.setOnItemClickListener((parent, view, position, id) -> {
            String item = recommendedItems.get(position);
            addRecommendedItem(item);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateRecommendedList();
    }

    private void updateRecommendedList() {
        ArrayList<String> newItems = ListDataStorage.getInstance().getDetectedTexts();
        for (String item : newItems) {
            addRecommendedItem(item);
        }
        ListDataStorage.getInstance().clearDetectedTexts();
    }



    private void setupNavigation() {
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.list), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);

            BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
            bottomNavigationView.setOnItemSelectedListener(new BottomNavigationView.OnItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    if (item.getItemId() == R.id.home) {
                        startActivity(new Intent(List.this, MainActivity.class));
                        return true;
                    } else if (item.getItemId() == R.id.camera) {
                        startActivity(new Intent(List.this, Camera.class));
                        return true;

                    } else if (item.getItemId() == R.id.calculator) {
                        startActivity(new Intent(List.this, CalculatorActivity.class));
                        return true;

                    } else if (item.getItemId() == R.id.search) {
                        startActivity(new Intent(List.this, Search.class));
                        return true;
                    }

                    return false;
                }
            });
            return insets;
        });
    }

    public void addRecommendedItem(String item) {
        if (!itemList.contains(item)) {
            itemList.add(item);
            adapter.notifyDataSetChanged();
            saveItems();
        }
    }

    private void setupShoppingList() {
        itemEditText = findViewById(R.id.itemEditText);
        addButton = findViewById(R.id.addButton);
        shoppingList = findViewById(R.id.shoppingList);
        itemList = loadItems();  // Load items from file
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, itemList);
        shoppingList.setAdapter(adapter);

        addButton.setOnClickListener(v -> {
            String item = itemEditText.getText().toString();
            if (!item.isEmpty()) {
                itemList.add(item);
                adapter.notifyDataSetChanged();
                itemEditText.setText("");  //empties the input field
                saveItems();  // saves items to file
            }
        });

        shoppingList.setOnItemClickListener((parent, view, position, id) -> {
            itemList.remove(position);
            adapter.notifyDataSetChanged();
            saveItems();  // saves changes to file
        });
    }

    private void saveItems() {
        Gson gson = new Gson();
        String json = gson.toJson(itemList);
        try (FileOutputStream fos = openFileOutput("shoppingList.json", MODE_PRIVATE);
             OutputStreamWriter isr = new OutputStreamWriter(fos)) {
            isr.write(json);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private ArrayList<String> loadItems() {
        Gson gson = new Gson();
        ArrayList<String> items;
        try (FileInputStream fis = openFileInput("shoppingList.json");
             InputStreamReader isr = new InputStreamReader(fis)) {
            Type type = new TypeToken<ArrayList<String>>() {}.getType();
            items = gson.fromJson(isr, type);
        } catch (Exception e) {
            items = new ArrayList<>();
        }
        return items != null ? items : new ArrayList<>();
    }
}