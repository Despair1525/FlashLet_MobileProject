package com.example.prm_final_project.Services;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.prm_final_project.R;
import com.example.prm_final_project.Ui.Activity.NotificationDetailActivity;

import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class ReminderBroadcast extends BroadcastReceiver {

    private final static String NOTIFICATION_CHANNEL= "NC011";
    private final static String MY_PREFERENCE = "preference_notification";
    private static final String NOTIFICATION_MESSAGE = "message";
    final int flag =  Build.VERSION.SDK_INT >= Build.VERSION_CODES.M ? PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE : PendingIntent.FLAG_UPDATE_CURRENT;


    @Override
    public void onReceive(Context context, Intent intent) {
//        String randomMessage = intent.getStringExtra("random_message");

        Bitmap largeIcon = BitmapFactory.decodeResource(context.getResources(),
                R.drawable.icon);

        SharedPreferences sharedPreferences = context.getSharedPreferences(MY_PREFERENCE, Context.MODE_PRIVATE);
        String message = sharedPreferences.getString(NOTIFICATION_MESSAGE, null);

        Intent intent1 = new Intent(context, NotificationDetailActivity.class);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, NOTIFICATION_CHANNEL);
        builder.setContentTitle("It's time to study!");

        if(message == null || message.trim().isEmpty()){
            String randomMessage = getDataFromFile(context);
            builder.setContentText(randomMessage);
            intent1.removeExtra("message_quote");
            intent1.putExtra("message_quote", randomMessage);
        } else {
            builder.setContentText(message);
            intent1.removeExtra("message_quote");
            intent1.putExtra("message_quote", message);
        }

        builder.setSmallIcon(R.mipmap.ic_stat_ic_app);
        builder.setColor(context.getColor(R.color.theme_color));
        builder.setPriority(NotificationManager.IMPORTANCE_HIGH);
        builder.setStyle(new NotificationCompat.BigTextStyle().bigText(message));
        builder.setLargeIcon(largeIcon);
        builder.setOngoing(true);
        PendingIntent pendingIntent = PendingIntent.getActivity(
                context, 2, intent1, flag);
        builder.setContentIntent(pendingIntent).setAutoCancel(true);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        notificationManager.notify(200, builder.build());

    }

    private String getDataFromFile(Context context){
        StringBuffer buffer = new StringBuffer();
        Scanner scanner = new Scanner(context.getResources().openRawResource(R.raw.sample_messages));

        ArrayList<String> array = new ArrayList<>();
        while(scanner.hasNext()){
            String line = scanner.nextLine();
            array.add(line);
        }
        scanner.close();

        Random rand = new Random();

        int randomIndex = rand.nextInt(array.size());
        return array.get(randomIndex);
    }

}
