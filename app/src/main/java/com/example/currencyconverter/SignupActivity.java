package com.example.currencyconverter;


import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class SignupActivity extends AppCompatActivity {

    EditText nameInput, emailInput, passwordInput;
    ImageButton signupButton;
    Button toLogin;
    FirebaseFirestore fStore;
    FirebaseAuth fAuth;
    String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_signup);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        nameInput = findViewById(R.id.editTextTextname);
        emailInput = findViewById(R.id.editTextTextemail);
        passwordInput = findViewById(R.id.editTextTextpassword);
        signupButton = findViewById(R.id.btnSignup);
        toLogin = findViewById(R.id.tologinactivity);

        fStore = FirebaseFirestore.getInstance();
        fAuth = FirebaseAuth.getInstance();

        // Go to login page
        toLogin.setOnClickListener(v -> startActivity(new Intent(SignupActivity.this, LoginActivity.class)));

        if (fAuth.getCurrentUser() != null){
            startActivity(new Intent(SignupActivity.this, MainActivity2.class));
            finish();
        }

        signupButton.setOnClickListener(this::onClick);
    }

    private void onClick(View v) {
        String name = nameInput.getText().toString().trim();
        String email = emailInput.getText().toString().trim();
        String password = passwordInput.getText().toString().trim();

        if (TextUtils.isEmpty(name)) {
            nameInput.setError("Name required");
            return;
        }
        if (TextUtils.isEmpty(email)) {
            emailInput.setError("Email required");
            return;
        }
        if (TextUtils.isEmpty(password)) {
            passwordInput.setError("Password required");
            return;
        }
        if (password.length() < 6) {
            passwordInput.setError("Password must be greater than 6 characters");
            return;
        }
        fAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(SignupActivity.this, "user is created", Toast.LENGTH_LONG).show();
                    userID = fAuth.getCurrentUser().getUid();
                    DocumentReference documentReference = fStore.collection("users").document(userID);
                    Map<String, Object> user = new HashMap<>();
                    user.put("Name", name);
                    user.put("email", email);
                    documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                        public static final String TAG = "TAG";

                        @Override
                        public void onSuccess(Void unused) {
                            Log.d(TAG, "onSuccess: user profile is created for" + userID);
                        }
                    });
                    startActivity(new Intent(SignupActivity.this, MainActivity2.class));
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(), "ERROR " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}