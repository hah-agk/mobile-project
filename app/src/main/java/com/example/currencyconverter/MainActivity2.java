package com.example.currencyconverter;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.textfield.TextInputEditText;

public class MainActivity2 extends AppCompatActivity {

    TextInputEditText txtUsername, txtEmail, txtCountry;
    ImageView profileImage;
    MaterialToolbar toolbar;

    private static final int PICK_IMAGE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main2);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        txtUsername = findViewById(R.id.txtUsername);
        txtEmail = findViewById(R.id.txtEmail);
        txtCountry = findViewById(R.id.txtCountry);
        profileImage = findViewById(R.id.profileImage);
        toolbar = findViewById(R.id.profileToolbar);

        toolbar.setNavigationOnClickListener(v -> finish());

        String user = getIntent().getStringExtra("username");
        String email = getIntent().getStringExtra("email");
        String country = getIntent().getStringExtra("country");

        SharedPreferences prefs = getSharedPreferences("userData", MODE_PRIVATE);

        txtUsername.setText(user != null ? user : prefs.getString("username", ""));
        txtEmail.setText(email != null ? email : prefs.getString("email", ""));
        txtCountry.setText(country != null ? country : prefs.getString("country", ""));

        // Clicking Edit Profile → open gallery
        findViewById(R.id.btnEdit).setOnClickListener(v -> openGallery());
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK && data != null) {
            Uri imageUri = data.getData();
            profileImage.setImageURI(imageUri);
        }
    }
    }
