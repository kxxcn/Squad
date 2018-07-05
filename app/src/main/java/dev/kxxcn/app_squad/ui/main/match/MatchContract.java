package dev.kxxcn.app_squad.ui.main.match;

import dev.kxxcn.app_squad.data.model.Information;
import dev.kxxcn.app_squad.data.model.User;
import dev.kxxcn.app_squad.ui.BasePresenter;
import dev.kxxcn.app_squad.ui.BaseView;
import dev.kxxcn.app_squad.util.Constants;

/**
 * Created by kxxcn on 2018-04-30.
 */

public interface MatchContract {
	interface View extends BaseView<MatchContract.Presenter> {
		void showSuccessfullyRegister();

		void showUnsuccessfullyRegister();

		void showSuccessfullyRemove();

		void showUnsuccessfullyRemove();

		void showSuccessfullyLoadAccount(User user);
	}

	interface Presenter extends BasePresenter {
		void onRegister(Information information, Constants.ListsFilterType requestType);

		void onRemove(String date);

		void onLoadAccount();
	}
}
