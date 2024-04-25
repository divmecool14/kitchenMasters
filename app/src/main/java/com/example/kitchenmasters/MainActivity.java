package com.example.kitchenmasters;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kitchenmasters.Adapters.RandomRecipeAdapter;
import com.example.kitchenmasters.Listeners.RandomRecipeResponseListener;
import com.example.kitchenmasters.Models.RandomRecipeApiResponse;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {



    RequestManager manager; // Declaration of RequestManager object
    RandomRecipeAdapter randomRecipeAdapter; // Declaration of RandomRecipeAdapter object
    RecyclerView recyclerView; // Declaration of RecyclerView object

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); // Set the layout for the activity from activity_main.xml

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        bottomNavigationView.setOnItemSelectedListener(new BottomNavigationView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.camera) {
                    startActivity(new Intent(MainActivity.this, Camera.class));
                    return true;
                } else if (item.getItemId() == R.id.checklist) {
                    startActivity(new Intent(MainActivity.this, List.class));
                    return true;
                }
                else if (item.getItemId() == R.id.calculator) {
                    startActivity(new Intent(MainActivity.this, CalculatorActivity.class));
                    return true;
                }
                 else if (item.getItemId() == R.id.search) {
                startActivity(new Intent(MainActivity.this, Search.class));
                return true;
            }
                // Handle other menu items if needed
                return false;
            }
        });

        manager = new RequestManager(this); // Initialize RequestManager with current activity context

        manager.getRandomRecipes(randomRecipeResponseListener); // Call method to fetch random recipes from API and pass listener



    }

    // Listener for handling API response

    private final RandomRecipeResponseListener randomRecipeResponseListener= new RandomRecipeResponseListener() {
        @Override
        public void didFetch(RandomRecipeApiResponse response, String message) {

            recyclerView = findViewById(R.id.recycler_random); // Find the RecyclerView in the layout
            recyclerView.setHasFixedSize(true); // Set fixed size for RecyclerView items
            recyclerView.setLayoutManager(new GridLayoutManager(MainActivity.this, 1)); // Set layout manager for RecyclerView
            randomRecipeAdapter = new RandomRecipeAdapter(MainActivity.this, response.recipes); // Create adapter with fetched recipes
            recyclerView.setAdapter(randomRecipeAdapter); // Set adapter for RecyclerView
        }

        @Override
        public void didError(String message) {
            Toast.makeText(MainActivity.this,message, Toast.LENGTH_SHORT).show(); // Show toast message for error
        }
    };
}
