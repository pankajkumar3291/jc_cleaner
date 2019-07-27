package android.com.provider.pushNotification;


import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.com.provider.activities.ActivityOpenApportunity;
import android.com.provider.applicationUtils.App;
import android.com.provider.models.OpenApportunity;
import android.com.provider15_nov_2018.R;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.orhanobut.hawk.Hawk;
import java.util.ArrayList;
import java.util.Map;
import java.util.Objects;
import java.util.Random;
import static android.com.provider.applicationUtils.App.listOfValues;


// https://stackoverflow.com/questions/40181654/firebase-fcm-open-activity-and-pass-data-on-notification-click-android // MARK : SHAHZEB

public class MyFirebaseMessagingService extends FirebaseMessagingService{
    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
        System.out.println("MyFirebaseInstanceIDService.onTokenRefresh " + s);
    }


    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        Hawk.init(this).build();

        System.out.println("MyFirebaseMessagingService.onMessageReceived " + remoteMessage.getFrom());

        // Check if message contains a data payload.


        if (remoteMessage.getData().size() > 0) {

            listOfValues.add(new OpenApportunity(remoteMessage.getData().get("title"), remoteMessage.getData().get("Service"), remoteMessage.getData().get("address"), remoteMessage.getData().get("Date"), remoteMessage.getData().get("Time"), remoteMessage.getData().get("job_id"), remoteMessage.getData().get("customer_id")));
            sendNotification(listOfValues);
            ((App) getApplicationContext()).getRxBus().send(new OpenApportunity(remoteMessage.getData().get("title"), remoteMessage.getData().get("Service"), remoteMessage.getData().get("address"), remoteMessage.getData().get("Date"), remoteMessage.getData().get("Time"), remoteMessage.getData().get("job_id"), remoteMessage.getData().get("customer_id")));


        }
        // Check if message contains a notification payload.
        else if (remoteMessage.getNotification() != null){

        }
    }

    private void sendNotification(ArrayList<OpenApportunity> listOfValues) {

        Intent intent = new Intent(this, ActivityOpenApportunity.class);
        intent.putExtra("arraylist", listOfValues);

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,PendingIntent.FLAG_ONE_SHOT);
        String channelId = "idddd";

        // MARK : FETCHING NOTIFICATION ID
//        long timenew = new Date().getTime();
//        String tmpStr = String.valueOf(timenew);
//        String last4Str = tmpStr.substring(tmpStr.length() - 5);
//        int notificationId = Integer.valueOf(last4Str);

        Random random = new Random();
        int m = random.nextInt(9999 - 1000) + 1000;

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(MyFirebaseMessagingService.this)
                .setSmallIcon(R.drawable.notification_icon)
                .setContentTitle("Provider")
                .setContentText("New Jobs")
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        Objects.requireNonNull(notificationManager).notify(m /* ID of notification */, notificationBuilder.build());
    }

    private void sendNotification(String title, String service, String zipcode, String date, String time, String job_id, String customer_id) {

        Intent intent = new Intent(this, ActivityOpenApportunity.class).

                putExtra("title", title).
                putExtra("Service", service).
                putExtra("address", zipcode).
                putExtra("Date", date).
                putExtra("Time", time).
                putExtra("job_id", job_id).
                putExtra("customer_id", customer_id);


        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        String channelId = "idddd";


        // MARK : FETCHING NOTIFICATION ID

//        long timenew = new Date().getTime();
//        String tmpStr = String.valueOf(timenew);
//        String last4Str = tmpStr.substring(tmpStr.length() - 5);
//        int notificationId = Integer.valueOf(last4Str);

        Random random = new Random();
        int m = random.nextInt(9999 - 1000) + 1000;

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(MyFirebaseMessagingService.this)
                .setSmallIcon(R.drawable.notification_icon)
                .setContentTitle(title)
                .setContentText("New Jobs")
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        Objects.requireNonNull(notificationManager).notify(m /* ID of notification */, notificationBuilder.build());


    }


    private void startInForeground(Map<String, String> data) {

        PendingIntent contentIntent = PendingIntent.getActivity(
                getApplicationContext(),
                0,
                new Intent(), // add this
                PendingIntent.FLAG_UPDATE_CURRENT);


        // Intent notificationIntent = new Intent(this, WorkoutActivity.class);
        //PendingIntent pendingIntent=PendingIntent.getActivity(this,0,notificationIntent,0);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, AppConstants.NOTIFICATION_CHANNEL_ID_CURRENT_LOCATION)
                .setSmallIcon(R.drawable.notification_icon)
                .setContentTitle(data.get("Provider"))
                .setContentText(data.get("New Jobs"))
                .setPriority(Notification.PRIORITY_DEFAULT)
                //.setContentText("HELLO")
                //.setTicker("TICKER")
                .setContentIntent(contentIntent);

        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);


        if (Build.VERSION.SDK_INT >= 26) {
            NotificationChannel channel = new NotificationChannel(AppConstants.NOTIFICATION_CHANNEL_ID_CURRENT_LOCATION, AppConstants.NOTIFICATION_CHANNEL_NAME_CURRENT_LOCATION, NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription(AppConstants.NOTIFICATION_CHANNEL_DESC_CURRENT_LOCATION);
            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(channel);
        }
        mNotificationManager.notify(1, builder.build());

    }


}
