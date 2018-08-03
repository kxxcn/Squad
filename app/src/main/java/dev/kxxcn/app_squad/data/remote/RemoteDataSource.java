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
import java.util.Random;

import dev.kxxcn.app_squad.data.DataSource;
import dev.kxxcn.app_squad.data.model.Battle;
import dev.kxxcn.app_squad.data.model.Chatting;
import dev.kxxcn.app_squad.data.model.Information;
import dev.kxxcn.app_squad.data.model.Notification;
import dev.kxxcn.app_squad.data.model.User;
import dev.kxxcn.app_squad.data.model.message.Data;
import dev.kxxcn.app_squad.data.model.message.Send;
import dev.kxxcn.app_squad.util.Constants;
import dev.kxxcn.app_squad.util.DialogUtils;
import dev.kxxcn.app_squad.util.SystemUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static dev.kxxcn.app_squad.data.remote.APIPersistence.TYPE_CHATTING;
import static dev.kxxcn.app_squad.data.remote.APIPersistence.TYPE_REQUEST;
import static dev.kxxcn.app_squad.data.remote.APIPersistence.TYPE_RESPONSE;
import static dev.kxxcn.app_squad.util.Constants.DEFAULT_NAME;
import static dev.kxxcn.app_squad.util.Constants.TYPE_COLLECTION;

/**
 * Created by kxxcn on 2018-05-01.
 */

public class RemoteDataSource extends DataSource {

	private static final String COLLECTION_NAME_CHATTING = "chatting";

	public static final String COLLECTION_NAME_USER = "user";
	public static final String COLLECTION_NAME_MATCH = "match";
	public static final String COLLECTION_NAME_RECRUITMENT = "recruitment";
	public static final String COLLECTION_NAME_PLAYER = "player";
	public static final String COLLECTION_NAME_BATTLE = "battle";

	public static final String DOCUMENT_NAME_MESSAGE = "message";
	public static final String DOCUMENT_NAME_JOIN = "join";
	public static final String DOCUMENT_NAME_NOTICE = "notice";
	public static final String DOCUMENT_NAME_SQUAD = "squad";
	public static final String DOCUMENT_NAME_EVENT = "event";

	private static final String INIT = "1";

	public static final int FLAG_MATCH_LIST = 0;
	public static final int FLAG_RECRUITMENT_LIST = 1;
	public static final int FLAG_PLAYER_LIST = 2;

	private static RemoteDataSource remoteDataSource;

	private FirebaseAuth mAuth;
	private DatabaseReference mReference;

	private boolean mIsDuplicate = false;
	private boolean mIsExistRoom = false;

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
	public void onSignup(final GetSignupCallback callback, final String email, final String phone, final String password, final String team) {
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
				onCreateUser(callback, email, phone, password, team);
			}

