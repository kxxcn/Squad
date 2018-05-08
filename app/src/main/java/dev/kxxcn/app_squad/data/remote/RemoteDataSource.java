package dev.kxxcn.app_squad.data.remote;

import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

import dev.kxxcn.app_squad.data.DataSource;
import dev.kxxcn.app_squad.util.Dlog;

/**
 * Created by kxxcn on 2018-05-01.
 */

public class RemoteDataSource extends DataSource {

	private static RemoteDataSource remoteDataSource;

	private FirebaseAuth mAuth;
	private DatabaseReference mDatabaseReference;

	public RemoteDataSource(FirebaseAuth auth, DatabaseReference databaseReference) {
		this.mAuth = auth;
		this.mDatabaseReference = databaseReference;
	}

	public static synchronized RemoteDataSource getInstance(FirebaseAuth firebaseAuth,
															DatabaseReference databaseReference) {
		if (remoteDataSource == null) {
			remoteDataSource = new RemoteDataSource(firebaseAuth, databaseReference);
		}
		return remoteDataSource;
	}

	@Override
	public void onSignup(final GetSignupCallback callback, final String email, String password, final String team) {
		mAuth.createUserWithEmailAndPassword(email, password)
				.addOnCompleteListener(new OnCompleteListener<AuthResult>() {
					@Override
					public void onComplete(@NonNull Task<AuthResult> task) {
						if (task.isSuccessful()) {
							Dlog.i("UID : " + mAuth.getCurrentUser().getUid());
							mDatabaseReference.child("user").child(mAuth.getCurrentUser().getUid()).child("team").setValue(team);
							callback.onSuccess();
						} else {
							callback.onFailure(task.getException());
						}
					}
				});
	}

	@Override
	public void onLogin(final GetLoginCallback callback, String email, String password) {
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

}
