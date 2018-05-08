package dev.kxxcn.app_squad.data;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kxxcn on 2018-05-01.
 */

public class DataRepository {

	private DataSource dataSource;

	private static DataRepository dataRepository;

	List<String> repo = new ArrayList<>(0);

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
		}, email, password, team);
	}


	public void onLogin(final DataSource.GetLoginCallback callback, String email, String password) {
		dataSource.onLogin(new DataSource.GetLoginCallback() {
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

}
