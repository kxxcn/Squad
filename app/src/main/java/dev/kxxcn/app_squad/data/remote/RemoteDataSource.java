package dev.kxxcn.app_squad.data.remote;

import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import dev.kxxcn.app_squad.data.DataSource;
import dev.kxxcn.app_squad.data.model.Information;
import dev.kxxcn.app_squad.data.model.User;
import dev.kxxcn.app_squad.util.Constants;
import dev.kxxcn.app_squad.util.DialogUtils;
import dev.kxxcn.app_squad.util.Dlog;

/**
 * Created by kxxcn on 2018-05-01.
 */

public class RemoteDataSource extends DataSource {

	private static final String COLLECTION_USER = "user";
	private static final String COLLECTION_MATCH = "match";
	private static final String COLLECTION_RECRUITMENT = "recruitment";
	private static final String COLLECTION_PLAYER = "player";


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
		DatabaseReference reference = FirebaseDatabase.getInstance().getReference(COLLECTION_USER);
		reference.addListenerForSingleValueEvent(new ValueEventListener() {
			@Override
			public void onDataChange(DataSnapshot dataSnapshot) {
				for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
					User user = snapshot.getValue(User.class);
					/* 팀명 중복 체크 */
					if (team.equals(user.getTeam())) {
						callback.onDuplicatedTeam();
						mIsDuplicate = true;
						break;
					} else {
						mIsDuplicate = false;
					}
				}
				onCreateUser(callback, email, password, team);
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
								User user = new User(email, uid, team);
								mReference.child(COLLECTION_USER).child(uid).setValue(user).addOnSuccessListener(new OnSuccessListener<Void>() {
									@Override
									public void onSuccess(Void aVoid) {
										callback.onSuccess();
									}
								}).addOnFailureListener(new OnFailureListener() {
									@Override
									public void onFailure(@NonNull Exception e) {
										callback.onFailure(e);
									}
								});
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
			remoteDataSource = null;
			mAuth.signOut();
			callback.onSuccess();
		} else {
			callback.onFailure(new Exception());
		}
	}

	@Override
	public void onLoad(final GetLoadCallback callback, Constants.ListsFilterType requestType) {
		switch (requestType) {
			case MATCH_LIST:
				DatabaseReference reference = FirebaseDatabase.getInstance().getReference(COLLECTION_MATCH);
				reference.addValueEventListener(new ValueEventListener() {
					@Override
					public void onDataChange(DataSnapshot dataSnapshot) {
						List<Information> list = new ArrayList<>(0);
						for (DataSnapshot parentSnapshot : dataSnapshot.getChildren()) {
							Dlog.d("data Count: " + dataSnapshot.getChildrenCount());
							Dlog.d("parent Count: " + parentSnapshot.getChildrenCount());
							for (DataSnapshot childSnapshot : parentSnapshot.getChildren()) {
								Dlog.d("child : " + childSnapshot.getValue());
								final Information information = childSnapshot.getValue(Information.class);
								list.add(information);
							}
						}

						callback.onSuccess(list);
					}

					@Override
					public void onCancelled(DatabaseError databaseError) {
						callback.onFailure(databaseError.toException());
					}
				});
				break;
			case PLAYER_LIST:
				break;
			case RECRUITMENT_LIST:
				break;
		}
	}

	@Override
	public void onRegister(final GetCommonCallback callback, final Information information, final Constants.ListsFilterType requestType) {
		DatabaseReference reference = FirebaseDatabase.getInstance().getReference(COLLECTION_USER).child(mAuth.getCurrentUser().getUid());
		reference.addListenerForSingleValueEvent(new ValueEventListener() {
			@Override
			public void onDataChange(DataSnapshot dataSnapshot) {
				User user = dataSnapshot.getValue(User.class);
				information.setTeam(user.getTeam());
				String collection = null;
				switch (requestType) {
					case MATCH_LIST:
						collection = COLLECTION_MATCH;
						break;
					case RECRUITMENT_LIST:
						collection = COLLECTION_RECRUITMENT;
						break;
					case PLAYER_LIST:
						collection = COLLECTION_PLAYER;
						break;
				}

				mReference.child(collection).child(DialogUtils.getFormattedDate(information.getDate())).child(mAuth.getCurrentUser().getUid()).setValue(information)
						.addOnSuccessListener(new OnSuccessListener<Void>() {
							@Override
							public void onSuccess(Void aVoid) {
								callback.onSuccess();
							}
						}).addOnFailureListener(new OnFailureListener() {
					@Override
					public void onFailure(@NonNull Exception e) {
						callback.onFailure(e);
					}
				});
			}

			@Override
			public void onCancelled(DatabaseError databaseError) {

			}
		});
	}

}
