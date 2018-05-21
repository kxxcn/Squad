package dev.kxxcn.app_squad.data.remote;

import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

import dev.kxxcn.app_squad.data.DataSource;
import dev.kxxcn.app_squad.data.model.Information;
import dev.kxxcn.app_squad.util.Constants;
import dev.kxxcn.app_squad.util.Dlog;

/**
 * Created by kxxcn on 2018-05-01.
 */

public class RemoteDataSource extends DataSource {

	private static final String COLLECTION_USER = "user";
	private static final String DOCUMENT_TEAM = "team";

	private static RemoteDataSource remoteDataSource;

	private FirebaseAuth mAuth;
	private DatabaseReference mReference;

	public RemoteDataSource(FirebaseAuth auth, DatabaseReference reference) {
		this.mAuth = auth;
		this.mReference = reference;
	}

	public static synchronized RemoteDataSource getInstance(FirebaseAuth firebaseAuth,
															DatabaseReference databaseReference) {
		if (remoteDataSource == null) {
			remoteDataSource = new RemoteDataSource(firebaseAuth, databaseReference);
		}
		return remoteDataSource;
	}

	@Override
	public void onSignup(final GetCommonCallback callback, final String email, String password, final String team) {
		mAuth.createUserWithEmailAndPassword(email, password)
				.addOnCompleteListener(new OnCompleteListener<AuthResult>() {
					@Override
					public void onComplete(@NonNull Task<AuthResult> task) {
						if (task.isSuccessful()) {
							String uid = mAuth.getCurrentUser().getUid();
							Dlog.v("UID : " + uid);
							mReference.child(COLLECTION_USER).child(uid).child(DOCUMENT_TEAM).setValue(team);
							callback.onSuccess();
						} else {
							callback.onFailure(task.getException());
						}
					}
				});
	}

	@Override
	public void onLogin(final GetCommonCallback callback, String email, String password) {
		mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
			@Override
			public void onComplete(@NonNull Task<AuthResult> task) {
				if (task.isSuccessful()) {
					callback.onSuccess();
				} else {
					callback.onFailure(task.getException());
				}
			}
		});
	}

	@Override
	public void onLogout(GetCommonCallback callback) {
		if (mAuth.getCurrentUser() != null) {
			mAuth.signOut();
			callback.onSuccess();
		} else {
			callback.onFailure(new Exception());
		}
	}

	@Override
	public void onLoad(GetLoadCallback callback, Constants.ListsFilterType requestType) {
		switch (requestType) {
			case MATCH_LIST:
				break;
			case PLAYER_LIST:
				break;
			case RECRUITMENT_LIST:
				break;
		}
	}

	@Override
	public void onRegister(GetCommonCallback callback, Information information, Constants.ListsFilterType requestType) {
		switch (requestType) {
			case MATCH_LIST:
				break;
			case PLAYER_LIST:
				break;
			case RECRUITMENT_LIST:
				break;
		}
	}

}
