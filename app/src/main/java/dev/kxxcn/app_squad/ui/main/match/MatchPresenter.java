package dev.kxxcn.app_squad.ui.main.match;

import dev.kxxcn.app_squad.data.DataRepository;
import dev.kxxcn.app_squad.data.DataSource;
import dev.kxxcn.app_squad.data.model.Information;

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
	public void onRegister(Information information) {
		mDataRepository.onRegister(new DataSource.GetCommonCallback() {
			@Override
			public void onSuccess() {

			}

			@Override
			public void onFailure(Throwable throwable) {

			}
		}, information);
	}

}
