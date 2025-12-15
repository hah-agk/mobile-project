package com.example.currencyconverter;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.currencyconverter.databinding.ActivityMain2Binding;
import com.google.android.material.navigation.NavigationBarView;

public class MainActivity2 extends AppCompatActivity {

    ActivityMain2Binding binding;
//    FirebaseAuth fAuth;
//    FirebaseFirestore fStore;
//    String userID;


        @Override
        protected void onCreate(Bundle savedInstanceState) {

            super.onCreate(savedInstanceState);
            binding = ActivityMain2Binding.inflate(getLayoutInflater());
            setContentView(binding.getRoot());
            replaceFragment(new ProfileFragment());

//            fAuth = FirebaseAuth.getInstance();
//            fStore = FirebaseFirestore.getInstance();

//            email = findViewById(R.id.email);
//            name = findViewById(R.id.Name);
//
//            userID = fAuth.getCurrentUser().getUid();
//
//            DocumentReference documentReference = fStore.collection("users").document(userID);
//            documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
//                @Override
//                public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException error) {
//                        email.setText(documentSnapshot.getString("email"));
//                        name.setText(documentSnapshot.getString("Name"));
//                }
//            });

            // // // // // // // // // // / // // / // / /// // / // /// /// /// // / // / / //// // / // / / // / / // / / /
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
