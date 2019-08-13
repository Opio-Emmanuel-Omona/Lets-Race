package com.example.letsrace;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static com.example.letsrace.SignUp.person;
import static com.example.letsrace.service.RealTimeDatabase.DATA_PREF;

public class Running extends AppCompatActivity {

    TextView timer;
    TextView distance;
    Button start;

    FirebaseDatabase database;
    DatabaseReference myRef;

    int distanceCounter = 0;
    long timerCounter = 0;
    long startTime = 0;
    int mInterval = 10000;

    Handler runningHandler;
    Runnable runningRunnable = new Runnable() {
        @Override
        public void run() {
            try {
                updatePosition();
            } finally {
                runningHandler.postDelayed(runningRunnable, mInterval);
            }
        }
    };

    Handler timerHandler;
    Runnable timerRunnable = new Runnable() {
        @Override
        public void run() {
            long millis = System.currentTimeMillis() - startTime;
            int seconds = (int) (millis / 1000);
            int minutes = seconds / 60;
            seconds = seconds % 60;
            timerCounter = seconds;
            timer.setText(String.format("%02d:%02d", minutes, seconds));
            timerHandler.postDelayed(timerRunnable, 500);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_running);

        timer = findViewById(R.id.runningTimerText);
        distance = findViewById(R.id.runningDistanceText);
        start  = findViewById(R.id.startRunningButton);

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("running");

        runningHandler = new Handler();
        timerHandler = new Handler();
        startTime = System.currentTimeMillis();

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startUpdating();
            }
        });

        // Read from the database
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.

                Log.d("Running", "Data Snapshot: " + dataSnapshot);

            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("Running", "Failed to read value.", error.toException());
            }
        });
    }

    private void startUpdating() {
        runningRunnable.run();
        timerRunnable.run();
    }

    private void stopUpdating() {
        runningHandler.removeCallbacks(runningRunnable);
        timerHandler.removeCallbacks(timerRunnable);
    }

    private void  updatePosition() {
        SharedPreferences prefs = getApplicationContext().getSharedPreferences(DATA_PREF, MODE_PRIVATE);
        String key = prefs.getString("key", "");

        person.setDistanceCovered(++distanceCounter);
        person.setTimeSpent(timerCounter);

        myRef.child(key).setValue(person);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopUpdating();
    }
}
