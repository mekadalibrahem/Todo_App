package com.example.todo_app.todo;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.example.todo_app.MainActivity;
import com.example.todo_app.R;
import com.example.todo_app.database.QueryHelper;

public class AlarmWorker extends Worker {

    public AlarmWorker(@NonNull Context context ,
                       @NonNull WorkerParameters workerParameters){
        super(context , workerParameters);
    }


    @Override
    public  Result doWork(){
        try{
            // Get the scheduled notification time from WorkerParameters
            long triggerAtMillis = getInputData().getLong("trigger_at_millis", 0);
            long reminder_id = getInputData().getLong("reminder_id" , 0);
            if (triggerAtMillis == 0 || reminder_id == 0) {
                // Handle invalid trigger time (log error or return failure)
                return Result.failure();
            }
            this.initNotification(reminder_id ,triggerAtMillis);
            return  Result.success();
        }catch (Exception e){
            return  Result.failure();
        }
    }


    private void initNotification(long remainder_id , long  triggerAtMillis){
        Context context = getApplicationContext();
        QueryHelper qh = new QueryHelper(context);
        Remainder remainder = qh.getRemainderById(remainder_id);
        String channel_id = String.valueOf(remainder.getId());
        Todo todo = qh.getTodoById(remainder.getTodo_id());
        // content text
        String contentText =  todo.getTitle() + " in " + todo.getDate() + " - " + todo.getTime() ;
        // Create PendingIntent to launch your activity when the notification is tapped
        Intent intent = new Intent(context, MainActivity.class); // Replace with your activity class
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_IMMUTABLE);

        // Build the notification with the pending intent
        NotificationCompat.Builder nbuilder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.mipmap.ic_app_icon)
                .setContentTitle(context.getResources().getString(R.string.app_name))
                .setContentText(contentText)
                .setContentIntent(pendingIntent)
                .setChannelId(channel_id);

        // Create NotificationManager
        NotificationManager notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);

        int importance = NotificationManager.IMPORTANCE_HIGH;
        NotificationChannel notificationChannel = new NotificationChannel(
                channel_id, "NOTIFICATION_CHANNEL_NAME", importance);
        notificationChannel.enableLights(true);
        notificationChannel.setLightColor(Color.GREEN);
        notificationChannel.enableVibration(true);
        notificationManager.createNotificationChannel(notificationChannel);

        // Schedule the notification delivery at the trigger time
        notificationManager.notify((int) triggerAtMillis, nbuilder.build());

    }
}