			@Override
			public void onCancelled(@NonNull DatabaseError databaseError) {
				callback.onFailure(databaseError.toException());
			}
		});
	}

	private void onCreateUser(final GetSignupCallback callback, final String email, final String contact, final String password, final String team) {
		if (!mIsDuplicate) {
			mAuth.createUserWithEmailAndPassword(email, password)
					.addOnCompleteListener(new OnCompleteListener<AuthResult>() {
						@Override
						public void onComplete(@NonNull Task<AuthResult> task) {
							if (task.isSuccessful()) {
								String uid = mAuth.getCurrentUser().getUid();
								String token = FirebaseInstanceId.getInstance().getToken();
								User user = new User(email, contact, uid, team, token);
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
	public void onLoadList(final GetLoadListCallback callback, Constants.ListsFilterType requestType, final String region, final String date) {
		DatabaseReference reference = null;
		switch (requestType) {
			case MATCH_LIST:
				reference = FirebaseDatabase.getInstance().getReference(COLLECTION_NAME_MATCH);
				break;
			case RECRUITMENT_LIST:
				reference = FirebaseDatabase.getInstance().getReference(COLLECTION_NAME_RECRUITMENT);
				break;
			case PLAYER_LIST:
				reference = FirebaseDatabase.getInstance().getReference(COLLECTION_NAME_PLAYER);
				break;
		}

		reference.addValueEventListener(new ValueEventListener() {
			@Override
			public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
				List<Information> list = new ArrayList<>(0);
				for (DataSnapshot parentSnapshot : dataSnapshot.getChildren()) {
					for (DataSnapshot childSnapshot : parentSnapshot.getChildren()) {
						SystemUtils.Dlog.i(childSnapshot.getValue().toString());
						final Information information = childSnapshot.getValue(Information.class);
						if (!information.isConnect()) {
							if (Integer.parseInt(DialogUtils.getFormattedDate(DialogUtils.getDate(), TYPE_COLLECTION)) <=
									Integer.parseInt(information.getDate().replace("-", ""))) {
								if (region == null && date == null) {
									list.add(information);
								} else if (region != null && date != null) {
									if (region.equals(information.getRegion()) && date.equals(information.getDate())) {
										list.add(information);
									}
								} else if (region != null) {
									if (region.equals(information.getRegion())) {
										list.add(information);
									}
								} else if (date != null) {
									if (date.equals(information.getDate())) {
										list.add(information);
									}
								}
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
	}

	@Override
	public void onRegister(final GetCommonCallback callback, final Information information, final Constants.ListsFilterType requestType) {
		List<String> joinList = new ArrayList<>(0);
		joinList.add(DEFAULT_NAME);
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
				List<Battle> battleList = new ArrayList<>(0);
				for (DataSnapshot parentSnapshot : dataSnapshot.getChildren()) {
					for (DataSnapshot childSnapshot : parentSnapshot.getChildren()) {
						battleList.add(childSnapshot.getValue(Battle.class));
					}
				}
				callback.onSuccess(battleList);
			}

			@Override
			public void onCancelled(@NonNull DatabaseError databaseError) {
				callback.onFailure(databaseError.toException());
			}
		});
	}

	@Override
	public void onRequest(final GetSendMessageCallback callback, final String to, final String title, final String message, final String from, final String date, final Constants.ListsFilterType filterType) {
		DatabaseReference reference = FirebaseDatabase.getInstance().getReference(COLLECTION_NAME_USER);
		reference.addListenerForSingleValueEvent(new ValueEventListener() {
			@Override
			public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
				final String token = getTokenOfRrecipient(dataSnapshot, to, Constants.TYPE_REQUEST);

				if (token != null) {
					String flag = null;
					Data data = new Data();
					data.setTitle(title);
					data.setMessage(message);
					data.setSender(from);
					data.setDate(date);
					data.setType(TYPE_REQUEST);
					switch (filterType) {
						case MATCH_LIST:
							flag = String.valueOf(FLAG_MATCH_LIST);
							break;
						case RECRUITMENT_LIST:
							flag = String.valueOf(FLAG_RECRUITMENT_LIST);
							break;
						case PLAYER_LIST:
							flag = String.valueOf(FLAG_PLAYER_LIST);
							break;
					}
					data.setFlag(flag);

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
			accountReference.addListenerForSingleValueEvent(new ValueEventListener() {
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
	public void onRemove(final GetCommonCallback callback, Constants.ListsFilterType filterType, final String date) {
		DatabaseReference reference = null;
		String tmp = null;
		switch (filterType) {
			case MATCH_LIST:
				reference = FirebaseDatabase.getInstance().getReference(COLLECTION_NAME_MATCH).child(date).child(mAuth.getCurrentUser().getUid());
				tmp = String.valueOf(FLAG_MATCH_LIST);
				break;
			case RECRUITMENT_LIST:
				reference = FirebaseDatabase.getInstance().getReference(COLLECTION_NAME_RECRUITMENT).child(date).child(mAuth.getCurrentUser().getUid());
				tmp = String.valueOf(FLAG_RECRUITMENT_LIST);
				break;
			case PLAYER_LIST:
				reference = FirebaseDatabase.getInstance().getReference(COLLECTION_NAME_PLAYER).child(date).child(mAuth.getCurrentUser().getUid());
				tmp = String.valueOf(FLAG_PLAYER_LIST);
				break;
		}

		final String flag = tmp;
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
								if (notification.getFlag().equals(flag)) {
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
	public void onLoadMatch(final GetInformationCallback callback, boolean isHome, final String date, final String enemy, final String flag) {
		if (isHome) {
			DatabaseReference reference = null;
			if (flag != null) {
				switch (Integer.parseInt(flag)) {
					case FLAG_MATCH_LIST:
						reference = FirebaseDatabase.getInstance().getReference(COLLECTION_NAME_MATCH).child(date).child(mAuth.getCurrentUser().getUid());
						break;
					case FLAG_RECRUITMENT_LIST:
						reference = FirebaseDatabase.getInstance().getReference(COLLECTION_NAME_RECRUITMENT).child(date).child(mAuth.getCurrentUser().getUid());
						break;
					case FLAG_PLAYER_LIST:
						reference = FirebaseDatabase.getInstance().getReference(COLLECTION_NAME_PLAYER).child(date).child(mAuth.getCurrentUser().getUid());
						break;
				}
			} else {
				reference = FirebaseDatabase.getInstance().getReference(COLLECTION_NAME_MATCH).child(date).child(mAuth.getCurrentUser().getUid());
			}
			reference.addListenerForSingleValueEvent(new ValueEventListener() {
				@Override
				public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
					try {
						SystemUtils.Dlog.d(dataSnapshot.getValue().toString());
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
						if (enemy.equals(user.getTeam())) {
							String uid = user.getUid();
							DatabaseReference matchReference = null;
							if (flag != null) {
								switch (Integer.parseInt(flag)) {
									case FLAG_MATCH_LIST:
										matchReference = FirebaseDatabase.getInstance().getReference(COLLECTION_NAME_MATCH).child(date).child(uid);
										break;
									case FLAG_RECRUITMENT_LIST:
										matchReference = FirebaseDatabase.getInstance().getReference(COLLECTION_NAME_RECRUITMENT).child(date).child(uid);
										break;
									case FLAG_PLAYER_LIST:
										matchReference = FirebaseDatabase.getInstance().getReference(COLLECTION_NAME_PLAYER).child(date).child(uid);
										break;
								}
							} else {
								matchReference = FirebaseDatabase.getInstance().getReference(COLLECTION_NAME_MATCH).child(date).child(uid);
							}
							matchReference.addListenerForSingleValueEvent(new ValueEventListener() {
								@Override
								public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
									try {
										SystemUtils.Dlog.d(dataSnapshot.getValue().toString());
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
	public void onAgree(final GetSendMessageCallback callback, final Information information, final String title, final String message, final String flag) {
		information.setConnect(true);
		DatabaseReference reference = null;
		switch (Integer.parseInt(flag)) {
			case FLAG_MATCH_LIST:
				reference = FirebaseDatabase.getInstance().getReference(COLLECTION_NAME_MATCH)
						.child(DialogUtils.getFormattedDate(information.getDate(), TYPE_COLLECTION)).child(mAuth.getCurrentUser().getUid());
				break;
			case FLAG_RECRUITMENT_LIST:
				reference = FirebaseDatabase.getInstance().getReference(COLLECTION_NAME_RECRUITMENT)
						.child(DialogUtils.getFormattedDate(information.getDate(), TYPE_COLLECTION)).child(mAuth.getCurrentUser().getUid());
				break;
			case FLAG_PLAYER_LIST:
				reference = FirebaseDatabase.getInstance().getReference(COLLECTION_NAME_PLAYER)
						.child(DialogUtils.getFormattedDate(information.getDate(), TYPE_COLLECTION)).child(mAuth.getCurrentUser().getUid());
				break;
		}

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
							data.setFlag(flag);

							Send send = new Send();
							send.setTo(token);
							send.setData(data);

							Call<Void> call = service.sendMessage(send);
							call.enqueue(new Callback<Void>() {
								@Override
								public void onResponse(@NonNull final Call<Void> call, @NonNull Response<Void> response) {
									if (response.isSuccessful()) {
										DatabaseReference battleReference = FirebaseDatabase.getInstance().getReference(COLLECTION_NAME_BATTLE)
												.child(mAuth.getCurrentUser().getUid()).child(DialogUtils.getFormattedDate(information.getDate(), TYPE_COLLECTION)).child(flag);
										battleReference.setValue(new Battle(information.getEnemy(), DialogUtils.getFormattedDate(information.getDate(), TYPE_COLLECTION), information.getPlace(), true, flag))
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

	@Override
	public void onRemoveNotification(final GetCommonCallback callback) {
		DatabaseReference reference = FirebaseDatabase.getInstance().getReference(COLLECTION_NAME_USER).child(mAuth.getCurrentUser().getUid()).child(DOCUMENT_NAME_MESSAGE);
		reference.removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
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
	public void onUpdateToken(final GetCommonCallback callback, final String token) {
		try {
			final DatabaseReference reference = FirebaseDatabase.getInstance().getReference(COLLECTION_NAME_USER).child(FirebaseAuth.getInstance().getCurrentUser().getUid());
			reference.addListenerForSingleValueEvent(new ValueEventListener() {
				@Override
				public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
					User user = dataSnapshot.getValue(User.class);
					user.setToken(token);
					reference.setValue(user).addOnSuccessListener(new OnSuccessListener<Void>() {
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
	public void onSubscribe(final GetChattingCallback callback, String date, String roomName) {
		DatabaseReference reference = FirebaseDatabase.getInstance().getReference(COLLECTION_NAME_CHATTING).child(date).child(roomName);
		reference.addValueEventListener(new ValueEventListener() {
			@Override
			public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
				List<Chatting> chattingList = new ArrayList<>(0);
				for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
					chattingList.add(snapshot.getValue(Chatting.class));
				}
				callback.onSuccess(chattingList);
			}

			@Override
			public void onCancelled(@NonNull DatabaseError databaseError) {
				callback.onFailure(databaseError.toException());
			}
		});
	}

	@Override
	public void onChat(final GetCommonCallback callback, final Chatting chatting, final String title, final String date, final String roomName) {
		final String to = roomName.replace(chatting.getUid(), "");
		DatabaseReference sendReference = FirebaseDatabase.getInstance().getReference(COLLECTION_NAME_USER);
		sendReference.addListenerForSingleValueEvent(new ValueEventListener() {
			@Override
			public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
				final String token = getTokenOfRrecipient(dataSnapshot, to, Constants.TYPE_CHATTING);
				if (token != null) {
					Data data = new Data();
					data.setTitle(title);
					data.setMessage(roomName);
					data.setSender(chatting.getFrom());
					data.setDate(date);
					data.setType(TYPE_CHATTING);

					Send send = new Send();
					send.setTo(token);
					send.setData(data);

					Call<Void> call = service.sendMessage(send);
					call.enqueue(new Callback<Void>() {
						@Override
						public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
							if (response.isSuccessful()) {
								callback.onSuccess();
							}
						}

						@Override
						public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
							callback.onFailure(t);
						}
					});
				}
			}

			@Override
			public void onCancelled(@NonNull DatabaseError databaseError) {
				callback.onFailure(databaseError.toException());
			}
		});

		final DatabaseReference storeReference = FirebaseDatabase.getInstance().getReference(COLLECTION_NAME_CHATTING).child(date).child(roomName);
		storeReference.addListenerForSingleValueEvent(new ValueEventListener() {
			@Override
			public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
				String key = null;
				if (dataSnapshot.getChildrenCount() == 0) {
					key = INIT;
				} else {
					for (DataSnapshot parentSnapshot : dataSnapshot.getChildren()) {
						key = String.valueOf(Integer.parseInt(parentSnapshot.getKey()) + 1);
					}
				}
				chatting.setKey(Integer.parseInt(key));
				chatting.setCheck(false);
				storeReference.child(key).setValue(chatting).addOnSuccessListener(new OnSuccessListener<Void>() {
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
			public void onCancelled(@NonNull DatabaseError databaseError) {

			}
		});
	}

	@Override
	public void onSaveIntroduce(final GetCommonCallback callback, User user) {
		DatabaseReference reference = FirebaseDatabase.getInstance().getReference(COLLECTION_NAME_USER).child(mAuth.getCurrentUser().getUid());
		reference.setValue(user).addOnSuccessListener(new OnSuccessListener<Void>() {
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
	public void onUpdateReadMessages(final GetCommonCallback callback, List<Chatting> chattingList, String room, String date) {
		DatabaseReference reference = FirebaseDatabase.getInstance().getReference(COLLECTION_NAME_CHATTING).child(date).child(room);
		reference.setValue(chattingList).addOnSuccessListener(new OnSuccessListener<Void>() {
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
	public void onQuickMatch(final GetQuickListCallback callback, final String team, final String uid, final String region, final String date, final String rule) {
		DatabaseReference reference = FirebaseDatabase.getInstance().getReference(COLLECTION_NAME_MATCH).child(date.replace("-", ""));
		reference.addListenerForSingleValueEvent(new ValueEventListener() {
			@Override
			public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
				List<Information> list = new ArrayList<>(0);
				List<Information> quickList = new ArrayList<>(0);
				for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
					list.add(childSnapshot.getValue(Information.class));
				}

				for (int i = 0; i < list.size(); i++) {
					if (!list.get(i).isConnect() && !list.get(i).getTeam().equals(team)) {
						boolean isJoin = false;
						for (int j = 0; j < list.get(i).getJoin().size(); j++) {
							if (list.get(i).getJoin().get(j).equals(uid)) {
								isJoin = !isJoin;
							}
						}
						if (!isJoin) {
							if (list.get(i).getRegion().equals(region)) {
								if (rule != null) {
									if (list.get(i).getRule().equals(rule)) {
										quickList.add(list.get(i));
									}
								} else {
									quickList.add(list.get(i));
								}
							}
						}
					}
				}

				if (quickList.size() != 0) {
					int random = new Random().nextInt(quickList.size());
					callback.onSuccess(quickList.get(random));
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
				case Constants.TYPE_CHATTING:
					if (to.equals(userList.get(i).getUid())) {
						user = userList.get(i);
						break;
					}
					break;
			}
		}
		return user.getToken();
	}

}
