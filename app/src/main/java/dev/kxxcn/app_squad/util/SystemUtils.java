package dev.kxxcn.app_squad.util;

import android.content.Context;
import android.os.PowerManager;
import android.os.Vibrator;

import static android.content.Context.VIBRATOR_SERVICE;

/**
 * Created by kxxcn on 2018-06-14.
 */

public class SystemUtils {

	private static final String WAKELOCK_TAG = "wakelock";

	public static void onAcquire(Context context) {
		PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
		PowerManager.WakeLock wakeLock = pm.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK
				| PowerManager.ACQUIRE_CAUSES_WAKEUP, WAKELOCK_TAG);
		wakeLock.acquire(3000);

		if (wakeLock != null) {
			wakeLock.release();
			wakeLock = null;
		}
	}

	public static void onVibrate(Context context, long time) {
		Vibrator vibrator = (Vibrator) context.getSystemService(VIBRATOR_SERVICE);
		vibrator.vibrate(1000);
	}

}
