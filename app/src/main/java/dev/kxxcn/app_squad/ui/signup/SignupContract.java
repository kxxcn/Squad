package dev.kxxcn.app_squad.ui.signup;

import dev.kxxcn.app_squad.ui.BasePresenter;
import dev.kxxcn.app_squad.ui.BaseView;

/**
 * Created by kxxcn on 2018-05-04.
 */

public interface SignupContract {
	interface View extends BaseView<SignupContract.Presenter> {
		void showSuccessfullyRegister();

		void showUnsuccessfullyRegister();

		void showAlreadyExistAccount();

		void showAlreadyExistTeam();

		void showBadlyFormatted();
	}

	interface Presenter extends BasePresenter {
		void signup(String email, String password, String team);
	}
}
