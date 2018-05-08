package dev.kxxcn.app_squad.data;

/**
 * Created by kxxcn on 2018-05-01.
 */

public abstract class DataSource {

	public interface GetSignupCallback {
		void onSuccess();

		void onFailure(Throwable throwable);
	}

	public interface GetLoginCallback {
		void onSuccess();

		void onFailure(Throwable throwable);
	}

	public abstract void onSignup(GetSignupCallback callback, String email, String password, String team);
	public abstract void onLogin(GetLoginCallback callback, String email, String password);

}
