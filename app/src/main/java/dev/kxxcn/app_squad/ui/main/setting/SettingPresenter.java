package dev.kxxcn.app_squad.ui.main.setting;

import dev.kxxcn.app_squad.data.DataRepository;
import dev.kxxcn.app_squad.data.DataSource;
import dev.kxxcn.app_squad.util.Dlog;

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
				Dlog.e(throwable.getMessage());
				mSettingView.showUnsuccessfullyLogout();
			}
		});
	}

}