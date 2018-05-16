package dev.kxxcn.app_squad.data;

import dev.kxxcn.app_squad.util.Constants;

/**
 * Created by kxxcn on 2018-05-01.
 */

public abstract class DataSource {

	public interface GetCommonCallback {
		void onSuccess();

		void onFailure(Throwable throwable);
	}

	public interface GetLoadCallback {
		void onSuccess();

		void onFailure(Throwable throwable);
	}

	public abstract void onSignup(GetCommonCallback callback, String email, String password, String team);

	public abstract void onLogin(GetCommonCallback callback, String email, String password);

	public abstract void onLogout(GetCommonCallback callback);

	public abstract void onLoad(GetLoadCallback callback, Constants.ListsFilterType requestType);

}
