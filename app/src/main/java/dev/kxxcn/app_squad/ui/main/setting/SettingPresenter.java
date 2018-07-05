package dev.kxxcn.app_squad.ui.main.setting;

import dev.kxxcn.app_squad.data.DataRepository;
import dev.kxxcn.app_squad.data.DataSource;
import dev.kxxcn.app_squad.data.model.User;
import dev.kxxcn.app_squad.util.Constants;
import dev.kxxcn.app_squad.util.SystemUtils;

/**
 * Created by kxxcn on 2018-04-30.
 */

public class SettingPresenter implements SettingContract.Presenter {

	private SettingContract.View mSettingView;
	private DataRepository mDataRepository;

	public SettingPresenter(SettingContract.View settingView, DataRepository dataRepository) {
		this.mSettingView = settingView;
		this.mDataRepository = dataRepository;
		mSettingView.setPresenter(this);
	}

	@Override
	public void onLogout() {
		if (mSettingView == null) {
			return;
		}

		mDataRepository.onLogout(new DataSource.GetCommonCallback() {
			@Override
			public void onSuccess() {
				mSettingView.showSuccessfullyLogout();
			}

			@Override
			public void onFailure(Throwable throwable) {
				SystemUtils.Dlog.e(throwable.getMessage());
				mSettingView.showUnsuccessfullyLogout();
			}
		});
	}

	@Override
	public void onLoadAccount() {
		if (mSettingView == null) {
			return;
		}

		mDataRepository.onLoadAccount(new DataSource.GetUserCallback() {
			@Override
			public void onSuccess(User user) {
				mSettingView.setToggleButton(user);
			}

			@Override
			public void onFailure(Throwable throwable) {
				mSettingView.showInvalidAccount();
				SystemUtils.Dlog.e(throwable.getMessage());
			}
		});
	}

	@Override
	public void onUpdateNotice(boolean on, Constants.NoticeFilterType type) {
		if (mSettingView == null) {
			return;
		}

		mDataRepository.onUpdateNotice(new DataSource.GetCommonCallback() {
			@Override
			public void onSuccess() {

			}

			@Override
			public void onFailure(Throwable throwable) {

			}
		}, on, type);
	}

}