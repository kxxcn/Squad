package dev.kxxcn.app_squad.ui.login;

import android.app.Activity;

import dev.kxxcn.app_squad.util.PermissionUtils;

/**
 * Created by kxxcn on 2018-04-30.
 */

public class LoginPresenter implements LoginContract.Presenter {

	private LoginContract.View mLoginView;

	public LoginPresenter(LoginContract.View loginView) {
		mLoginView = loginView;
		mLoginView.setPresenter(this);
	}

	@Override
	public void setPermission(Activity activity, String... permission) {
		PermissionUtils.authorization(activity, permission);
	}

}
