package com.example.currencyconverter;

import static androidx.core.content.ContextCompat.startActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    EditText emailEt, passwordEt;
    ImageButton loginBtn;
    Button registerBtn;
    FirebaseAuth fAuth;

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseAuth auth = FirebaseAuth.getInstance();

        if (auth.getCurrentUser() != null) {
            // User already logged in
            startActivity(new Intent(this, MainActivity2.class));
            finish();
        }
    }

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

        fAuth = FirebaseAuth.getInstance();
        loginBtn.setOnClickListener(v -> {
            String email = emailEt.getText().toString().trim();
            String password = passwordEt.getText().toString().trim();
            if (TextUtils.isEmpty(email)) {
                emailEt.setError("Email required");
                return;
            }
            if (TextUtils.isEmpty(password)) {
                passwordEt.setError("Password required");
                return;
            }
            if (password.length()<6) {
                passwordEt.setError("Password must be greater than 6 characters");
                return;
            }
            fAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Toast.makeText(LoginActivity.this, "Logged in Successfully", Toast.LENGTH_LONG).show();
                    startActivity(new Intent(LoginActivity.this, MainActivity2.class));
                    finish();
                } else {
                    Toast.makeText(LoginActivity.this, "ERROR " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                }
            });
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
