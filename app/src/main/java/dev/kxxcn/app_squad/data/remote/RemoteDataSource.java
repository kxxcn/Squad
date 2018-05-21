package dev.kxxcn.app_squad.data.remote;

import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import dev.kxxcn.app_squad.data.DataSource;
import dev.kxxcn.app_squad.data.model.Information;
import dev.kxxcn.app_squad.data.model.User;
import dev.kxxcn.app_squad.util.Constants;
import dev.kxxcn.app_squad.util.Dlog;

/**
 * Created by kxxcn on 2018-05-01.
 */

public class RemoteDataSource extends DataSource {

	private static final String COLLECTION_USER = "user";

	private static final String DOCUMENT_EMAIL = "email";
	private static final String DOCUMENT_TEAM = "team";

	private static RemoteDataSource remoteDataSource;

	private FirebaseAuth mAuth;
	private DatabaseReference mReference;

	private boolean mIsDuplicate = false;

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
	public void onSignup(final GetSignupCallback callback, final String email, final String password, final String team) {
		DatabaseReference database = FirebaseDatabase.getInstance().getReference(COLLECTION_USER);
		database.addListenerForSingleValueEvent(new ValueEventListener() {
			@Override
			public void onDataChange(DataSnapshot dataSnapshot) {
				for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
					User user = snapshot.getValue(User.class);
					if (team.equals(user.getTeam())) {
						callback.onDuplicatedTeam();
						mIsDuplicate = true;
						break;
					} else {
						mIsDuplicate = false;
					}
					onCreateUser(callback, email, password, team);
				}
			}

			@Override
			public void onCancelled(DatabaseError databaseError) {
				callback.onFailure(databaseError.toException());
			}
		});
	}

	private void onCreateUser(final GetSignupCallback callback, final String email, final String password, final String team) {
		if (!mIsDuplicate) {
			mAuth.createUserWithEmailAndPassword(email, password)
					.addOnCompleteListener(new OnCompleteListener<AuthResult>() {
						@Override
						public void onComplete(@NonNull Task<AuthResult> task) {
							if (task.isSuccessful()) {
								String uid = mAuth.getCurrentUser().getUid();
								Dlog.v("UID : " + uid);
								mReference.child(COLLECTION_USER).child(uid).child(DOCUMENT_EMAIL).setValue(email);
								mReference.child(COLLECTION_USER).child(uid).child(DOCUMENT_TEAM).setValue(team);
								callback.onSuccess();
							} else {
								callback.onFailure(task.getException());
							}
						}
					});
		}
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
