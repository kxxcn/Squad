package dev.kxxcn.app_squad.ui.main.match.fab.quick;

import dev.kxxcn.app_squad.data.model.User;
import dev.kxxcn.app_squad.ui.BasePresenter;
import dev.kxxcn.app_squad.ui.BaseView;
import dev.kxxcn.app_squad.util.Constants;

/**
 * Created by kxxcn on 2018-08-03.
 */
public interface QuickContract {
	interface View extends BaseView<Presenter> {
		void showSuccessfullyLoadAccount(User user);

		void showEnemyData(User user);

		void showSuccessfullyRequested();

		void showUnsuccessfullyRequested();
	}

	interface Presenter extends BasePresenter {
		void onLoadAccount();

		void onLoadEnemyData(String enemy);

		void onRequest(String to, String title, String message, String from, String date, Constants.ListsFilterType filterType);
	}
}
