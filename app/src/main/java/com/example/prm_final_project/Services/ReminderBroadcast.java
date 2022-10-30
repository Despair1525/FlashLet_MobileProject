package com.example.prm_final_project.Services;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.prm_final_project.R;
import com.example.prm_final_project.Ui.Activity.MainActivity;
import com.example.prm_final_project.Ui.Activity.NotificationDetailActivity;

public class ReminderBroadcast extends BroadcastReceiver {

    private final static String NOTIFICATION_CHANNEL= "NC011";
    private final static String MY_PREFERENCE = "preference_notification";
    private static final String NOTIFICATION_MESSAGE = "message";

    @Override
    public void onReceive(Context context, Intent intent) {
        String randomMessage = intent.getStringExtra("random_message");

        SharedPreferences sharedPreferences = context.getSharedPreferences(MY_PREFERENCE, Context.MODE_PRIVATE);
        String message = sharedPreferences.getString(NOTIFICATION_MESSAGE, null);

        Intent intent1 = new Intent(context, NotificationDetailActivity.class);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, NOTIFICATION_CHANNEL);
        builder.setContentTitle("It's time to study!");

        if(message == null || message.trim().isEmpty()){
            builder.setContentText(randomMessage);
            intent1.removeExtra("message_quote");
            intent1.putExtra("message_quote", randomMessage);
        } else {
            builder.setContentText(message);
            intent1.removeExtra("message_quote");
            intent1.putExtra("message_quote", message);
        }

        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setPriority(NotificationManager.IMPORTANCE_HIGH);
        builder.setStyle(new NotificationCompat.BigTextStyle().bigText(message));
        builder.setOngoing(true);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 2, intent1, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(pendingIntent).setAutoCancel(true);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        notificationManager.notify(200, builder.build());

    }
}