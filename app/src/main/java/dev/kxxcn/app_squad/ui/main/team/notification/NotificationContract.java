package dev.kxxcn.app_squad.ui.main.team.notification;

import dev.kxxcn.app_squad.data.model.Information;
import dev.kxxcn.app_squad.data.model.User;
import dev.kxxcn.app_squad.ui.BasePresenter;
import dev.kxxcn.app_squad.ui.BaseView;

/**
 * Created by kxxcn on 2018-06-22.
 */

public class NotificationContract {
	interface View extends BaseView<NotificationContract.Presenter> {
		void showEnemyData(User user);

		void showSuccessfullyAgree();

		void showUnsuccessfullyAgree();
	}

	interface Presenter extends BasePresenter {
		void onAgree(Information information, String title, String message);

		void onLoadEnemyData(String enemy);
	}
}
