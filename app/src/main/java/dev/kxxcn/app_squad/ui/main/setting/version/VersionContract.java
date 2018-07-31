package dev.kxxcn.app_squad.ui.main.setting.version;

import dev.kxxcn.app_squad.ui.BasePresenter;
import dev.kxxcn.app_squad.ui.BaseView;

/**
 * Created by kxxcn on 2018-07-31.
 */
public interface VersionContract {
	interface View extends BaseView<Presenter> {
		void showSuccessfulyCheckVersion(String latestVersion);
		void showUnsuccessfulyCheckVersion();
	}

	interface Presenter extends BasePresenter {
		void onCheckVersion(String packageName);
	}
}
