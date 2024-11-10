package com.example.madproject;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class WaitingRoomActivity extends AppCompatActivity {
    private TextView userCountTextView;
    private Button leaveRoomButton;
    private DatabaseReference bookingRef;
    private String venue, date, slot;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.waiting_room);

        userCountTextView = findViewById(R.id.userCountTextView);
        leaveRoomButton = findViewById(R.id.leaveRoomButton);

        venue = getIntent().getStringExtra("venue");
        date = getIntent().getStringExtra("date");
        slot = getIntent().getStringExtra("slot");

        mAuth = FirebaseAuth.getInstance();
        bookingRef = FirebaseDatabase.getInstance().getReference("Bookings")
                .child(venue).child(date).child(slot).child("Users");

        monitorWaitingRoom();

        leaveRoomButton.setOnClickListener(view -> leaveWaitingRoom());
    }

    private void monitorWaitingRoom() {
        bookingRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                long userCount = snapshot.getChildrenCount();
                userCountTextView.setText("Current users in waiting room: " + userCount);

                if (userCount >= 2) {
                    Toast.makeText(WaitingRoomActivity.this, "Slot booked!", Toast.LENGTH_LONG).show();
                    bookingRef.getParent().child("status").setValue("Booked");
                    moveToConfirmation();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(WaitingRoomActivity.this, "Failed to load data.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void leaveWaitingRoom() {
        String userId = mAuth.getCurrentUser().getUid();
        bookingRef.child(userId).removeValue().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(WaitingRoomActivity.this, "You have left the waiting room.", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(WaitingRoomActivity.this, "Error leaving room.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void moveToConfirmation() {
        Intent intent = new Intent(WaitingRoomActivity.this, ConfirmationActivity.class);
        intent.putExtra("venue", venue);
        intent.putExtra("date", date);
        intent.putExtra("slot", slot);
        startActivity(intent);
        finish();
    }
}
