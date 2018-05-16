package dev.kxxcn.app_squad.ui.main.setting;

import dev.kxxcn.app_squad.ui.BasePresenter;
import dev.kxxcn.app_squad.ui.BaseView;

/**
 * Created by kxxcn on 2018-05-09.
 */

public interface SettingContract {
	interface View extends BaseView<Presenter> {
		void showSuccessfullyLogout();
		void showUnsuccessfullyLogout();
	}

	interface Presenter extends BasePresenter {
		void onLogout();
	}
}
