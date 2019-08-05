package com.example.letsrace;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class SignUp extends AppCompatActivity {

    private static final String PROF_PREF = "PROFILE";
    private static final String AVATAR = "AVATAR";
    private static final String USERNAME = "USERNAME";
    private static final String PHONE = "PHONE";
    private static final String HEIGHT = "HEIGHT";
    private static final String WEIGHT = "WEIGHT";

    CircleImageView avatarImg;
    EditText usernameTxt;
    EditText phoneNumber;
    EditText heightTxt;
    EditText weightTxt;
    Button submit;

    Uri selectedImageUri;
    Bitmap selectedImage;
    OutputStream outputStream;

    SharedPreferences pref;
    SharedPreferences.Editor profilePref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        avatarImg = findViewById(R.id.avatar);
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
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                }
            }
        });

        avatarImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(Intent.createChooser(intent,"Select file to upload "), 10);
            }
        });
    }

    private void populateFields() {
        if (verifyPreferences()) {
            loadAvatar();
            usernameTxt.setText(pref.getString(USERNAME, ""));
            phoneNumber.setText(pref.getString(PHONE, ""));
            heightTxt.setText(String.format(Locale.ENGLISH, "%s %s"
                    , pref.getString(HEIGHT, ""), "Kg"));
            weightTxt.setText(String.format(Locale.ENGLISH, "%s %s"
                    ,pref.getString(WEIGHT, ""), "cm"));
        }
    }

    private void loadAvatar() {
        File filepath = Environment.getExternalStorageDirectory();
        Uri file = Uri.fromFile(new File(filepath.getAbsolutePath(), "/LetsRace/avatar.jpg"));

        try {
            Bitmap image = MediaStore.Images.Media.getBitmap(getContentResolver(), file);
            avatarImg.setImageBitmap(image);
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }


    }

    private boolean verifyPreferences() {
        return (pref.contains(USERNAME) && pref.contains(PHONE) && pref.contains(HEIGHT) && pref.contains(WEIGHT));
    }

    private void saveToPref() {

        BitmapDrawable avatar = (BitmapDrawable) avatarImg.getDrawable();
        String user = this.usernameTxt.getText().toString();
        String phone = this.phoneNumber.getText().toString();
        String height = this.heightTxt.getText().toString();
        String weight = this.weightTxt.getText().toString();

        profilePref = getSharedPreferences(PROF_PREF, MODE_PRIVATE).edit();
        profilePref.clear();

        if (avatar != null) saveImage(avatar);
        if (!user.equals("")) profilePref.putString(USERNAME, user);
        if (!phone.equals("")) profilePref.putString(PHONE, phone);
        if (!height.equals("")) profilePref.putString(HEIGHT, height);
        if (!weight.equals("")) profilePref.putString(WEIGHT, weight);
        profilePref.apply();
    }

    private void saveImage(BitmapDrawable avatar) {
        Bitmap bitmap = avatar.getBitmap();

        File filepath = Environment.getExternalStorageDirectory();
        File dir = new File(filepath.getAbsolutePath() + "/LetsRace/");

        boolean isDirectoryCreated = dir.exists();
        if (!isDirectoryCreated) {
            isDirectoryCreated = dir.mkdir();
        }
        if (!isDirectoryCreated) {
            Toast.makeText(getApplicationContext(),
                    "Go to Settings, Select the Application and turn on permissions",
                    Toast.LENGTH_LONG).show();
        }

        File file =  new File(dir, "avatar.jpg");
        try {
            outputStream = new FileOutputStream(file);
        } catch (Exception e) {
            e.printStackTrace();
        }

        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);

        try {
            outputStream.flush();
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (data.getData() != null) {
                selectedImageUri = data.getData();
            } else {
                Toast.makeText(getApplicationContext(), "failed to get Image!", Toast.LENGTH_SHORT).show();
            }

            if (requestCode == 10) {
                try {
                    selectedImage = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImageUri);
                    avatarImg.setImageBitmap(selectedImage);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }
    }


}
