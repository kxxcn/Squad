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
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.ArrayList;
import java.util.List;

import dev.kxxcn.app_squad.data.DataSource;
import dev.kxxcn.app_squad.data.model.Account;
import dev.kxxcn.app_squad.data.model.Information;
import dev.kxxcn.app_squad.data.model.User;
import dev.kxxcn.app_squad.data.model.message.Data;
import dev.kxxcn.app_squad.data.model.message.Send;
import dev.kxxcn.app_squad.util.Constants;
import dev.kxxcn.app_squad.util.DialogUtils;
import dev.kxxcn.app_squad.util.Dlog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static dev.kxxcn.app_squad.util.Constants.TYPE_COLLECTION;

/**
 * Created by kxxcn on 2018-05-01.
 */

public class RemoteDataSource extends DataSource {

	private static final String COLLECTION_NAME_USER = "user";
	private static final String COLLECTION_NAME_MATCH = "match";
	private static final String COLLECTION_NAME_RECRUITMENT = "recruitment";
	private static final String COLLECTION_NAME_PLAYER = "player";

	private static final String FCM_SERVER_URL = "https://fcm.googleapis.com/fcm/send";
	private static final String FCM_SERVER_KEY = "AAAAogOTo-4:APA91bEJaeSUvNeumE60WxDyc96lzl-wRck3tWw-i6g4j0_EAk2_IKv7lPWUmh2DQ5xP6pYTg-LMzac4MGtxM9PHzl6f43i7ObCTHbeIxBwIibATL2JIYIZ0B_EpyBPBOq6eTI4onSPc";

	private static final String FCM_JSON_TITLE = "title";
	private static final String FCM_JSON_MESSAGE = "message";
	private static final String FCM_JSON_DATA = "data";
	private static final String FCM_JSON_TO = "to";

	private static RemoteDataSource remoteDataSource;

	private FirebaseAuth mAuth;
	private DatabaseReference mReference;

	private boolean mIsDuplicate = false;

	private APIService service;

	public RemoteDataSource(FirebaseAuth auth, DatabaseReference reference) {
		this.mAuth = auth;
		this.mReference = reference;
		this.service = APIService.Factory.create();
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
		DatabaseReference reference = FirebaseDatabase.getInstance().getReference(COLLECTION_NAME_USER);
		reference.addListenerForSingleValueEvent(new ValueEventListener() {
			@Override
			public void onDataChange(DataSnapshot dataSnapshot) {
				for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
					User user = snapshot.getValue(User.class);
					if (isDuplicate(team, user.getTeam())) {
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
								String token = FirebaseInstanceId.getInstance().getToken();
								User user = new User(email, uid, team, token);
								mReference.child(COLLECTION_NAME_USER).child(uid).setValue(user).addOnSuccessListener(new OnSuccessListener<Void>() {
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
	public void onLoadList(final GetLoadListCallback callback, Constants.ListsFilterType requestType) {
		switch (requestType) {
			case MATCH_LIST:
				DatabaseReference reference = FirebaseDatabase.getInstance().getReference(COLLECTION_NAME_MATCH);
				reference.addValueEventListener(new ValueEventListener() {
					@Override
					public void onDataChange(DataSnapshot dataSnapshot) {
						List<Information> list = new ArrayList<>(0);
						for (DataSnapshot parentSnapshot : dataSnapshot.getChildren()) {
							Dlog.v("Data Count: " + dataSnapshot.getChildrenCount());
							Dlog.v("Parent Count: " + parentSnapshot.getChildrenCount());
							for (DataSnapshot childSnapshot : parentSnapshot.getChildren()) {
								Dlog.i("Child : " + childSnapshot.getValue());
								final Information information = childSnapshot.getValue(Information.class);
								if (!information.isConnect()) {
									list.add(information);
								}
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
		information.setTeam(Account.getInstance().getTeam());
		String collection = null;
		switch (requestType) {
			case MATCH_LIST:
				collection = COLLECTION_NAME_MATCH;
				break;
			case RECRUITMENT_LIST:
				collection = COLLECTION_NAME_RECRUITMENT;
				break;
			case PLAYER_LIST:
				collection = COLLECTION_NAME_PLAYER;
				break;
		}

		mReference.child(collection).child(DialogUtils.getFormattedDate(information.getDate(), TYPE_COLLECTION)).child(mAuth.getCurrentUser().getUid()).setValue(information)
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
	public void onLoadRecord(final GetLoadRecordCallback callback) {

	}

	@Override
	public void onSendMessage(final GetSendMessageCallback callback, final String to, final String title, final String message) {
		DatabaseReference reference = FirebaseDatabase.getInstance().getReference(COLLECTION_NAME_USER);
		reference.addListenerForSingleValueEvent(new ValueEventListener() {
			@Override
			public void onDataChange(DataSnapshot dataSnapshot) {
				List<User> userList = new ArrayList<>(0);
				for (DataSnapshot parentSnapshot : dataSnapshot.getChildren()) {
					userList.add(parentSnapshot.getValue(User.class));
				}

				User user = null;
				for (int i = 0; i < userList.size(); i++) {
					if (to.equals(userList.get(i).getEmail())) {
						user = userList.get(i);
					}
				}
				final String token = user.getToken();

				if (token != null) {
					Data data = new Data();
					data.setTitle(title);
					data.setMessage(message);
					Send send = new Send();
					send.setTo(token);
					send.setData(data);
					Call<Void> call = service.sendMessage(send);
					call.enqueue(new Callback<Void>() {
						@Override
						public void onResponse(Call<Void> call, Response<Void> response) {
							callback.onSuccess();
						}

						@Override
						public void onFailure(Call<Void> call, Throwable t) {
							callback.onFailure(t);
						}
					});
				} else {
					callback.onErrorNoData();
				}
			}

			@Override
			public void onCancelled(DatabaseError databaseError) {
				callback.onFailure(databaseError.toException());
			}
		});
	}

	@Override
	public void onLoadAccount(final GetCommonCallback callback) {
		DatabaseReference accountReference = FirebaseDatabase.getInstance().getReference(COLLECTION_NAME_USER).child(mAuth.getCurrentUser().getUid());
		accountReference.addListenerForSingleValueEvent(new ValueEventListener() {
			@Override
			public void onDataChange(DataSnapshot dataSnapshot) {
				new Account(dataSnapshot.getValue(User.class));
				callback.onSuccess();
			}

			@Override
			public void onCancelled(DatabaseError databaseError) {
				callback.onFailure(databaseError.toException());
			}
		});
	}

	/**
	 * 팀명 중복 체크
	 *
	 * @author kxxcn
	 * @since 2018-05-30 오후 5:55
	 */
	private boolean isDuplicate(String team, String fixedTeam) {
		boolean rtn = false;
		if (team.equals(fixedTeam)) {
			rtn = true;
		}
		return rtn;
	}

}
