package com.example.currencyconverter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {

    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    StorageReference storageReference;
    String userID;
    Button changeProfilepic;
    ActivityResultLauncher<Intent> galleryLauncher;
    ImageView profileImage;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        galleryLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {

                        Uri imageUri = result.getData().getData();
                        profileImage.setImageURI(imageUri);

                        saveProfileImageLocally(imageUri);
                    }
                }
        );

    }



    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        profileImage = view.findViewById(R.id.profilepic);
        changeProfilepic = view.findViewById(R.id.editprofilepic);
        Button editInfoBtn = view.findViewById(R.id.editInfoBtn);
        Button logoutButton = view.findViewById(R.id.Logout_btn);

        profileImage.setOnClickListener(v -> openGallery());
        changeProfilepic.setOnClickListener(v -> openGallery());
        editInfoBtn.setOnClickListener(v -> showEditDialog());

        logoutButton.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(requireActivity(), LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        });

        loadData(view);
        loadLocalProfileImage();

        return view;
    }

    // ===================== DATA =====================
    private void loadData(View view) {
        if (fAuth.getCurrentUser() == null) return;

        userID = fAuth.getCurrentUser().getUid();
        DocumentReference docRef = fStore.collection("users").document(userID);

        docRef.get().addOnSuccessListener(snapshot -> {
            if (!snapshot.exists()) return;

            String name = snapshot.getString("Name");
            String email = snapshot.getString("email");
            String age = snapshot.getString("age");
            String phone = snapshot.getString("phone");

            ((TextView) view.findViewById(R.id.Name)).setText(name);
            ((TextView) view.findViewById(R.id.Name2)).setText(name);
            ((TextView) view.findViewById(R.id.email)).setText(email);

            TextView ageText = view.findViewById(R.id.textView6);
            TextView phoneText = view.findViewById(R.id.textView4);

            if (age != null && !age.isEmpty()) {
                ageText.setText(age + " years");
            } else {
                ageText.setText("Not set");
            }
            if (phone != null && !phone.isEmpty()) {
                phoneText.setText(age);
            } else {
                phoneText.setText("Not set");
            }
        }).addOnFailureListener(e ->
                Log.e("Firestore", "Failed to load profile", e));
    }

    // ===================== EDIT DIALOG =====================
    private void showEditDialog() {
        View dialogView = LayoutInflater.from(getContext())
                .inflate(R.layout.dialog_edit_profile, null);

        EditText editName = dialogView.findViewById(R.id.editName);
        EditText editAge = dialogView.findViewById(R.id.editAge);
        EditText editPhone = dialogView.findViewById(R.id.editPhone);

        DocumentReference docRef = fStore.collection("users").document(userID);

        docRef.get().addOnSuccessListener(snapshot -> {
            if (!snapshot.exists()) return;
            editName.setText(snapshot.getString("Name"));
            editAge.setText(snapshot.getString("age"));
            editPhone.setText(snapshot.getString("phone"));
        });

        new androidx.appcompat.app.AlertDialog.Builder(requireContext())
                .setTitle("Edit profile")
                .setView(dialogView)
                .setPositiveButton("Save", (dialog, which) -> {

                    String name = editName.getText().toString().trim();
                    String age = editAge.getText().toString().trim();
                    String phone = editPhone.getText().toString().trim();

                    if (name.isEmpty()) {
                        Toast.makeText(getContext(),
                                "Name is required", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    Map<String, Object> updates = new HashMap<>();
                    updates.put("Name", name);
                    updates.put("age", age);
                    updates.put("phone", phone);

                    docRef.update(updates).addOnSuccessListener(unused -> {
                        Toast.makeText(getContext(),
                                "Profile updated", Toast.LENGTH_SHORT).show();
                        loadData(requireView());
                    });
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    // ===================== IMAGE LOCAL STORAGE =====================
    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        galleryLauncher.launch(intent);
    }

    private void saveProfileImageLocally(Uri imageUri) {
        try {
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(
                    requireContext().getContentResolver(), imageUri);

            File file = new File(requireContext().getFilesDir(),
                    userID + "_profile.jpg");

            FileOutputStream fos = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 85, fos);
            fos.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadLocalProfileImage() {
        if (userID == null) return;

        File file = new File(requireContext().getFilesDir(),
                userID + "_profile.jpg");

        if (file.exists()) {
            Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
            profileImage.setImageBitmap(bitmap);
        }
    }


}