package com.example.letsrace.service;

import android.util.Log;
import android.widget.Toast;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private static final String TAG = "Firebase";
    private static final String PUSH_NOTIFICATION = "pushNotification";
    private static final String UPDATED_TOKEN = "updatedToken";

    /**
     * Called if InstanceID token is updated. This may occur if the security of
     * the previous token had been compromised. Note that this is called when the InstanceID token
     * is initially generated so this is where you would retrieve the token.
     */
    @Override
    public void onNewToken(String token) {
        Log.d(TAG, "Refreshed token: " + token);

        Toast.makeText(getApplicationContext(), "onNewToken" + token, Toast.LENGTH_LONG).show();
    }


    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        if (remoteMessage.getNotification() != null) {
            Toast.makeText(getApplicationContext(),
                    "onMessageReceived " + remoteMessage.getNotification().getBody(),
                    Toast.LENGTH_LONG).show();
        }
    }
}
