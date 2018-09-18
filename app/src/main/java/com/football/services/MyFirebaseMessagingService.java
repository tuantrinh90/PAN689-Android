package com.football.services;


import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;
import android.util.Log;

import com.football.fantasy.R;
import com.football.fantasy.activities.MainActivity;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";

    // [START receive_message]
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        Log.d(TAG, "From: " + remoteMessage.getFrom());

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());
            Map<String, String> payload = remoteMessage.getData();

            // Check if message contains a notification payload.
            if (remoteMessage.getNotification() != null) {
                Log.d(TAG, "onMessageReceived#title: " + remoteMessage.getNotification().getTitle());
                Log.d(TAG, "onMessageReceived#Body: " + remoteMessage.getNotification().getBody());
                sendNotification(
                        payload,
                        remoteMessage.getNotification().getTitle(),
                        remoteMessage.getNotification().getBody());
            }

        }

        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.
    }
    // [END receive_message]

    private void sendNotification(Map<String, String> payload, String title, String messageBody) {
        Intent intent = getIntentByAction(payload);

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        String channelId = "0";
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this, channelId)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle(title)
                        .setContentText(messageBody)
                        .setAutoCancel(true)
                        .setSound(defaultSoundUri)
                        .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId,
                    "Channel human readable title",
                    NotificationManager.IMPORTANCE_DEFAULT);
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(channel);
            }
        }

        if (notificationManager != null) {
            notificationManager.notify(-1 /* ID of notification */, notificationBuilder.build());
        }
    }

    private Intent getIntentByAction(Map<String, String> payload) {
        String id = TextUtils.isEmpty(payload.get("id")) ? "-1" : payload.get("id");
        String action = payload.get("action");
        String teamName = payload.get("team_name");
        int leagueId = getInt(payload, "league_id");
        int teamId = getInt(payload, "team_id");
        int playerId = getInt(payload, "player_id");

        Intent intent;

        intent = new Intent(this, MainActivity.class);
        intent.putExtra(MainActivity.KEY_ACTION, action);
        intent.putExtra(MainActivity.KEY_TEAM_NAME, teamName);
        intent.putExtra(MainActivity.KEY_LEAGUE_ID, leagueId);
        intent.putExtra(MainActivity.KEY_TEAM_ID, teamId);
        intent.putExtra(MainActivity.KEY_PLAYER_ID, playerId);
        return intent;
    }

    private int getInt(Map<String, String> payload, String key) {
        if (payload.containsKey(key)) {
            String value = payload.get(key);
            if (TextUtils.isDigitsOnly(value)) {
                return Integer.valueOf(value);
            }
        }
        return -1;
    }
}