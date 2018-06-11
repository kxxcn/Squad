package dev.kxxcn.app_squad.data.remote;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by kxxcn on 2018-06-11.
 */

public class MyFirebaseInstanceIdService extends FirebaseInstanceIdService {

	@Override
	public void onTokenRefresh() {
		saveNewToken(FirebaseInstanceId.getInstance().getToken());
	}

	private void saveNewToken(String token) {

	}

}
