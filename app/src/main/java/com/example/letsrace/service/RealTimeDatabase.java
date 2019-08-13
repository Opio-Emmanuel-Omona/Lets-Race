package com.example.letsrace.service;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.letsrace.model.Person;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import com.example.letsrace.MainActivity;

import static android.content.Context.MODE_PRIVATE;

public class RealTimeDatabase {

    private static final String TAG = "Realtime Database";
    public static final String DATA_PREF = "DATA";
    FirebaseDatabase database;
    DatabaseReference myRef;
    Query query;
    String key;

    public static String addedUser;

    public RealTimeDatabase() {
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("person");

        // Read from the database
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.

                Log.d(TAG, "Data Snapshot: " + dataSnapshot);

                // setTextView.setText(dataSnapshot.getKey() + dataSnapshot.getChildrenCount());
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
    }

    public void updatePosition(Person person, Context context) {
        // get and or store key in shared preferences
        SharedPreferences prefs = context.getSharedPreferences(DATA_PREF, MODE_PRIVATE);
        key = prefs.getString("key", "");

        if (key.isEmpty()) {
            key = myRef.push().getKey();
            SharedPreferences.Editor editor = context.getSharedPreferences(DATA_PREF, MODE_PRIVATE).edit();
            editor.putString("key", key);
            editor.apply();
        }

        // Write a message to the database
        myRef.child(key).setValue(person);
    }


}
