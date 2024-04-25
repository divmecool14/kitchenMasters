package com.example.kitchenmasters;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class CalculatorActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_calculator);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize BottomNavigationView
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.home) {
                startActivity(new Intent(CalculatorActivity.this, MainActivity.class));
                return true;
            } else if (item.getItemId() == R.id.checklist) {
                startActivity(new Intent(CalculatorActivity.this, List.class));
                return true;
            } else if (item.getItemId() == R.id.camera) {
                startActivity(new Intent(CalculatorActivity.this, Camera.class));
                return true;
            } else if (item.getItemId() == R.id.search) {
                startActivity(new Intent(CalculatorActivity.this, Search.class));
                return true;
            }
            return false;
        });


        final EditText edtFat = findViewById(R.id.edtFat);
        final EditText edtProtein = findViewById(R.id.edtProtein);
        final EditText edtCarbs = findViewById(R.id.edtCarbs);
        Button btnCalculate = findViewById(R.id.btnCalculate);
        TextView txtCalories = findViewById(R.id.txtCalories);

        btnCalculate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                try {
                    int fat = Integer.parseInt(edtFat.getText().toString());
                    int protein = Integer.parseInt(edtProtein.getText().toString());
                    int carbs = Integer.parseInt(edtCarbs.getText().toString());

                    if (fat > 300 || protein > 300 || carbs > 300) {
                        Toast.makeText(CalculatorActivity.this, "Please ensure all values are 300 or less.", Toast.LENGTH_LONG).show();
                    } else {
                        int calories = (fat * 9) + (protein * 4) + (carbs * 4);
                        txtCalories.setText("Total Calories: " + calories);
                    }
                } catch (NumberFormatException e) {
                    Toast.makeText(CalculatorActivity.this, "Please enter valid numbers.", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
