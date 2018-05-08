package dev.kxxcn.app_squad.util;

import android.app.Activity;

import dev.kxxcn.app_squad.R;

/**
 * Created by kxxcn on 2018-05-02.
 */

public class TransitionUtils {
	public static void fade(Activity activity) {
		activity.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
	}
}
