package dev.kxxcn.app_squad.data.remote;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by kxxcn on 2018-06-11.
 */

public class MyFirebaseInstanceIdService extends FirebaseInstanceIdService {

	private static final String COLLECTION_NAME_USER = "user";

	private static final String TOKEN = "token";

	@Override
	public void onTokenRefresh() {
		saveNewToken(FirebaseInstanceId.getInstance().getToken());
	}

	private void saveNewToken(String token) {
		Map<String, Object> task = new HashMap<>();
		task.put(TOKEN, token);
		DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
		reference.child(COLLECTION_NAME_USER + "/" + FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(task);
	}

}
