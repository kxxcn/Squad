package dev.kxxcn.app_squad.ui.signup;

import dev.kxxcn.app_squad.data.DataRepository;
import dev.kxxcn.app_squad.data.DataSource;
import dev.kxxcn.app_squad.util.Dlog;

/**
 * Created by kxxcn on 2018-05-04.
 */

public class SignupPresenter implements SignupContract.Presenter {

	private static final String ALREADY_USED_EMAIL = "The email address is already in use by another account.";

	private SignupContract.View mSignupView;
	private DataRepository mDataRepository;

	public SignupPresenter(SignupContract.View signupView, DataRepository dataRepository) {
		this.mSignupView = signupView;
		this.mDataRepository = dataRepository;
		this.mSignupView.setPresenter(this);
	}

	@Override
	public void signup(String email, String password, String team) {
		if (mSignupView == null) {
			return;
		}

		mSignupView.showLoadingIndicator(true);

		mDataRepository.onSignup(new DataSource.GetCommonCallback() {
			@Override
			public void onSuccess() {
				mSignupView.showSuccessfullyRegister();
				mSignupView.showLoadingIndicator(false);
			}

			@Override
			public void onFailure(Throwable throwable) {
				Dlog.e("Exception : " + throwable.getMessage());
				if (throwable.getMessage().equals(ALREADY_USED_EMAIL)) {
					mSignupView.showAlreadyExistAccount();
				} else {
					mSignupView.showUnsuccessfullyRegister();
				}
				mSignupView.showLoadingIndicator(false);
			}
		}, email, password, team);
	}

}
