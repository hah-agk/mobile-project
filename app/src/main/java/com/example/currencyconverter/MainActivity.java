package com.example.currencyconverter;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    EditText usernameInput, passwordInput;
    Button loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        usernameInput = findViewById(R.id.usernameInput);
        passwordInput = findViewById(R.id.passwordInput);
        loginButton = findViewById(R.id.loginButton);

        final String correctUsername = "admin";
        final String correctPassword = "1234";

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String user = usernameInput.getText().toString().trim();
                String pass = passwordInput.getText().toString().trim();

                if (user.isEmpty() || pass.isEmpty()) {
                    Toast.makeText(MainActivity.this,
                            "Please fill all fields", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (user.equals(correctUsername) && pass.equals(correctPassword)) {
                    Toast.makeText(MainActivity.this,
                            "Login Successful!", Toast.LENGTH_SHORT).show();

                    // Navigate to home page
                    Intent intent = new Intent(MainActivity.this, MainActivity2.class);
                    startActivity(intent);
                    finish(); // prevents going back to login
                } else {
                    Toast.makeText(MainActivity.this,
                            "Wrong username or password", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


}