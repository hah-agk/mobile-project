package com.example.currencyconverter;

import static com.example.currencyconverter.R.id.*;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.currencyconverter.databinding.ActivityMain2Binding;
import com.example.currencyconverter.databinding.ActivityMainBinding;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.textfield.TextInputEditText;

public class MainActivity2 extends AppCompatActivity {

    ActivityMain2Binding binding;

        @Override
        protected void onCreate(Bundle savedInstanceState) {

            super.onCreate(savedInstanceState);
            binding = ActivityMain2Binding.inflate(getLayoutInflater());
            setContentView(binding.getRoot());
            replaceFragment(new ProfileFragment());

            binding.bottomNavigationBar.setOnItemSelectedListener(
                    new NavigationBarView.OnItemSelectedListener() {
                        @Override
                        public boolean onNavigationItemSelected(@NonNull android.view.MenuItem item) {

                            int id = item.getItemId();

                            if (id == R.id.profile) {
                                replaceFragment(new ProfileFragment());
                                return true;
                            } else if (id == R.id.home) {
                                replaceFragment(new HomeFragment());
                                return true;
                            } else if (id == R.id.chat) {
                                replaceFragment(new ChatFragment());
                                return true;
                            }

                            return false;
                        }
                    }
            );
        }

        private void replaceFragment(Fragment fragment){

            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.frame_layout,fragment);
            fragmentTransaction.commit();
        }
    }
