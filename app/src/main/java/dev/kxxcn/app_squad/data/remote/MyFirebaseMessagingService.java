package dev.kxxcn.app_squad.data.remote;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Random;
import java.util.TimeZone;

import dev.kxxcn.app_squad.R;
import dev.kxxcn.app_squad.data.model.Notification;
import dev.kxxcn.app_squad.ui.main.MainActivity;
import dev.kxxcn.app_squad.util.SystemUtils;

import static dev.kxxcn.app_squad.util.Constants.VIBRATE_NOTIFICATION;

/**
 * Created by kxxcn on 2018-06-11.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {

	private final static String FCM_TITLE = "title";
	private final static String FCM_MESSAGE = "message";
	private final static String FCM_SENDER = "sender";

	private final static boolean DID_NOT_CHECK = false;

	private SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm", Locale.KOREA);

	@Override
	public void onMessageReceived(RemoteMessage remoteMessage) {
		if (remoteMessage.getData().isEmpty()) {
			sendNotification(remoteMessage.getNotification().getTitle(), remoteMessage.getNotification().getBody(),
					getString(R.string.app_name), 0);
		} else {
			sendNotification(remoteMessage.getData().get(FCM_TITLE), remoteMessage.getData().get(FCM_MESSAGE),
					remoteMessage.getData().get(FCM_SENDER), remoteMessage.getSentTime());
		}
	}

	private void sendNotification(String title, String message, String from, long time) {
		SystemUtils.onAcquire(this);
		SystemUtils.onVibrate(this, VIBRATE_NOTIFICATION);

		Date date = new Date(time);
		format.setTimeZone(TimeZone.getTimeZone("Asia/Seoul"));
		String timestamp = format.format(date);

		onRegisterNotification(new Notification(message, from, timestamp, DID_NOT_CHECK));

		Intent intent = new Intent(this, MainActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

		PendingIntent pendingIntent = PendingIntent.getActivity(
				this,
				0 /* Request code */,
				intent,
				PendingIntent.FLAG_ONE_SHOT);

		Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

		NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

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

	private void onRegisterNotification(final Notification notification) {
		final DatabaseReference reference = FirebaseDatabase.getInstance().getReference(RemoteDataSource.COLLECTION_NAME_USER)
				.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(RemoteDataSource.DOCUMENT_NAME_MESSAGE);
		reference.addListenerForSingleValueEvent(new ValueEventListener() {
			@Override
			public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
				notification.setKey((int) (dataSnapshot.getChildrenCount() + 1));
				reference.child(String.valueOf(dataSnapshot.getChildrenCount() + 1)).setValue(notification);
			}

			@Override
			public void onCancelled(@NonNull DatabaseError databaseError) {

			}
		});
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}
}
