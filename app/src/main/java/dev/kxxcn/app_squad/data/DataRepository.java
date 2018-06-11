package dev.kxxcn.app_squad.data;

import java.util.List;

import dev.kxxcn.app_squad.data.model.Information;
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

	public void onLoadRecord(final DataSource.GetLoadRecordCallback callback) {
		dataSource.onLoadRecord(new DataSource.GetLoadRecordCallback() {
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

	public void onSendMessage(final DataSource.GetSendMessageCallback callback, String to, String title, String message) {
		dataSource.onSendMessage(new DataSource.GetSendMessageCallback() {
			@Override
			public void onSuccess() {
				callback.onSuccess();
			}

			@Override
			public void onFailure(Throwable throwable) {
				callback.onFailure(throwable);
			}

			@Override
			public void onErrorNoData() {

			}
		}, to, title, message);
	}

	public void onLoadAccount(final DataSource.GetCommonCallback callback) {
		dataSource.onLoadAccount(new DataSource.GetCommonCallback() {
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

}
