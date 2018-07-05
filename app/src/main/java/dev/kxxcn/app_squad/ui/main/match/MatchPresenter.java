package dev.kxxcn.app_squad.ui.main.match;

import dev.kxxcn.app_squad.data.DataRepository;
import dev.kxxcn.app_squad.data.DataSource;
import dev.kxxcn.app_squad.data.model.Information;
import dev.kxxcn.app_squad.data.model.User;
import dev.kxxcn.app_squad.util.Constants;
import dev.kxxcn.app_squad.util.SystemUtils;


/**
 * Created by kxxcn on 2018-04-30.
 */
public class MatchPresenter implements MatchContract.Presenter {

	private final MatchContract.View mMatchView;
	private DataRepository mDataRepository;

	public MatchPresenter(MatchContract.View matchView, DataRepository dataRepository) {
		this.mMatchView = matchView;
		this.mDataRepository = dataRepository;
		mMatchView.setPresenter(this);
	}

	@Override
	public void onRegister(Information information, Constants.ListsFilterType requestType) {
		if (mMatchView == null) {
			return;
		}

		mMatchView.showLoadingIndicator(true);
		mDataRepository.onRegister(new DataSource.GetCommonCallback() {
			@Override
			public void onSuccess() {
				mMatchView.showSuccessfullyRegister();
				mMatchView.showLoadingIndicator(false);
			}

			@Override
			public void onFailure(Throwable throwable) {
				SystemUtils.Dlog.e(throwable.getMessage());
				mMatchView.showUnsuccessfullyRegister();
				mMatchView.showLoadingIndicator(false);
			}
		}, information, requestType);
	}

	@Override
	public void onRemove(String date) {
		if (mMatchView == null) {
			return;
		}

		mMatchView.showLoadingIndicator(true);
		mDataRepository.onRemove(new DataSource.GetCommonCallback() {
			@Override
			public void onSuccess() {
				mMatchView.showSuccessfullyRemove();
				mMatchView.showLoadingIndicator(false);
			}

			@Override
			public void onFailure(Throwable throwable) {
				mMatchView.showUnsuccessfullyRemove();
				mMatchView.showLoadingIndicator(false);
			}
		}, date);
	}

	@Override
	public void onLoadAccount() {
		if (mMatchView == null) {
			return;
		}

		mDataRepository.onLoadAccount(new DataSource.GetUserCallback() {
			@Override
			public void onSuccess(User user) {
				mMatchView.showSuccessfullyLoadAccount(user);
			}

			@Override
			public void onFailure(Throwable throwable) {
				SystemUtils.Dlog.e(throwable.getMessage());
			}
		});
	}

}
