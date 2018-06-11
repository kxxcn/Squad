package dev.kxxcn.app_squad.data.remote;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.PowerManager;
import android.support.v4.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Random;

import dev.kxxcn.app_squad.R;
import dev.kxxcn.app_squad.ui.main.MainActivity;
import dev.kxxcn.app_squad.util.Dlog;

/**
 * Created by kxxcn on 2018-06-11.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {

	@Override
	public void onMessageReceived(RemoteMessage remoteMessage) {
		Dlog.d("onMessageReceived");
		if (remoteMessage.getData().isEmpty()) {
			sendNotification(remoteMessage.getNotification().getTitle(), remoteMessage.getNotification().getBody());
		} else {
			sendNotification(remoteMessage.getData().get("title"), remoteMessage.getData().get("message"));
		}
	}

	private void sendNotification(String title, String message) {
		PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
		PowerManager.WakeLock wakeLock = pm.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK |
				PowerManager.ACQUIRE_CAUSES_WAKEUP, getString(R.string.app_name));
		wakeLock.acquire(3000);

		Intent intent = new Intent(this, MainActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

		PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
				PendingIntent.FLAG_ONE_SHOT);

		Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

		NotificationManager notificationManager =
				(NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

		NotificationCompat.Builder notificationBuilder;


		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
			NotificationChannel channel = new NotificationChannel(
					getString(R.string.app_name),
					getString(R.string.app_name),
					NotificationManager.IMPORTANCE_DEFAULT);
			notificationManager = (android.app.NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
			notificationManager.createNotificationChannel(channel);
			notificationBuilder = new NotificationCompat.Builder(this, channel.getId());
		} else {
			notificationBuilder = new NotificationCompat.Builder(this);
		}

		notificationBuilder.setSmallIcon(getApplicationInfo().icon)
				.setContentTitle(title)
				.setContentText(message)
				.setAutoCancel(true)
				.setSound(defaultSoundUri)
				.setContentIntent(pendingIntent);

		notificationManager.notify(new Random().nextInt() /* ID of notification */, notificationBuilder.build());
	}

}
