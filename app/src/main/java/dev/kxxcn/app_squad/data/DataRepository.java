package dev.kxxcn.app_squad.data;

import android.app.Activity;

import java.util.List;

import dev.kxxcn.app_squad.data.model.Battle;
import dev.kxxcn.app_squad.data.model.Chatting;
import dev.kxxcn.app_squad.data.model.Information;
import dev.kxxcn.app_squad.data.model.Notification;
import dev.kxxcn.app_squad.data.model.User;
import dev.kxxcn.app_squad.util.Constants;

/**
 * Created by kxxcn on 2018-05-01.
 */

public class DataRepository {

	private DataSource dataSource;

	private static DataRepository dataRepository;

	public DataRepository(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	public static synchronized DataRepository getInstance(DataSource dataSource) {
		if (dataRepository == null) {
			dataRepository = new DataRepository(dataSource);
		}
		return dataRepository;
	}

	public void onSignup(final DataSource.GetSignupCallback callback, String email, String contact, String password, String team) {
		dataSource.onSignup(new DataSource.GetSignupCallback() {
			@Override
			public void onSuccess() {
				callback.onSuccess();
			}

			@Override
			public void onFailure(Throwable throwable) {
				callback.onFailure(throwable);
			}

			@Override
			public void onDuplicatedTeam() {
				callback.onDuplicatedTeam();
			}
		}, email, contact, password, team);
	}


	public void onLogin(final DataSource.GetCommonCallback callback, String email, String password) {
		dataSource.onLogin(new DataSource.GetCommonCallback() {
			@Override
			public void onSuccess() {
				callback.onSuccess();
			}

			@Override
			public void onFailure(Throwable throwable) {
				callback.onFailure(throwable);
			}
		}, email, password);
	}

	public void onLogout(final DataSource.GetCommonCallback callback) {
		dataSource.onLogout(new DataSource.GetCommonCallback() {
			@Override
			public void onSuccess() {
				callback.onSuccess();
			}

			@Override
			public void onFailure(Throwable throwable) {
				callback.onFailure(throwable);
			}
		});
	}

	public void onLoadList(final DataSource.GetLoadListCallback callback, Constants.ListsFilterType requestType, String region, String date) {
		dataSource.onLoadList(new DataSource.GetLoadListCallback() {

			@Override
			public void onSuccess(List<Information> list) {
				callback.onSuccess(list);
			}

			@Override
			public void onFailure(Throwable throwable) {
				callback.onFailure(throwable);
			}
		}, requestType, region, date);
	}

	public void onRegister(final DataSource.GetCommonCallback callback, Information information, Constants.ListsFilterType requestType) {
		dataSource.onRegister(new DataSource.GetCommonCallback() {
			@Override
			public void onSuccess() {
				callback.onSuccess();
			}

			@Override
			public void onFailure(Throwable throwable) {
				callback.onFailure(throwable);
			}
		}, information, requestType);
	}

	public void onLoadRecord(final DataSource.GetBattleCallback callback) {
		dataSource.onLoadRecord(new DataSource.GetBattleCallback() {
			@Override
			public void onSuccess(List<Battle> battleList) {
				callback.onSuccess(battleList);
			}

			@Override
			public void onFailure(Throwable throwable) {
				callback.onFailure(throwable);
			}
		});
	}

	public void onRequest(final DataSource.GetSendMessageCallback callback, String to, String title, String message, String from, String date, Constants.ListsFilterType filterType) {
		dataSource.onRequest(new DataSource.GetSendMessageCallback() {
			@Override
			public void onSuccess() {
				callback.onSuccess();
			}

			@Override
			public void onFailure(Throwable throwable) {
				callback.onFailure(throwable);
			}

			@Override
			public void onError() {

			}
		}, to, title, message, from, date, filterType);
	}

	public void onLoadAccount(final DataSource.GetUserCallback callback) {
		dataSource.onLoadAccount(new DataSource.GetUserCallback() {
			@Override
			public void onSuccess(User user) {
				callback.onSuccess(user);
			}

			@Override
			public void onFailure(Throwable throwable) {
				callback.onFailure(throwable);
			}
		});
	}

	public void onLoadNotification(final DataSource.GetNotificationCallback callback) {
		dataSource.onLoadNotification(new DataSource.GetNotificationCallback() {
			@Override
			public void onSuccess(List<Notification> list) {
				callback.onSuccess(list);
			}

			@Override
			public void onFailure(Throwable throwable) {
				callback.onFailure(throwable);
			}
		});
	}

	public void onReadNotification(final DataSource.GetCommonCallback callback, List<Notification> notifications) {
		dataSource.onReadNotification(new DataSource.GetCommonCallback() {
			@Override
			public void onSuccess() {
				callback.onSuccess();
			}

			@Override
			public void onFailure(Throwable throwable) {
				callback.onFailure(throwable);
			}
		}, notifications);
	}

	public void onRemove(final DataSource.GetCommonCallback callback, Constants.ListsFilterType filterType, String date) {
		dataSource.onRemove(new DataSource.GetCommonCallback() {
			@Override
			public void onSuccess() {
				callback.onSuccess();
			}

			@Override
			public void onFailure(Throwable throwable) {
				callback.onFailure(throwable);
			}
		}, filterType, date);
	}

	public void onLoadMatch(final DataSource.GetInformationCallback callback, boolean isHome, String date, String enemy, String flag) {
		dataSource.onLoadMatch(new DataSource.GetInformationCallback() {
			@Override
			public void onSuccess(Information information) {
				callback.onSuccess(information);
			}

			@Override
			public void onFailure(Throwable throwable) {
				callback.onFailure(throwable);
			}
		}, isHome, date, enemy, flag);
	}

	public void onLoadEnemyData(final DataSource.GetUserCallback callback, String enemy) {
		dataSource.onLoadEnemyData(new DataSource.GetUserCallback() {
			@Override
			public void onSuccess(User user) {
				callback.onSuccess(user);
			}

			@Override
			public void onFailure(Throwable throwable) {
				callback.onFailure(throwable);
			}
		}, enemy);

	}

	public void onAgree(final DataSource.GetSendMessageCallback callback, Information information, String title, String message, String flag) {
		dataSource.onAgree(new DataSource.GetSendMessageCallback() {
			@Override
			public void onSuccess() {
				callback.onSuccess();
			}

			@Override
			public void onFailure(Throwable throwable) {
				callback.onFailure(throwable);
			}

			@Override
			public void onError() {
				callback.onError();
			}
		}, information, title, message, flag);
	}

	public void onUpdateNotice(final DataSource.GetCommonCallback callback, boolean on, Constants.NoticeFilterType requestType) {
		dataSource.onUpdateNotice(new DataSource.GetCommonCallback() {
			@Override
			public void onSuccess() {
				callback.onSuccess();
			}

			@Override
			public void onFailure(Throwable throwable) {
				callback.onFailure(throwable);
			}
		}, on, requestType);
	}

	public void onRemoveNotification(final DataSource.GetCommonCallback callback) {
		dataSource.onRemoveNotification(new DataSource.GetCommonCallback() {
			@Override
			public void onSuccess() {
				callback.onSuccess();
			}

			@Override
			public void onFailure(Throwable throwable) {
				callback.onFailure(throwable);
			}
		});
	}

	public void onUpdateToken(final DataSource.GetCommonCallback callback, String token) {
		dataSource.onUpdateToken(new DataSource.GetCommonCallback() {
			@Override
			public void onSuccess() {
				callback.onSuccess();
			}

			@Override
			public void onFailure(Throwable throwable) {
				callback.onFailure(throwable);
			}
		}, token);
	}

	public void onChat(final DataSource.GetCommonCallback callback, Chatting chatting, String title, String date, String roomName) {
		dataSource.onChat(new DataSource.GetCommonCallback() {
			@Override
			public void onSuccess() {
				callback.onSuccess();
			}

			@Override
			public void onFailure(Throwable throwable) {
				callback.onFailure(throwable);
			}
		}, chatting, title, date, roomName);
	}

	public void onSubscribe(final DataSource.GetChattingCallback callback, String date, String roomName) {
		dataSource.onSubscribe(new DataSource.GetChattingCallback() {
			@Override
			public void onSuccess(List<Chatting> chattingList) {
				callback.onSuccess(chattingList);
			}

			@Override
			public void onFailure(Throwable throwable) {
				callback.onFailure(throwable);
			}
		}, date, roomName);
	}

	public void onSaveIntroduce(final DataSource.GetCommonCallback callback, User user) {
		dataSource.onSaveIntroduce(new DataSource.GetCommonCallback() {
			@Override
			public void onSuccess() {
				callback.onSuccess();
			}

			@Override
			public void onFailure(Throwable throwable) {
				callback.onFailure(throwable);
			}
		}, user);
	}

	public void onUpdateReadMessages(final DataSource.GetCommonCallback callback, List<Chatting> chattingList, String room, String date) {
		dataSource.onUpdateReadMessages(new DataSource.GetCommonCallback() {
			@Override
			public void onSuccess() {
				callback.onSuccess();
			}

			@Override
			public void onFailure(Throwable throwable) {
				callback.onFailure(throwable);
			}
		}, chattingList, room, date);
	}

	public void onQuickMatch(final DataSource.GetQuickListCallback callback, String team, String uid, String region, String date, String rule) {
		dataSource.onQuickMatch(new DataSource.GetQuickListCallback() {
			@Override
			public void onSuccess(Information information) {
				callback.onSuccess(information);
			}

			@Override
			public void onFailure(Throwable throwable) {
				callback.onFailure(throwable);
			}

			@Override
			public void onError() {
				callback.onError();
			}
		}, team, uid, region, date, rule);
	}

	public void onAuth(final DataSource.GetAuthCallback callback, Activity activity, String phoneNumber, String authCode) {
		dataSource.onAuth(new DataSource.GetAuthCallback() {
			@Override
			public void onSuccessfullyTransfer(String smsCode) {
				callback.onSuccessfullyTransfer(smsCode);
			}

			@Override
			public void onSuccessfullyAuth() {
				callback.onSuccessfullyAuth();
			}

			@Override
			public void onUnsuccessfullyAuth() {
				callback.onUnsuccessfullyAuth();
			}

			@Override
			public void onFailure(Throwable throwable) {
				callback.onFailure(throwable);
			}
		}, activity, phoneNumber, authCode);
	}

}
