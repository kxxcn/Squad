package dev.kxxcn.app_squad.ui.main.match.fab;

import dev.kxxcn.app_squad.data.DataRepository;
import dev.kxxcn.app_squad.data.DataSource;
import dev.kxxcn.app_squad.data.model.Information;
import dev.kxxcn.app_squad.data.model.User;
import dev.kxxcn.app_squad.util.SystemUtils;
import dev.kxxcn.app_squad.util.threading.UiThread;

import static dev.kxxcn.app_squad.util.Constants.SEARCHING;

/**
 * Created by kxxcn on 2018-08-03.
 */
public class FabPresenter implements FabContract.Presenter {

	private FabContract.View mFabView;
	private DataRepository mDataRepository;

	public FabPresenter(FabContract.View fabView, DataRepository dataRepository) {
		this.mFabView = fabView;
		this.mDataRepository = dataRepository;
		mFabView.setPresenter(this);
	}

	@Override
	public void onLoadAccount() {
		if (mFabView == null) {
			return;
		}

		mDataRepository.onLoadAccount(new DataSource.GetUserCallback() {
			@Override
			public void onSuccess(User user) {
				mFabView.showSuccessfullyLoadAccount(user);
			}

			@Override
			public void onFailure(Throwable throwable) {
				SystemUtils.Dlog.e(throwable.getMessage());
			}
		});
	}

	@Override
	public void onQuickMatch(String team, String uid, String region, String date, String rule) {
		if (mFabView == null) {
			return;
		}

		mFabView.showLoadingIndicator(true);
		mFabView.isUsableComponent(false);

		mDataRepository.onQuickMatch(new DataSource.GetQuickListCallback() {
			@Override
			public void onSuccess(final Information information) {
				UiThread.getInstance().postDelayed(new Runnable() {
					@Override
					public void run() {
						mFabView.showLoadingIndicator(false);
						mFabView.isUsableComponent(true);
						mFabView.showResults(information);
					}
				}, SEARCHING);
			}

			@Override
			public void onFailure(Throwable throwable) {
				UiThread.getInstance().postDelayed(new Runnable() {
					@Override
					public void run() {
						mFabView.showLoadingIndicator(false);
						mFabView.isUsableComponent(true);
						mFabView.showFailedToLoad();
					}
				},SEARCHING);
			}

			@Override
			public void onError() {
				UiThread.getInstance().postDelayed(new Runnable() {
					@Override
					public void run() {
						mFabView.showLoadingIndicator(false);
						mFabView.isUsableComponent(true);
						mFabView.noResults();
					}
				},SEARCHING);
			}
		}, team, uid, region, date, rule);
	}

}
