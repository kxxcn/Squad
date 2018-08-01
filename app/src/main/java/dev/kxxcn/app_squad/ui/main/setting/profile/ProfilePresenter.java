package dev.kxxcn.app_squad.ui.main.setting.profile;

import dev.kxxcn.app_squad.data.DataRepository;
import dev.kxxcn.app_squad.data.DataSource;
import dev.kxxcn.app_squad.data.model.User;

/**
 * Created by kxxcn on 2018-08-01.
 */
public class ProfilePresenter implements ProfileContract.Presenter {

	private ProfileContract.View mProfileView;
	private DataRepository mDataRepository;

	public ProfilePresenter(ProfileContract.View profileView, DataRepository dataRepository) {
		this.mProfileView = profileView;
		this.mDataRepository = dataRepository;
		mProfileView.setPresenter(this);
	}

	@Override
	public void onLoadAccount() {
		if (mProfileView == null) {
			return;
		}

		mProfileView.showLoadingIndicator(true);

		mDataRepository.onLoadAccount(new DataSource.GetUserCallback() {
			@Override
			public void onSuccess(User user) {
				mProfileView.showLoadingIndicator(false);
				mProfileView.showSuccessfullyLoadAccount(user);
			}

			@Override
			public void onFailure(Throwable throwable) {
				mProfileView.showLoadingIndicator(false);
				mProfileView.showUnsuccessfullyLoadAccount();
			}
		});
	}

	@Override
	public void onSaveIntroduce(User user) {
		if (mProfileView == null) {
			return;
		}

		mDataRepository.onSaveIntroduce(new DataSource.GetCommonCallback() {
			@Override
			public void onSuccess() {
				mProfileView.showSuccessfullySaveIntroduce();
			}

			@Override
			public void onFailure(Throwable throwable) {
				mProfileView.showUnsuccessfullySaveIntroduce();
			}
		}, user);
	}

}
