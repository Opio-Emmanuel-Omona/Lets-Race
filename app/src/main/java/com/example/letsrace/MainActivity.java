package com.example.letsrace;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    TextView chooseActivityText;
    Button runningButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        chooseActivityText = findViewById(R.id.chooseActivityHeader);
        runningButton = findViewById(R.id.runningActivityButton);

        runningButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Running.class);
                startActivity(intent);
            }
        });
    }
}
