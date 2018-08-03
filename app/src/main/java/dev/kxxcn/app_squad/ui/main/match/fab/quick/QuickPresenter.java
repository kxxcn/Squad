package dev.kxxcn.app_squad.ui.main.match.fab.quick;

import dev.kxxcn.app_squad.data.DataRepository;
import dev.kxxcn.app_squad.data.DataSource;
import dev.kxxcn.app_squad.data.model.User;
import dev.kxxcn.app_squad.util.Constants;
import dev.kxxcn.app_squad.util.SystemUtils;

/**
 * Created by kxxcn on 2018-08-03.
 */
public class QuickPresenter implements QuickContract.Presenter {

	private QuickContract.View mQuickView;
	private DataRepository mDataRepository;

	public QuickPresenter(QuickContract.View quickView, DataRepository dataRepository) {
		this.mQuickView = quickView;
		this.mDataRepository = dataRepository;
		mQuickView.setPresenter(this);
	}

	@Override
	public void onLoadAccount() {
		if (mQuickView == null) {
			return;
		}

		mDataRepository.onLoadAccount(new DataSource.GetUserCallback() {
			@Override
			public void onSuccess(User user) {
				mQuickView.showSuccessfullyLoadAccount(user);
			}

			@Override
			public void onFailure(Throwable throwable) {
				SystemUtils.Dlog.e(throwable.getMessage());
			}
		});
	}

	@Override
	public void onLoadEnemyData(String enemy) {
		if (mQuickView == null) {
			return;
		}

		mDataRepository.onLoadEnemyData(new DataSource.GetUserCallback() {
			@Override
			public void onSuccess(User user) {
				mQuickView.showEnemyData(user);
			}

			@Override
			public void onFailure(Throwable throwable) {
				SystemUtils.Dlog.e(throwable.getMessage());
			}
		}, enemy);
	}

	@Override
	public void onRequest(String to, String title, String message, String from, String date, Constants.ListsFilterType filterType) {
		if (mQuickView == null) {
			return;
		}
		mDataRepository.onRequest(new DataSource.GetSendMessageCallback() {
			@Override
			public void onSuccess() {
				mQuickView.showSuccessfullyRequested();
			}

			@Override
			public void onFailure(Throwable throwable) {
				mQuickView.showUnsuccessfullyRequested();
				SystemUtils.Dlog.e(throwable.getMessage());
			}

			@Override
			public void onError() {
				mQuickView.showUnsuccessfullyRequested();
			}
		}, to, title, message, from, date, filterType);
	}
}
