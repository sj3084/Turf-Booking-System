package com.example.madproject;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ConfirmationActivity extends AppCompatActivity {
    private TextView confirmationDetailsTextView;
    private DatabaseReference bookingRef;
    private String venue, date, slot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        setContentView(R.layout.confirmation);

        confirmationDetailsTextView = findViewById(R.id.confirmationDetailsTextView);

        venue = getIntent().getStringExtra("venue");
        date = getIntent().getStringExtra("date");
        slot = getIntent().getStringExtra("slot");

        bookingRef = FirebaseDatabase.getInstance().getReference("Bookings")
                .child(venue).child(date).child(slot).child("Users");

        loadConfirmationDetails();
    }

    private void loadConfirmationDetails() {
        bookingRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                StringBuilder details = new StringBuilder();
                details.append("Booking Details\nVenue: ").append(venue)
                        .append("\nDate: ").append(date)
                        .append("\nSlot: ").append(slot)
                        .append("\n\nUser List:\n");

                for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                    String name = userSnapshot.child("name").getValue(String.class);
                    String phone = userSnapshot.child("phone").getValue(String.class);
                    details.append("\nName: ").append(name)
                            .append("\nPhone: ").append(phone).append("\n");
                }

                confirmationDetailsTextView.setText(details.toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                confirmationDetailsTextView.setText("Failed to load confirmation details.");
            }
        });
    }
}
