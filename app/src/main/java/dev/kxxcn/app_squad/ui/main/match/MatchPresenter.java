package dev.kxxcn.app_squad.ui.main.match;

import dev.kxxcn.app_squad.data.DataRepository;
import dev.kxxcn.app_squad.data.DataSource;
import dev.kxxcn.app_squad.data.model.Information;
import dev.kxxcn.app_squad.util.Constants;
import dev.kxxcn.app_squad.util.Dlog;

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
				Dlog.e(throwable.getMessage());
				mMatchView.showUnsuccessfullyRegister();
				mMatchView.showLoadingIndicator(false);
			}
		}, information, requestType);
	}

}
