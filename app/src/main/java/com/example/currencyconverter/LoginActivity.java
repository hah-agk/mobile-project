package com.example.currencyconverter;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class LoginActivity extends AppCompatActivity {

    EditText emailEt, passwordEt;
    ImageButton loginBtn;
    Button registerBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        emailEt = findViewById(R.id.editTextTextemail);
        passwordEt = findViewById(R.id.editTextTextpassword);
        loginBtn = findViewById(R.id.loginbutton);
        registerBtn = findViewById(R.id.toregisteractivity);

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailEt.getText().toString().trim();
                String password = passwordEt.getText().toString().trim();

                // Check empty fields
                if (email.isEmpty() || password.isEmpty()) {
                    Toast.makeText(LoginActivity.this,
                            "Please fill all the fields",
                            Toast.LENGTH_SHORT).show();
                    return;
                }

                // Hard-coded login (later replaced by SQLite)
                String correctEmail = "test@gmail.com";
                String correctPassword = "123456";

                if (email.equals(correctEmail) && password.equals(correctPassword)) {
                    // Go to Main Activity
                    Intent intent = new Intent(LoginActivity.this, MainActivity2.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(LoginActivity.this,
                            "Wrong email or password",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
                startActivity(intent);
            }
        });

    }
}