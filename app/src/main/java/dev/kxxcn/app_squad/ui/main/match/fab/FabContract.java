package dev.kxxcn.app_squad.ui.main.match.fab;

import dev.kxxcn.app_squad.data.model.Information;
import dev.kxxcn.app_squad.data.model.User;
import dev.kxxcn.app_squad.ui.BasePresenter;
import dev.kxxcn.app_squad.ui.BaseView;

/**
 * Created by kxxcn on 2018-08-03.
 */
public interface FabContract {
	interface View extends BaseView<Presenter> {
		void isUsableComponent(boolean isUsable);

		void showResults(Information information);

		void showFailedToLoad();

		void noResults();

		void showSuccessfullyLoadAccount(User user);
	}

	interface Presenter extends BasePresenter {
		void onLoadAccount();

		void onQuickMatch(String team, String uid, String region, String date, String rule);
	}

	interface OnDialogDismissed {
		void onDialogDismissed();
	}

}
