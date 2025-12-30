package com.example.currencyconverter;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class ChatFragment extends Fragment {

    private RecyclerView chatList;
    private EditText chatBox;
    private ImageButton sendBtn, clearChatBtn;

    private DatabaseReference chatRef;
    private FirebaseUser currentUser;

    private final List<Message> messages = new ArrayList<>();
    private ChatAdapter adapter;
    private String senderName = "Anonymous";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_chat, container, false);

        chatList = view.findViewById(R.id.chat_list);
        chatBox = view.findViewById(R.id.chat_box);
        sendBtn = view.findViewById(R.id.send_btn);
        clearChatBtn = view.findViewById(R.id.clearChatBtn);

        adapter = new ChatAdapter(requireContext(), messages);
        chatList.setLayoutManager(new LinearLayoutManager(requireContext()));
        chatList.setAdapter(adapter);

        currentUser = FirebaseAuth.getInstance().getCurrentUser();

        // SINGLE, CONSISTENT DATABASE PATH
        chatRef = FirebaseDatabase.getInstance(
                "https://my-currency-converter-project-default-rtdb.europe-west1.firebasedatabase.app"
        ).getReference("chatroom/messages");


        loadSenderName();
        listenForMessages();

        sendBtn.setOnClickListener(v -> sendMessage());
        clearChatBtn.setOnClickListener(v -> deleteAllMessages());

        return view;
    }

    // Load username
    private void loadSenderName() {
        if (currentUser == null) return;

        FirebaseFirestore.getInstance()
                .collection("users")
                .document(currentUser.getUid())
                .get()
                .addOnSuccessListener(snapshot -> {
                    if (snapshot.exists()) {
                        String name = snapshot.getString("Name");
                        if (name != null && !name.isEmpty()) {
                            senderName = name;
                        }
                    }
                });
    }

    // SAVE MESSAGE (PERSISTENT)
    private void sendMessage() {
        String text = chatBox.getText().toString().trim();
        if (text.isEmpty()) return;

        Message msg = new Message(senderName, text, System.currentTimeMillis());
        chatRef.push().setValue(msg);

        chatBox.setText("");
    }

    // LOAD MESSAGES (PERSISTENT)
    private void listenForMessages() {
        messages.clear();
        adapter.notifyDataSetChanged();

        chatRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Message msg = snapshot.getValue(Message.class);
                if (msg == null) return;

                messages.add(msg);
                adapter.notifyItemInserted(messages.size() - 1);
                chatList.scrollToPosition(messages.size() - 1);

                if (!msg.getSender().equals(senderName)) {
                    showNotification(msg.getSender(), msg.getMessage());
                }
            }

            @Override public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {}
            @Override public void onChildRemoved(@NonNull DataSnapshot snapshot) {}
            @Override public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {}
            @Override public void onCancelled(@NonNull DatabaseError error) {}
        });
    }

    // Notification
    private void showNotification(String title, String text) {
        String channelId = "chat_channel";

        NotificationManager manager =
                (NotificationManager) requireContext().getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    channelId, "Chat Messages",
                    NotificationManager.IMPORTANCE_HIGH);
            manager.createNotificationChannel(channel);
        }

        Notification notification = new NotificationCompat.Builder(requireContext(), channelId)
                .setSmallIcon(android.R.drawable.ic_dialog_email)
                .setContentTitle(title)
                .setContentText(text)
                .setAutoCancel(true)
                .build();

        if (Build.VERSION.SDK_INT >= 33 &&
                ActivityCompat.checkSelfPermission(requireContext(),
                        Manifest.permission.POST_NOTIFICATIONS)
                        != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        manager.notify((int) System.currentTimeMillis(), notification);
    }

    // Delete all messages
    private void deleteAllMessages() {
        chatRef.removeValue()
                .addOnSuccessListener(unused -> {
                    messages.clear();
                    adapter.notifyDataSetChanged();
                    Toast.makeText(requireContext(),
                            "All messages deleted", Toast.LENGTH_SHORT).show();
                });
    }
}
