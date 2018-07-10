package dev.kxxcn.app_squad.data.remote;

import android.content.SharedPreferences;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import dev.kxxcn.app_squad.util.SystemUtils;


/**
 * Created by kxxcn on 2018-06-11.
 */

public class MyFirebaseInstanceIdService extends FirebaseInstanceIdService {

	public static final String NAME = "pref";
	public static final String KEY = "token";

	@Override
	public void onTokenRefresh() {
		saveNewToken(FirebaseInstanceId.getInstance().getToken());
	}

	private void saveNewToken(String token) {
		SystemUtils.Dlog.v("New token : " + token);
		SharedPreferences preferences = getSharedPreferences(NAME, MODE_PRIVATE);
		SharedPreferences.Editor editor = preferences.edit();
		editor.putString(KEY, token);
		editor.apply();
	}

}
