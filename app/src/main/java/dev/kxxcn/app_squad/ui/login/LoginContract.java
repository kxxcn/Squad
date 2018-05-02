package dev.kxxcn.app_squad.ui.login;

import android.app.Activity;

import dev.kxxcn.app_squad.ui.BasePresenter;
import dev.kxxcn.app_squad.ui.BaseView;

/**
 * Created by kxxcn on 2018-04-30.
 */

public class LoginContract {
	interface View extends BaseView<LoginContract.Presenter> {

	}

	interface Presenter extends BasePresenter {
		void setPermission(Activity activity, String... permission);
	}
}
