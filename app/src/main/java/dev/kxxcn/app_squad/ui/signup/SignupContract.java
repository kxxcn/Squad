package dev.kxxcn.app_squad.ui.signup;

import android.app.Activity;

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

		void showSuccessfullyGetCode(String smsCode);

		void showUnsuccessfullyGetCode();

		void showSuccessfullyAuth();

		void showUnsuccessfullyAuth();
	}

	interface Presenter extends BasePresenter {
		void signup(String email, String contact, String password, String team);

		void onAuth(Activity activity, String phoneNumber, String authCode);
	}
}
