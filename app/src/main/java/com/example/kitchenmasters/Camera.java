package com.example.kitchenmasters;


import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.util.SparseArray;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.IOException;
import java.io.InputStream;

public class Camera extends AppCompatActivity {

    // Constant to store the camera permission request code
    private static final int CAMERA_PERMISSION_REQUEST_CODE = 100;

    DataStorage storage;
    // Declare ActivityResultLauncher variables for launching camera and gallery intents
    ActivityResultLauncher<Intent> activityResultLauncher1;
    ActivityResultLauncher<Intent> activityResultLauncher2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera); // Set the layout for the activity
        //
        storage = new DataStorage(this);


        EdgeToEdge.enable(this);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return WindowInsetsCompat.CONSUMED;
        });

        // Request camera permission if it is not already granted
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_REQUEST_CODE);
        }

        Button buttonCamera = findViewById(R.id.button2);
        Button buttonGallery = findViewById(R.id.button3);

        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        ActivityResultLauncher<Intent> activityResultLauncher1 = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                        Bundle extras = result.getData().getExtras();
                        if (extras != null) {
                            Bitmap imageBitmap;
                            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
                                imageBitmap = result.getData().getParcelableExtra("data", Bitmap.class);
                            } else {
                                imageBitmap = androidx.core.os.BundleCompat.getParcelable(extras, "data", Bitmap.class);
                            }
                            if (imageBitmap != null) {
                                extractTextFromImage(imageBitmap); // Process the image
                            }
                        }
                    }
                }
        );

        ActivityResultLauncher<Intent> activityResultLauncher2 = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                        Uri selectedImageUri = result.getData().getData();
                        try {
                            Bitmap imageBitmap = null;
                            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
                                // For Android 10 (API level 29) and above
                                ImageDecoder.Source source = ImageDecoder.createSource(this.getContentResolver(), selectedImageUri);
                                imageBitmap = ImageDecoder.decodeBitmap(source);
                            } else {
                                // For SDK versions lower than Android 10
                                InputStream imageStream = getContentResolver().openInputStream(selectedImageUri);
                                imageBitmap = BitmapFactory.decodeStream(imageStream);
                            }
                            if (imageBitmap != null) {
                                extractTextFromImage(imageBitmap); // Process the image
                            }
                        } catch (IOException e) {
                            Log.e("ImageProcessing", "Error processing gallery image", e);
                        }
                    }
                }
        );

        buttonCamera.setOnClickListener(v -> activityResultLauncher1.launch(cameraIntent));
        buttonGallery.setOnClickListener(v -> activityResultLauncher2.launch(galleryIntent));

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.home) {
                startActivity(new Intent(Camera.this, MainActivity.class));
                return true;
            } else if (item.getItemId() == R.id.checklist) {
                startActivity(new Intent(Camera.this, List.class));
                return true;
            } else if (item.getItemId() == R.id.calculator) {
                startActivity(new Intent(Camera.this, CalculatorActivity.class));
                return true;}
         else if (item.getItemId() == R.id.search) {
            startActivity(new Intent(Camera.this, Search.class));
            return true;}
            return false;
        });


    }

    // Method to extract text from an image using TextRecognizer
    private void extractTextFromImage(Bitmap bitmap) {
        storage = new DataStorage(this);

        TextRecognizer textRecognizer = new TextRecognizer.Builder(getApplicationContext()).build();
        if (!textRecognizer.isOperational()) {
            Log.e("TextRecognition", "TextRecognizer is not operational.");
            Toast.makeText(this, "Text recognizer is not available", Toast.LENGTH_SHORT).show();
            return;
        }
        Frame frame = new Frame.Builder().setBitmap(bitmap).build();
        SparseArray<TextBlock> textBlocks = textRecognizer.detect(frame);

        // go through each TextBlock
        for (int index = 0; index < textBlocks.size(); index++) {
            TextBlock textBlock = textBlocks.valueAt(index);
            String text = textBlock.getValue();
            storage.manageItem(text);
        }
    }

}

