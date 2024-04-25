package com.example.kitchenmasters;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kitchenmasters.Adapters.SearchRecipeAdapter;
import com.example.kitchenmasters.Listeners.InstructionsListener;
import com.example.kitchenmasters.Models.RecipeInstructionResponse;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Search extends AppCompatActivity {

    RequestManager manager; // Declaration of RequestManager object
    SearchRecipeAdapter searchRecipeAdapter; // Declaration of RandomRecipeAdapter object
    RecyclerView recyclerView; // Declaration of RecyclerView object
    private SearchView searchView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_search);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            // Initialize RequestManager with current activity context
            RequestManager manager = new RequestManager(Search.this);
            return insets;
        });



        // Set a listener for the EditText to detect when the user presses "Enter" key


        // Set up BottomNavigationView
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.home) {
                startActivity(new Intent(Search.this, MainActivity.class));
                return true;
            } else if (item.getItemId() == R.id.checklist) {
                startActivity(new Intent(Search.this, List.class));
                return true;
            } else if (item.getItemId() == R.id.calculator) {
                startActivity(new Intent(Search.this, CalculatorActivity.class));
                return true;
            } else if (item.getItemId() == R.id.camera) {
                startActivity(new Intent(Search.this, Camera.class));

                return true;
            }
            return false;
        });



        searchView = findViewById(R.id.search_view1);

        // Set up a listener for search events
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // Call method to fetch random recipes from API and pass listener
                manager.getInstructions(query , searchRecipeResponseListener);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    //SETS UP RECYCLER VIEW
    private final InstructionsListener searchRecipeResponseListener= new InstructionsListener() {
        @Override
        public void didFetch(RecipeInstructionResponse response, String message) {
                //these five lines of code are from chatgpt
                recyclerView = findViewById(R.id.recycler_search);
                recyclerView.setLayoutManager(new GridLayoutManager(Search.this, 1));
                searchRecipeAdapter = new SearchRecipeAdapter(Search.this, response.recipes);
                recyclerView.setAdapter(searchRecipeAdapter);

        }

        @Override
        public void didError(String message) {
            showFailedNotification(message);
        }


        //METHOD TO SHOW WHEN API FAILS
        private void showFailedNotification(String message) {
            AlertDialog.Builder builder = new AlertDialog.Builder(Search.this);
            builder.setMessage(message)
                    .setTitle(message)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.dismiss();
                        }
                    });
            AlertDialog dialog = builder.create();
            dialog.show();
        }
    };

}