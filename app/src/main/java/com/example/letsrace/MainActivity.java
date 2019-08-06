package com.example.letsrace;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.letsrace.model.Person;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "Realtime Database";
    FirebaseDatabase database;
    DatabaseReference myRef;

    TextView textView;
    TextView setTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("person");

        setTextView = findViewById(R.id.setTextView);
        textView = findViewById(R.id.mainText);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updatePosition();
            }
        });

        // Read from the database
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.

                Log.d(TAG, "Data Snapshot: " + dataSnapshot);

                setTextView.setText(dataSnapshot.getKey() + dataSnapshot.getChildrenCount());
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
    }

    private void updatePosition() {
        // Write a message to the database

        Intent intent = getIntent();
        String username = intent.getStringExtra("username");
        String weight = intent.getStringExtra("weight");
        String height = intent.getStringExtra("height");

        int distance = 100;
        Long time = 1000L;

        Person person = new Person(username, weight, height, distance, time);

        myRef.child(username).setValue(person);

        Toast.makeText(getApplicationContext(), "updated position", Toast.LENGTH_SHORT).show();
    }
}
