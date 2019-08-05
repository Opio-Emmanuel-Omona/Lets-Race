package com.example.letsrace;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.Locale;

public class SignUp extends AppCompatActivity {

    private static final String PROF_PREF = "PROFILE";
    private static final String USERNAME = "USERNAME";
    private static final String PHONE = "PHONE";
    private static final String HEIGHT = "HEIGHT";
    private static final String WEIGHT = "WEIGHT";
    ImageView avatar;
    EditText usernameTxt;
    EditText phoneNumber;
    EditText heightTxt;
    EditText weightTxt;
    Button submit;

    SharedPreferences pref;
    SharedPreferences.Editor profilePref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        avatar = findViewById(R.id.avatar);
        usernameTxt = findViewById(R.id.username);
        phoneNumber = findViewById(R.id.phone_number);
        heightTxt = findViewById(R.id.height);
        weightTxt = findViewById(R.id.weight);
        submit = findViewById(R.id.submit);

        pref = getSharedPreferences(PROF_PREF, MODE_PRIVATE);

        usernameTxt.setImeOptions(EditorInfo.IME_ACTION_DONE);
        phoneNumber.setImeOptions(EditorInfo.IME_ACTION_DONE);
        heightTxt.setImeOptions(EditorInfo.IME_ACTION_DONE);
        weightTxt.setImeOptions(EditorInfo.IME_ACTION_DONE);

        populateFields();

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveToPref();
                if (!verifyPreferences()) {
                    Toast.makeText(SignUp.this, "All fields required", Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(SignUp.this, MainActivity.class);
                    startActivity(intent);
                }
            }
        });
    }

    private void populateFields() {
        if (verifyPreferences()) {
            usernameTxt.setText(pref.getString(USERNAME, ""));
            phoneNumber.setText(pref.getString(PHONE, ""));
            heightTxt.setText(String.format(Locale.ENGLISH, "%s %s"
                    , pref.getString(HEIGHT, ""), "Kg"));
            weightTxt.setText(String.format(Locale.ENGLISH, "%s %s"
                    ,pref.getString(WEIGHT, ""), "cm"));
        }
    }

    private boolean verifyPreferences() {
        return (pref.contains(USERNAME) && pref.contains(PHONE) && pref.contains(HEIGHT) && pref.contains(WEIGHT));
    }

    private void saveToPref() {

        String user = this.usernameTxt.getText().toString();
        String phone = this.phoneNumber.getText().toString();
        String height = this.heightTxt.getText().toString();
        String weight = this.weightTxt.getText().toString();

        profilePref = getSharedPreferences(PROF_PREF, MODE_PRIVATE).edit();
        profilePref.clear();
        profilePref.putString("avatar", "");
        if (!user.equals("")) profilePref.putString("usernameTxt", user);
        if (!phone.equals("")) profilePref.putString("phone", phone);
        if (!height.equals("")) profilePref.putString("heightTxt", height);
        if (!weight.equals("")) profilePref.putString("weightTxt", weight);
        profilePref.apply();
    }


}
