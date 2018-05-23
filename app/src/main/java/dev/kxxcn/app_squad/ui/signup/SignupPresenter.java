package dev.kxxcn.app_squad.ui.signup;

import dev.kxxcn.app_squad.data.DataRepository;
import dev.kxxcn.app_squad.data.DataSource;
import dev.kxxcn.app_squad.util.Dlog;

/**
 * Created by kxxcn on 2018-05-04.
 */

public class SignupPresenter implements SignupContract.Presenter {

	private static final String ALREADY_USED = "The email address is already in use by another account.";
	private static final String BADLY_FORMATTED = "The email address is badly formatted.";

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

		mDataRepository.onSignup(new DataSource.GetSignupCallback() {
			@Override
			public void onSuccess() {
				mSignupView.showSuccessfullyRegister();
				mSignupView.showLoadingIndicator(false);
			}

			@Override
			public void onFailure(Throwable throwable) {
				Dlog.e(throwable.getMessage());
				if (throwable.getMessage().equals(ALREADY_USED)) {
					mSignupView.showAlreadyExistAccount();
				} else if (throwable.getMessage().equals(BADLY_FORMATTED)) {
					mSignupView.showBadlyFormatted();
				} else {
					mSignupView.showUnsuccessfullyRegister();
				}
				mSignupView.showLoadingIndicator(false);
			}

			@Override
			public void onDuplicatedTeam() {
				mSignupView.showAlreadyExistTeam();
				mSignupView.showLoadingIndicator(false);
			}
		}, email, password, team);
	}

}
