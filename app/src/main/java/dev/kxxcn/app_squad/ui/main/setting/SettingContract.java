package dev.kxxcn.app_squad.ui.main.setting;

import dev.kxxcn.app_squad.data.model.User;
import dev.kxxcn.app_squad.ui.BasePresenter;
import dev.kxxcn.app_squad.ui.BaseView;
import dev.kxxcn.app_squad.util.Constants;

/**
 * Created by kxxcn on 2018-05-09.
 */

public interface SettingContract {
	interface View extends BaseView<Presenter> {
		void showSuccessfullyLogout();

		void showUnsuccessfullyLogout();

		void setToggleButton(User user);

		void showInvalidAccount();

		void showSuccessfullyUpdateToken();

		void showUnsuccessfullyUpdateToken();

		void showSuccessfulyCheckVersion(String latestVersion);

		void showUnsuccessfulyCheckVersion();
	}

	interface Presenter extends BasePresenter {
		void onLogout();

		void onLoadAccount();

		void onUpdateNotice(boolean on, Constants.NoticeFilterType type);

		void onUpdateToken(String token);

		void onCheckVersion(String packageName);
	}
}
