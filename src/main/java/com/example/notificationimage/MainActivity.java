package com.example.notificationimage;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        showNotificationWithImage();
    }

    //notify with an image
    @SuppressLint("StaticFieldLeak")
    private void showNotificationWithImage(){

        new AsyncTask<String, Void, Bitmap>() {
            @Override
            protected Bitmap doInBackground(String... strings) {
                InputStream input;
                try {
                    URL url = new URL(strings[0]);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setDoInput(true);
                    connection.connect();
                    input = connection.getInputStream();
                    return BitmapFactory.decodeStream(input);

                }catch (Exception ignored){
                }
                return null;
            }
            @Override
            protected void onPostExecute(Bitmap bitmap) {
                    showNotification(bitmap);
            }
        }.execute("https://www.infinityandroid.com/wp-content/uploads/2021/05/landing_background_image-576x1024.jpg"); //url of an image
        //once the notication chanel is created, some updated notification configuration doesn't work
        // until you go and update notification channel id or uninstall and rein the application
    }
    //Pass in bitmap
    private void showNotification(Bitmap bitmap){
        int notificationID = new Random().nextInt(100);
        String channelId =  " notification_channel1";

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        Intent i = new Intent(getApplicationContext(), SecondActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        PendingIntent pi = PendingIntent.getActivity(
                getApplicationContext(),
                0,
                i,
                PendingIntent.FLAG_UPDATE_CURRENT);
        //(getApplicationContext(), 0,i,PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(
                getApplicationContext(),channelId
        );
        builder.setSmallIcon(R.drawable.ic_notifications);
        builder.setDefaults(NotificationCompat.DEFAULT_ALL);
        builder.setContentTitle("What is Lorem Ipsum?");
        builder.setContentText("Lorem Ipsum is simply dummy text..");

        //Notification with big text
        //builder.setStyle(new NotificationCompat.BigTextStyle().bigText("" + "Lorem Ipsum is simply dummy text of the printing and typesetting industry. " + "Lorem Ipsum has been the industry's standard dummy text ever since the 1500s"));
        builder.setStyle(new NotificationCompat.BigPictureStyle().bigPicture(bitmap));

        builder.setContentIntent(pi);
        builder.setAutoCancel(true);
        builder.setPriority(NotificationCompat.PRIORITY_MAX);

        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.O){
            if(notificationManager != null && notificationManager.getNotificationChannel(channelId) == null){
                NotificationChannel notificationChannel = new NotificationChannel(
                        channelId,
                        "notification Channel 1",
                        NotificationManager.IMPORTANCE_HIGH
                );

                notificationChannel.setDescription("this notification channel is used to notify the user ");
                notificationChannel.enableVibration(true);
                notificationChannel.enableLights(true);
                notificationManager.createNotificationChannel(notificationChannel);
            }
        }

        Notification not = builder.build();
        if(notificationManager != null){
            notificationManager.notify(notificationID, not);
        }

    }
}