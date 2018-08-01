package dev.kxxcn.app_squad.ui.main.setting.profile;

import dev.kxxcn.app_squad.data.model.User;
import dev.kxxcn.app_squad.ui.BasePresenter;
import dev.kxxcn.app_squad.ui.BaseView;

/**
 * Created by kxxcn on 2018-08-01.
 */
public interface ProfileContract {
	interface View extends BaseView<Presenter> {
		void showSuccessfullyLoadAccount(User user);

		void showUnsuccessfullyLoadAccount();

		void showSuccessfullySaveIntroduce();

		void showUnsuccessfullySaveIntroduce();
	}

	interface Presenter extends BasePresenter {
		void onLoadAccount();

		void onSaveIntroduce(User user);
	}
}
