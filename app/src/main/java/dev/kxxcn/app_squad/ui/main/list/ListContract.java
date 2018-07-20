package dev.kxxcn.app_squad.ui.main.list;

import dev.kxxcn.app_squad.data.model.User;
import dev.kxxcn.app_squad.ui.BasePresenter;
import dev.kxxcn.app_squad.ui.BaseView;

/**
 * Created by kxxcn on 2018-07-17.
 */

public interface ListContract {
	interface View extends BaseView<Presenter> {
		void showSuccessfullyLoadAccount(User user);
	}

	interface Presenter extends BasePresenter {
		void onLoadAccount();
	}

	interface OnDialogDismissed {
		void onDialogDismissed(String region);
	}

	interface OnDialogRequested{
		void onDialogRequested(String place, String date, String time, String money);
	}
}
