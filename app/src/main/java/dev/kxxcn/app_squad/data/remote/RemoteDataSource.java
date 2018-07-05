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
import dev.kxxcn.app_squad.data.model.Battle;
import dev.kxxcn.app_squad.data.model.Information;
import dev.kxxcn.app_squad.data.model.Notification;
import dev.kxxcn.app_squad.data.model.User;
import dev.kxxcn.app_squad.data.model.message.Data;
import dev.kxxcn.app_squad.data.model.message.Send;
import dev.kxxcn.app_squad.util.Constants;
import dev.kxxcn.app_squad.util.DialogUtils;
import dev.kxxcn.app_squad.util.Dlog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static dev.kxxcn.app_squad.data.remote.APIPersistence.TYPE_REQUEST;
import static dev.kxxcn.app_squad.data.remote.APIPersistence.TYPE_RESPONSE;
import static dev.kxxcn.app_squad.util.Constants.TYPE_COLLECTION;

/**
 * Created by kxxcn on 2018-05-01.
 */

public class RemoteDataSource extends DataSource {

	public static final String COLLECTION_NAME_USER = "user";
	public static final String COLLECTION_NAME_MATCH = "match";
	private static final String COLLECTION_NAME_RECRUITMENT = "recruitment";
	private static final String COLLECTION_NAME_PLAYER = "player";
	public static final String COLLECTION_NAME_BATTLE = "battle";

	public static final String DOCUMENT_NAME_MESSAGE = "message";
	public static final String DOCUMENT_NAME_JOIN = "join";
	public static final String DOCUMENT_NAME_NOTICE = "notice";
	public static final String DOCUMENT_NAME_SQUAD = "squad";
	public static final String DOCUMENT_NAME_EVENT = "event";

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
			public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
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
			public void onCancelled(@NonNull DatabaseError databaseError) {
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
					public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
						List<Information> list = new ArrayList<>(0);
						for (DataSnapshot parentSnapshot : dataSnapshot.getChildren()) {
							for (DataSnapshot childSnapshot : parentSnapshot.getChildren()) {
								Dlog.i(childSnapshot.getValue().toString());
								final Information information = childSnapshot.getValue(Information.class);
								if (!information.isConnect()) {
									if (Integer.parseInt(DialogUtils.getFormattedDate(DialogUtils.getDate(), TYPE_COLLECTION)) <=
											Integer.parseInt(information.getDate().replace("-", ""))) {
										list.add(information);
									}
								}
							}
						}

						callback.onSuccess(list);
					}

