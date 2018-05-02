package dev.kxxcn.app_squad.util;

import android.util.Log;

import dev.kxxcn.app_squad.SquadApplication;

import static dev.kxxcn.app_squad.util.Constants.*;


/**
 * Created by kxxcn on 2018-04-26.
 */
public class Dlog {

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
