package dev.kxxcn.app_squad.ui.main.list.matchlist;

import dev.kxxcn.app_squad.data.DataRepository;
import dev.kxxcn.app_squad.data.DataSource;
import dev.kxxcn.app_squad.util.Constants;

/**
 * Created by kxxcn on 2018-05-09.
 */

public class MatchListPresenter implements MatchListContract.Presenter {

	private MatchListContract.View mMatchListView;
	private DataRepository mDataRepository;

	private Constants.ListsFilterType mCurrentFiltering;

	public MatchListPresenter(MatchListContract.View matchListView, DataRepository dataRepository) {
		this.mMatchListView = matchListView;
		this.mDataRepository = dataRepository;
		this.mMatchListView.setPresenter(this);
	}

	@Override
	public void setFiltering(Constants.ListsFilterType requestType) {
		mCurrentFiltering = requestType;
	}

	@Override
	public void onLoad() {
		if (mMatchListView == null && mCurrentFiltering == null) {
			return;
		}
		mDataRepository.onLoad(new DataSource.GetLoadCallback() {
			@Override
			public void onSuccess() {
				mMatchListView.showMatchList();
			}

			@Override
			public void onFailure(Throwable throwable) {

			}
			/*
			 * Sets the current task filtering type.
			 *
			 * @param ListsFilterType Can be
			 *                    {@link ListsFilterType#MATCH_LIST},
			 *                    {@link ListsFilterType#PLAYER_LIST}, or
			 *                    {@link ListsFilterType#RECRUITMENT_LIST}
			 */
		}, mCurrentFiltering);
	}

}
