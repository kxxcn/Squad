package dev.kxxcn.app_squad.data.remote;

import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import java.util.HashMap;
import java.util.Map;

import dev.kxxcn.app_squad.util.Dlog;

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
		Dlog.i("New token : " + token);
		Map<String, Object> task = new HashMap<>();
		task.put(TOKEN, token);
		try {
			DatabaseReference reference = FirebaseDatabase.getInstance().getReference()
					.child(COLLECTION_NAME_USER).child(FirebaseAuth.getInstance().getCurrentUser().getUid());
			reference.setValue(task).addOnFailureListener(new OnFailureListener() {
				@Override
				public void onFailure(@NonNull Exception e) {
					Dlog.e(e.getMessage());
				}
			});
		} catch (NullPointerException e) {
			e.printStackTrace();
			Dlog.e(e.getMessage());
		}
	}

}
