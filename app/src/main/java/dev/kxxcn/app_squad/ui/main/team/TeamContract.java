package dev.kxxcn.app_squad.ui.main.team;

import java.util.List;

import dev.kxxcn.app_squad.data.model.Notification;
import dev.kxxcn.app_squad.ui.BasePresenter;
import dev.kxxcn.app_squad.ui.BaseView;

/**
 * Created by kxxcn on 2018-06-08.
 */

public interface TeamContract {
	interface View extends BaseView<TeamContract.Presenter> {
		void setToolbarTitle(String title);

		void showErrorBadRequest();

		void showSuccessLoadNotification(List<Notification> list);

		void showFailureLoadNotification();
	}

	interface Presenter extends BasePresenter {
		void onLoadRecord();

		void onLoadAccount();

		void onLoadNotification();

		void onReadNotification(List<Notification> notifications);
	}
}
