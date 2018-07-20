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

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
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
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.TimeZone;

import dev.kxxcn.app_squad.R;
import dev.kxxcn.app_squad.data.model.Battle;
import dev.kxxcn.app_squad.data.model.Information;
import dev.kxxcn.app_squad.data.model.Notification;
import dev.kxxcn.app_squad.ui.main.MainActivity;
import dev.kxxcn.app_squad.ui.main.team.notification.content.introduce.chat.ChattingDialog;
import dev.kxxcn.app_squad.util.SystemUtils;

import static dev.kxxcn.app_squad.data.remote.APIPersistence.TYPE_CHATTING;
import static dev.kxxcn.app_squad.data.remote.APIPersistence.TYPE_REQUEST;
import static dev.kxxcn.app_squad.data.remote.APIPersistence.TYPE_RESPONSE;
import static dev.kxxcn.app_squad.util.Constants.SIMPLE_DATE_FORMAT1;
import static dev.kxxcn.app_squad.util.Constants.VIBRATE_NOTIFICATION;

/**
 * Created by kxxcn on 2018-06-11.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {

	private static final String FCM_TITLE = "title";
	private static final String FCM_MESSAGE = "message";
	private static final String FCM_SENDER = "sender";
	private static final String FCM_DATE = "date";
	private static final String FCM_TYPE = "type";
	private static final String FCM_PLACE = "place";
	private static final String FCM_FLAG = "flag";

	private static final boolean DID_NOT_CHECK = false;
	private static final boolean CHECKED = true;

	private static final String INIT = "1";

	private static final int MATCH_LIST = 0;
	private static final int RECRUITMENT_LIST = 1;
	private static final int PLAYER_LIST = 2;

	private SimpleDateFormat format = new SimpleDateFormat(SIMPLE_DATE_FORMAT1, Locale.KOREA);

	public static NotificationManager notificationManager;

	@Override
	public void onMessageReceived(RemoteMessage remoteMessage) {
		sendNotification(remoteMessage.getData().get(FCM_TITLE), remoteMessage.getData().get(FCM_MESSAGE),
				remoteMessage.getData().get(FCM_SENDER), remoteMessage.getSentTime(), remoteMessage.getData().get(FCM_DATE),
				remoteMessage.getData().get(FCM_TYPE), remoteMessage.getData().get(FCM_PLACE), remoteMessage.getData().get(FCM_FLAG));
	}

	private void sendNotification(String title, String message, String from, long time, String matchDate, String type, String place, String flag) {
		Date date = new Date(time);
		format.setTimeZone(TimeZone.getTimeZone("Asia/Seoul"));
		String timestamp = format.format(date);
		String roomName = null;
		if (type.equals(TYPE_CHATTING)) {
			roomName = message;
			message = String.format(getApplicationContext().getString(R.string.received_message), from);
		} else {
			if (flag.equals(String.valueOf(RemoteDataSource.FLAG_PLAYER_LIST))) {
				int index = message.indexOf("/");
				String content = message.substring(0, index);
				String[] information = message.substring(index + 1, message.length()).split("#");
				for (int i = 0; i < information.length; i++) {
					SystemUtils.Dlog.d(information[i]);
				}
				onRegisterNotification(new Notification(content, from, timestamp, DID_NOT_CHECK, matchDate, type, flag,
						information[0], information[1], information[2], information[3]));

			} else {
				onRegisterNotification(new Notification(message, from, timestamp, DID_NOT_CHECK, matchDate, type, flag));
			}
			onRegisterBattle(new Battle(from, matchDate, place, false), type, flag);
		}

		Intent intent = new Intent(this, MainActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

		PendingIntent pendingIntent = PendingIntent.getActivity(
				this,
				0 /* Request code */,
				intent,
				PendingIntent.FLAG_ONE_SHOT);

		Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

		notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

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

		if (type.equals(TYPE_CHATTING)) {
			if (!ChattingDialog.DAY.equals(matchDate) && !ChattingDialog.ROOM_NAME.equals(roomName)) {
				notificationManager.notify(0, notificationBuilder.build());
				SystemUtils.onAcquire(this);
				SystemUtils.onVibrate(this, VIBRATE_NOTIFICATION);
				onRegisterNotification(new Notification(message, from, timestamp, DID_NOT_CHECK, matchDate, type, flag));
			}
			if (ChattingDialog.DAY.equals(matchDate) && ChattingDialog.ROOM_NAME.equals(roomName)) {
				onRegisterNotification(new Notification(message, from, timestamp, CHECKED, matchDate, type, flag));
			}
		} else {
			notificationManager.notify(new Random().nextInt() /* ID of notification */, notificationBuilder.build());
			SystemUtils.onAcquire(this);
			SystemUtils.onVibrate(this, VIBRATE_NOTIFICATION);
		}
	}

	private void onRegisterNotification(final Notification notification) {
		final DatabaseReference reference = FirebaseDatabase.getInstance().getReference(RemoteDataSource.COLLECTION_NAME_USER)
				.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(RemoteDataSource.DOCUMENT_NAME_MESSAGE);
		reference.addListenerForSingleValueEvent(new ValueEventListener() {
			@Override
			public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
				String key = null;
				if (dataSnapshot.getChildrenCount() == 0) {
					key = INIT;
				} else {
					for (DataSnapshot parentSnapshot : dataSnapshot.getChildren()) {
						key = String.valueOf(Integer.parseInt(parentSnapshot.getKey()) + 1);
					}
				}
				notification.setKey(Integer.parseInt(key));
				reference.child(key).setValue(notification).addOnSuccessListener(new OnSuccessListener<Void>() {
					@Override
					public void onSuccess(Void aVoid) {
						if (notification.getType().equals(TYPE_REQUEST)) {
							DatabaseReference listReference = null;
							int type = Integer.parseInt(notification.getFlag());
							switch (type) {
								case MATCH_LIST:
									listReference = FirebaseDatabase.getInstance().getReference(RemoteDataSource.COLLECTION_NAME_MATCH)
											.child(notification.getDate()).child(FirebaseAuth.getInstance().getCurrentUser().getUid());
									break;
								case RECRUITMENT_LIST:
									listReference = FirebaseDatabase.getInstance().getReference(RemoteDataSource.COLLECTION_NAME_RECRUITMENT)
											.child(notification.getDate()).child(FirebaseAuth.getInstance().getCurrentUser().getUid());
									break;
								case PLAYER_LIST:
									listReference = FirebaseDatabase.getInstance().getReference(RemoteDataSource.COLLECTION_NAME_PLAYER)
											.child(notification.getDate()).child(FirebaseAuth.getInstance().getCurrentUser().getUid());
									break;
							}
							final DatabaseReference searchReference = listReference;
							listReference.addListenerForSingleValueEvent(new ValueEventListener() {
								@Override
								public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
									Information information = dataSnapshot.getValue(Information.class);
									List<String> joinList = information.getJoin();
									joinList.add(notification.getSender());
									searchReference.child(RemoteDataSource.DOCUMENT_NAME_JOIN).setValue(joinList).addOnSuccessListener(new OnSuccessListener<Void>() {
										@Override
										public void onSuccess(Void aVoid) {
											SystemUtils.Dlog.d("Success!");
										}
									}).addOnFailureListener(new OnFailureListener() {
										@Override
										public void onFailure(@NonNull Exception e) {
											SystemUtils.Dlog.e(e.getMessage());
										}
									});
								}

								@Override
								public void onCancelled(@NonNull DatabaseError databaseError) {

								}
							});
						}
					}
				}).addOnFailureListener(new OnFailureListener() {
					@Override
					public void onFailure(@NonNull Exception e) {
						SystemUtils.Dlog.e(e.getMessage());
					}
				});
			}

			@Override
			public void onCancelled(@NonNull DatabaseError databaseError) {
				SystemUtils.Dlog.e(databaseError.getMessage());
			}
		});
	}

	private void onRegisterBattle(final Battle battle, String type, String flag) {
		if (type.equals(TYPE_RESPONSE)) {
			if (Integer.parseInt(flag) == RemoteDataSource.FLAG_MATCH_LIST) {
				DatabaseReference reference = FirebaseDatabase.getInstance().getReference(RemoteDataSource.COLLECTION_NAME_BATTLE)
						.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(battle.getDate());
				reference.setValue(battle);
			}
		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}
}
