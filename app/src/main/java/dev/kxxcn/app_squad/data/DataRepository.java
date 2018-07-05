package dev.kxxcn.app_squad.data;

import java.util.List;

import dev.kxxcn.app_squad.data.model.Battle;
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

	public void onSignup(final DataSource.GetSignupCallback callback, String email, String password, String team) {
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
		}, email, password, team);
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

	public void onLoadList(final DataSource.GetLoadListCallback callback, Constants.ListsFilterType requestType) {
		dataSource.onLoadList(new DataSource.GetLoadListCallback() {

			@Override
			public void onSuccess(List<Information> list) {
				callback.onSuccess(list);
			}

			@Override
			public void onFailure(Throwable throwable) {
				callback.onFailure(throwable);
			}
		}, requestType);
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

	public void onRequest(final DataSource.GetSendMessageCallback callback, String to, String title, String message, String from, String date) {
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
		}, to, title, message, from, date);
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

	public void onRemove(final DataSource.GetCommonCallback callback, String date) {
		dataSource.onRemove(new DataSource.GetCommonCallback() {
			@Override
			public void onSuccess() {
				callback.onSuccess();
			}

			@Override
			public void onFailure(Throwable throwable) {
				callback.onFailure(throwable);
			}
		}, date);
	}

	public void onLoadMatch(final DataSource.GetInformationCallback callback, String date) {
		dataSource.onLoadMatch(new DataSource.GetInformationCallback() {
			@Override
			public void onSuccess(Information information) {
				callback.onSuccess(information);
			}

			@Override
			public void onFailure(Throwable throwable) {
				callback.onFailure(throwable);
			}
		}, date);
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

	public void onAgree(final DataSource.GetSendMessageCallback callback, Information information, String title, String message) {
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
		}, information, title, message);
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

}
