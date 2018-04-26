package dev.kxxcn.app_squad.application;

import android.app.Application;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;

/**
 * Created by Jun on 2018-04-26.
 */

public class SquadApplication extends Application {

	public static boolean DEBUG = false;

	@Override
	protected void attachBaseContext(Context base) {
		super.attachBaseContext(base);
	}

	@Override
	public void onCreate() {
		super.onCreate();
		this.DEBUG = isDebuggable(this);
	}

	private boolean isDebuggable(Context context) {
		boolean debuggable = false;

		PackageManager pm = context.getPackageManager();
		try {
			ApplicationInfo appinfo = pm.getApplicationInfo(context.getPackageName(), 0);
			debuggable = (0 != (appinfo.flags & ApplicationInfo.FLAG_DEBUGGABLE));
		} catch (PackageManager.NameNotFoundException e) {
			/* debuggable variable will remain false */
		}

		return debuggable;
	}

}
