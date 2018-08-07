package dev.kxxcn.app_squad.ui.signup;

import android.app.Activity;

import dev.kxxcn.app_squad.data.DataRepository;
import dev.kxxcn.app_squad.data.DataSource;
import dev.kxxcn.app_squad.util.SystemUtils;


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
		mSignupView.setPresenter(this);
	}

	@Override
	public void signup(String email, String contact, String password, String team) {
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
				SystemUtils.Dlog.e(throwable.getMessage());
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
		}, email, contact, password, team);
	}

	@Override
	public void onAuth(Activity activity, String phoneNumber, String authCode) {
		if (mSignupView == null) {
			return;
		}

		mDataRepository.onAuth(new DataSource.GetAuthCallback() {
			@Override
			public void onSuccessfullyTransfer(String smsCode) {
				mSignupView.showSuccessfullyGetCode(smsCode);
			}

			@Override
			public void onSuccessfullyAuth() {
				mSignupView.showSuccessfulAuthentication();
			}

			@Override
			public void onUnsuccessfullyAuth() {
				mSignupView.showUnsuccessfulAuthentication();
			}

			@Override
			public void onFailure(Throwable throwable) {
				mSignupView.showUnsuccessfullyGetCode();
			}
		}, activity, phoneNumber, authCode);
	}

}
