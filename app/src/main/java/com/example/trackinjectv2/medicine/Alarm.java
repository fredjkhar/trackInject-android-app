package com.example.trackinjectv2.medicine;



import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;

import android.os.Build;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;


import com.example.trackinjectv2.R;
import com.example.trackinjectv2.authentication.MainActivity;


public class Alarm extends BroadcastReceiver {


    private static final String TAG = "yup ";

    @Override
    public void onReceive(Context context, Intent intent) {
        final String NOTIFICATION_CHANNEL_ID = "my_channel_id";

        int id =  intent.getExtras().getInt("id");

        /* SetTimeActivity alarm = new SetTimeActivity();

        alarm.setExactAlarm(id);*/

        Intent notificationIntent = new Intent(context, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(context,id,notificationIntent, 0);


        //create a basic notification
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID);
        notificationBuilder
                .setContentTitle("TrackInject")
                .setContentText("Hi! Don't forget to take your medicine.")
                .setAutoCancel(true)
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setVibrate(new long[]{0, 500, 500})
                .setContentIntent(pendingIntent);


        //create a channel and set the importance
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            NotificationChannel channel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "Repeating notifications alarm", NotificationManager.IMPORTANCE_HIGH);
            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(channel);
        }

        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);
        notificationManagerCompat.notify(id,notificationBuilder.build());
    }
}
