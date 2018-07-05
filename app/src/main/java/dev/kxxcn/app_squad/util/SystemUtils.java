package dev.kxxcn.app_squad.util;

import android.app.Activity;
import android.content.Context;
import android.os.PowerManager;
import android.os.Vibrator;
import android.util.Log;

import dev.kxxcn.app_squad.SquadApplication;

import static android.content.Context.VIBRATOR_SERVICE;
import static dev.kxxcn.app_squad.util.Constants.TAG;

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

	public static void onFinish(Activity activity) {
		activity.moveTaskToBack(true);
		activity.finish();
		android.os.Process.killProcess(android.os.Process.myPid());
	}

	public static class Dlog {
		/* ERROR */
		public static final void e(String message) {
			if (SquadApplication.DEBUG) Log.e(TAG, buildLogMsg(message));
		}

		/* WARNING */
		public static final void w(String message) {
			if (SquadApplication.DEBUG) Log.w(TAG, buildLogMsg(message));
		}

		/* INFORMATION */
		public static final void i(String message) {
			if (SquadApplication.DEBUG) Log.i(TAG, buildLogMsg(message));
		}

		/* DEBUG */
		public static final void d(String message) {
			if (SquadApplication.DEBUG) Log.d(TAG, buildLogMsg(message));
		}

		/* VERBOSE */
		public static final void v(String message) {
			if (SquadApplication.DEBUG) Log.v(TAG, buildLogMsg(message));
		}

		private static String buildLogMsg(String message) {
			StackTraceElement ste = Thread.currentThread().getStackTrace()[4];

			StringBuilder sb = new StringBuilder();

			sb.append("[");
			sb.append(ste.getFileName().replace(".java", ""));
			sb.append("::");
			sb.append(ste.getMethodName());
			sb.append("] ");
			sb.append(message);

			return sb.toString();
		}
	}

}