					@Override
					public void onCancelled(@NonNull DatabaseError databaseError) {
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
		List<String> joinList = new ArrayList<>(0);
		joinList.add("SQUAD");
		information.setJoin(joinList);
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
	public void onLoadRecord(final GetBattleCallback callback) {
		DatabaseReference reference = FirebaseDatabase.getInstance().getReference(COLLECTION_NAME_BATTLE).child(mAuth.getCurrentUser().getUid());
		reference.addValueEventListener(new ValueEventListener() {
			@Override
			public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
				if (dataSnapshot.getChildrenCount() != 0) {
					List<Battle> battleList = new ArrayList<>(0);
					for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
						battleList.add(childSnapshot.getValue(Battle.class));
					}
					callback.onSuccess(battleList);
				}
			}

			@Override
			public void onCancelled(@NonNull DatabaseError databaseError) {
				callback.onFailure(databaseError.toException());
			}
		});
	}

	@Override
	public void onRequest(final GetSendMessageCallback callback, final String to, final String title, final String message, final String from, final String date) {
		DatabaseReference reference = FirebaseDatabase.getInstance().getReference(COLLECTION_NAME_USER);
		reference.addListenerForSingleValueEvent(new ValueEventListener() {
			@Override
			public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
				final String token = getTokenOfRrecipient(dataSnapshot, to, Constants.TYPE_REQUEST);

				if (token != null) {
					Data data = new Data();
					data.setTitle(title);
					data.setMessage(message);
					data.setSender(from);
					data.setDate(date);
					data.setType(TYPE_REQUEST);

					Send send = new Send();
					send.setTo(token);
					send.setData(data);

					Call<Void> call = service.sendMessage(send);
					call.enqueue(new Callback<Void>() {
						@Override
						public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
							if (response.isSuccessful()) {
								callback.onSuccess();
							} else {
								callback.onError();
							}
						}

						@Override
						public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
							callback.onFailure(t);
						}
					});
				} else {
					callback.onError();
				}
			}

			@Override
			public void onCancelled(@NonNull DatabaseError databaseError) {
				callback.onFailure(databaseError.toException());
			}
		});
	}

	@Override
	public void onLoadAccount(final GetUserCallback callback) {
		try {
			DatabaseReference accountReference = FirebaseDatabase.getInstance().getReference(COLLECTION_NAME_USER).child(mAuth.getCurrentUser().getUid());
			accountReference.addValueEventListener(new ValueEventListener() {
				@Override
				public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
					if (dataSnapshot.getChildrenCount() != 0) {
						callback.onSuccess(dataSnapshot.getValue(User.class));
					} else {
						callback.onFailure(new Exception());
					}
				}

				@Override
				public void onCancelled(@NonNull DatabaseError databaseError) {
					callback.onFailure(databaseError.toException());
				}
			});
		} catch (NullPointerException e) {
			e.printStackTrace();
			callback.onFailure(e);
		}
	}

	@Override
	public void onLoadNotification(final GetNotificationCallback callback) {
		final DatabaseReference reference = FirebaseDatabase.getInstance().getReference(COLLECTION_NAME_USER).child(mAuth.getCurrentUser().getUid()).child(DOCUMENT_NAME_MESSAGE);
		reference.addValueEventListener(new ValueEventListener() {
			@Override
			public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
				List<Notification> list = new ArrayList<>(0);
				for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
					list.add(snapshot.getValue(Notification.class));
				}
				callback.onSuccess(list);
			}

			@Override
			public void onCancelled(@NonNull DatabaseError databaseError) {
				callback.onFailure(databaseError.toException());
			}
		});
	}

	@Override
	public void onReadNotification(GetCommonCallback callback, final List<Notification> notifications) {
		final DatabaseReference reference = FirebaseDatabase.getInstance().getReference(COLLECTION_NAME_USER).child(mAuth.getCurrentUser().getUid()).child(DOCUMENT_NAME_MESSAGE);
		for (int i = 0; i < notifications.size(); i++) {
			reference.child(String.valueOf(notifications.get(i).getKey())).setValue(notifications.get(i));
		}
	}

	@Override
	public void onRemove(final GetCommonCallback callback, final String date) {
		DatabaseReference reference = FirebaseDatabase.getInstance().getReference(COLLECTION_NAME_MATCH).child(date).child(mAuth.getCurrentUser().getUid());
		reference.setValue(null).addOnSuccessListener(new OnSuccessListener<Void>() {
			@Override
			public void onSuccess(Void aVoid) {
				final DatabaseReference userReference = FirebaseDatabase.getInstance().getReference(COLLECTION_NAME_USER).child(mAuth.getCurrentUser().getUid()).child(DOCUMENT_NAME_MESSAGE);
				userReference.addListenerForSingleValueEvent(new ValueEventListener() {
					@Override
					public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
						for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
							Notification notification = childSnapshot.getValue(Notification.class);
							if (notification.getDate().equals(date)) {
								userReference.child(String.valueOf(notification.getKey())).setValue(null).addOnSuccessListener(new OnSuccessListener<Void>() {
									@Override
									public void onSuccess(Void aVoid) {
										callback.onSuccess();
									}
								}).addOnFailureListener(new OnFailureListener() {
									@Override
									public void onFailure(@NonNull Exception e) {
										callback.onFailure(e.getCause());
									}
								});
							}
						}
					}

					@Override
					public void onCancelled(@NonNull DatabaseError databaseError) {
						callback.onFailure(databaseError.toException());
					}
				});
				callback.onSuccess();
			}
		}).addOnFailureListener(new OnFailureListener() {
			@Override
			public void onFailure(@NonNull Exception e) {
				callback.onFailure(e.getCause());
			}
		});
	}

	@Override
	public void onLoadMatch(final GetInformationCallback callback, final String date, final Battle battle) {
		if (battle.isHome()) {
			DatabaseReference reference = FirebaseDatabase.getInstance().getReference(COLLECTION_NAME_MATCH).child(date).child(mAuth.getCurrentUser().getUid());
			reference.addListenerForSingleValueEvent(new ValueEventListener() {
				@Override
				public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
					try {
						callback.onSuccess(dataSnapshot.getValue(Information.class));
					} catch (NullPointerException e) {
						callback.onFailure(e);
						e.printStackTrace();
					}
				}

				@Override
				public void onCancelled(@NonNull DatabaseError databaseError) {
					callback.onFailure(databaseError.toException());
				}
			});
		} else {
			DatabaseReference reference = FirebaseDatabase.getInstance().getReference(COLLECTION_NAME_USER);
			reference.addListenerForSingleValueEvent(new ValueEventListener() {
				@Override
				public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
					for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
						User user = childSnapshot.getValue(User.class);
						if (battle.getEnemy().equals(user.getTeam())) {
							String uid = user.getUid();
							DatabaseReference matchReference = FirebaseDatabase.getInstance().getReference(COLLECTION_NAME_MATCH).child(date).child(uid);
							matchReference.addListenerForSingleValueEvent(new ValueEventListener() {
								@Override
								public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
									try {
										callback.onSuccess(dataSnapshot.getValue(Information.class));
									} catch (NullPointerException e) {
										callback.onFailure(e);
										e.printStackTrace();
									}
								}

								@Override
								public void onCancelled(@NonNull DatabaseError databaseError) {
									callback.onFailure(databaseError.toException());
								}
							});
							break;
						}
					}
				}

				@Override
				public void onCancelled(@NonNull DatabaseError databaseError) {

				}
			});
		}
	}

	@Override
	public void onLoadEnemyData(final GetUserCallback callback, final String enemy) {
		DatabaseReference reference = FirebaseDatabase.getInstance().getReference(COLLECTION_NAME_USER);
		reference.addListenerForSingleValueEvent(new ValueEventListener() {
			@Override
			public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
				for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
					User user = childSnapshot.getValue(User.class);
					if (user.getTeam().equals(enemy)) {
						callback.onSuccess(user);
						break;
					}
				}
			}

			@Override
			public void onCancelled(@NonNull DatabaseError databaseError) {
				callback.onFailure(databaseError.toException());
			}
		});
	}

	@Override
	public void onAgree(final GetSendMessageCallback callback, final Information information, final String title, final String message) {
		information.setConnect(true);
		DatabaseReference reference = FirebaseDatabase.getInstance().getReference(COLLECTION_NAME_MATCH)
				.child(DialogUtils.getFormattedDate(information.getDate(), TYPE_COLLECTION)).child(mAuth.getCurrentUser().getUid());
		reference.setValue(information).addOnSuccessListener(new OnSuccessListener<Void>() {
			@Override
			public void onSuccess(Void aVoid) {
				DatabaseReference reference = FirebaseDatabase.getInstance().getReference(COLLECTION_NAME_USER);
				reference.addListenerForSingleValueEvent(new ValueEventListener() {
					@Override
					public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
						final String token = getTokenOfRrecipient(dataSnapshot, information.getEnemy(), Constants.TYPE_RESPONSE);

						if (token != null) {
							Data data = new Data();
							data.setTitle(title);
							data.setMessage(message);
							data.setSender(information.getTeam());
							data.setDate(DialogUtils.getFormattedDate(information.getDate(), TYPE_COLLECTION));
							data.setType(TYPE_RESPONSE);
							data.setPlace(information.getPlace());

							Send send = new Send();
							send.setTo(token);
							send.setData(data);

							Call<Void> call = service.sendMessage(send);
							call.enqueue(new Callback<Void>() {
								@Override
								public void onResponse(@NonNull final Call<Void> call, @NonNull Response<Void> response) {
									if (response.isSuccessful()) {
										DatabaseReference battleReference = FirebaseDatabase.getInstance().getReference(COLLECTION_NAME_BATTLE)
												.child(mAuth.getCurrentUser().getUid()).child(DialogUtils.getFormattedDate(information.getDate(), TYPE_COLLECTION));
										battleReference.setValue(new Battle(information.getEnemy(), DialogUtils.getFormattedDate(information.getDate(), TYPE_COLLECTION), information.getPlace(), true))
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
									} else {
										callback.onError();
									}
								}

								@Override
								public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
									callback.onFailure(t);
								}
							});
						} else {
							callback.onError();
						}
					}

					@Override
					public void onCancelled(@NonNull DatabaseError databaseError) {
						callback.onFailure(databaseError.toException());
					}
				});
			}
		}).addOnFailureListener(new OnFailureListener() {
			@Override
			public void onFailure(@NonNull Exception e) {
				callback.onFailure(e);
			}
		});
	}

	@Override
	public void onUpdateNotice(GetCommonCallback callback, boolean on, Constants.NoticeFilterType requestType) {
		String type = null;
		switch (requestType) {
			case SQAUD:
				type = DOCUMENT_NAME_SQUAD;
				break;
			case NOTICE:
				type = DOCUMENT_NAME_NOTICE;
				break;
			case EVENT:
				type = DOCUMENT_NAME_EVENT;
				break;
		}
		DatabaseReference reference = FirebaseDatabase.getInstance().getReference(COLLECTION_NAME_USER).child(mAuth.getCurrentUser().getUid())
				.child(DOCUMENT_NAME_NOTICE).child(type);
		reference.setValue(on);
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

	/**
	 * 상대방 토큰 획득
	 *
	 * @author kxxcn
	 * @since 2018-06-25 오후 12:43
	 */
	private String getTokenOfRrecipient(DataSnapshot dataSnapshot, String to, int type) {
		List<User> userList = new ArrayList<>(0);
		for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
			userList.add(childSnapshot.getValue(User.class));
		}

		User user = null;

		for (int i = 0; i < userList.size(); i++) {
			switch (type) {
				case Constants.TYPE_REQUEST:
					if (to.equals(userList.get(i).getEmail())) {
						user = userList.get(i);
						break;
					}
					break;
				case Constants.TYPE_RESPONSE:
					if (to.equals(userList.get(i).getTeam())) {
						user = userList.get(i);
						break;
					}
					break;
			}
		}
		return user.getToken();
	}

}
