package dev.kxxcn.app_squad.ui.login;

import android.app.Activity;

import dev.kxxcn.app_squad.data.DataRepository;
import dev.kxxcn.app_squad.data.DataSource;
import dev.kxxcn.app_squad.util.Dlog;
import dev.kxxcn.app_squad.util.PermissionUtils;

/**
 * Created by kxxcn on 2018-04-30.
 */

public class LoginPresenter implements LoginContract.Presenter {

	private LoginContract.View mLoginView;
	private DataRepository mDataRepository;

	public LoginPresenter(LoginContract.View loginView, DataRepository dataRepository) {
		this.mLoginView = loginView;
		this.mDataRepository = dataRepository;
		this.mLoginView.setPresenter(this);
	}

	@Override
	public void setPermission(Activity activity, String... permission) {
		PermissionUtils.authorization(activity, permission);
	}

	@Override
	public void login(String email, String password) {
		if (mLoginView == null) {
			return;
		}

		mLoginView.showLoadingIndicator(true);

		mDataRepository.onLogin(new DataSource.GetLoginCallback() {
			@Override
			public void onSuccess() {
				mLoginView.showSuccessfullyAuth();
				mLoginView.showLoadingIndicator(false);
			}

			@Override
			public void onFailure(Throwable throwable) {
				Dlog.e("Exception : " + throwable.getMessage());
				mLoginView.showUnsuccessfullyAuth();
				mLoginView.showLoadingIndicator(false);
			}
		}, email, password);

	}

}
