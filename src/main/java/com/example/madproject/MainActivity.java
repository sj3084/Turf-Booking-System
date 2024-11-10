package com.example.madproject;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private Button dateButton, bookButton, loginButton, logoutButton;
    private EditText nameEditText, phoneEditText;
    private Spinner venueSpinner, slotSpinner;
    private String selectedDate, selectedSlot, selectedVenue;
    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("Bookings");

        // Initialize UI elements
        dateButton = findViewById(R.id.dateButton);
        bookButton = findViewById(R.id.bookButton);
        loginButton = findViewById(R.id.loginButton);
        logoutButton = findViewById(R.id.logoutButton);
        nameEditText = findViewById(R.id.nameEditText);
        phoneEditText = findViewById(R.id.phoneEditText);
        slotSpinner = findViewById(R.id.slotSpinner);
        venueSpinner = findViewById(R.id.venueSpinner);

        // Check if the user is logged in or not
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            loginButton.setVisibility(View.VISIBLE);
            logoutButton.setVisibility(View.GONE);
        } else {
            loginButton.setVisibility(View.GONE);
            logoutButton.setVisibility(View.VISIBLE);
        }

        // Set up date picker
        dateButton.setOnClickListener(view -> pickDate());

        // Set up booking button
        bookButton.setOnClickListener(view -> {
            selectedVenue = venueSpinner.getSelectedItem().toString();
            selectedSlot = slotSpinner.getSelectedItem().toString();
            String name = nameEditText.getText().toString();
            String phone = phoneEditText.getText().toString();

            if (selectedDate != null && selectedSlot != null && selectedVenue != null && !name.isEmpty() && !phone.isEmpty()) {
                checkSlotAvailability(name, phone);
            } else {
                Toast.makeText(this, "Please complete all fields", Toast.LENGTH_SHORT).show();
            }
        });

        // Set up login button
        loginButton.setOnClickListener(view -> {
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
        });

        // Set up logout button
        logoutButton.setOnClickListener(view -> {
            mAuth.signOut();
            // After logging out, redirect to LoginActivity
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
            finish();
        });
    }

    private void pickDate() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, (view, year1, month1, dayOfMonth) -> {
            selectedDate = dayOfMonth + "/" + (month1 + 1) + "/" + year1;
            dateButton.setText(selectedDate);
        }, year, month, day);
        datePickerDialog.show();
    }

    private void checkSlotAvailability(String name, String phone) {
        DatabaseReference slotRef = databaseReference.child(selectedVenue).child(selectedDate).child(selectedSlot);
        slotRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.child("status").exists() && snapshot.child("status").getValue().equals("Booked")) {
                    Toast.makeText(MainActivity.this, "This slot is already booked.", Toast.LENGTH_SHORT).show();
                } else {
                    saveBookingDetails(name, phone);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MainActivity.this, "Failed to check slot availability.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void saveBookingDetails(String name, String phone) {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user == null) {
            Toast.makeText(MainActivity.this, "User is not authenticated.", Toast.LENGTH_SHORT).show();
            return;
        }

        String userId = user.getUid();
        DatabaseReference bookingRef = databaseReference.child(selectedVenue).child(selectedDate).child(selectedSlot);
        Map<String, Object> userDetails = new HashMap<>();
        userDetails.put("name", name);
        userDetails.put("phone", phone);
        bookingRef.child("Users").child(userId).setValue(userDetails);

        Intent intent = new Intent(MainActivity.this, WaitingRoomActivity.class);
        intent.putExtra("venue", selectedVenue);
        intent.putExtra("date", selectedDate);
        intent.putExtra("slot", selectedSlot);
        startActivity(intent);
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Check if user is still authenticated
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
            finish();
        }
    }
}
