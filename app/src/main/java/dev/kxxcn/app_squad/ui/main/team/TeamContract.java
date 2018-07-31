package dev.kxxcn.app_squad.ui.main.team;

import java.util.List;

import dev.kxxcn.app_squad.data.model.Battle;
import dev.kxxcn.app_squad.data.model.Information;
import dev.kxxcn.app_squad.data.model.Notification;
import dev.kxxcn.app_squad.data.model.User;
import dev.kxxcn.app_squad.ui.BasePresenter;
import dev.kxxcn.app_squad.ui.BaseView;

/**
 * Created by kxxcn on 2018-06-08.
 */

public interface TeamContract {
	interface View extends BaseView<TeamContract.Presenter> {
		void setToolbarTitle(User user);

		void showSuccessfullyLogout();

		void showSuccessLoadNotification(List<Notification> list);

		void showFailureLoadNotification();

		void showSuccessfullyLoadInformation(Information information, String flag, int type);

		void showSuccessfullyLoadBattle(List<Battle> battleList);

		void showUnsuccessfullyLoadBattle();

		void showInvalidAccount();

		void showSuccessfullyRemoveNotification();

		void showUnsuccessfullyRemoveNotification();
	}

	interface Presenter extends BasePresenter {
		void onLoadRecord();

		void onLoadAccount();

		void onLoadNotification();

		void onReadNotification(List<Notification> notifications);

		void onLoadMatch(boolean isHome, String date, String enemy, String flag, int type);

		void onLogout();

		void onRemoveNotification();
	}

	interface ItemClickListener {
		void onClick(int position, int type);
	}
}
