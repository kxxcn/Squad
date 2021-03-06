package dev.kxxcn.app_squad.ui.main.list.matchlist;

import java.util.List;

import dev.kxxcn.app_squad.data.DataRepository;
import dev.kxxcn.app_squad.data.DataSource;
import dev.kxxcn.app_squad.data.model.Information;
import dev.kxxcn.app_squad.util.Constants;
import dev.kxxcn.app_squad.util.SystemUtils;


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
		mMatchListView.setPresenter(this);
	}

	@Override
	public void setFiltering(Constants.ListsFilterType requestType) {
		mCurrentFiltering = requestType;
	}

	@Override
	public void onLoadList(final String region, final String date) {
		if (mMatchListView == null && mCurrentFiltering == null) {
			return;
		}
		mDataRepository.onLoadList(new DataSource.GetLoadListCallback() {
			@Override
			public void onSuccess(List<Information> list) {
				mMatchListView.showMatchList(list);
			}

			@Override
			public void onFailure(Throwable throwable) {
				SystemUtils.Dlog.e(throwable.getMessage());
			}
			/*
			 * Sets the current task filtering type.
			 *
			 * @param ListsFilterType Can be
			 *                    {@link ListsFilterType#MATCH_LIST},
			 *                    {@link ListsFilterType#PLAYER_LIST}, or
			 *                    {@link ListsFilterType#RECRUITMENT_LIST}
			 */
		}, mCurrentFiltering, region, date);
	}

	@Override
	public void onRequest(String to, String title, String message, String from, String date, Constants.ListsFilterType filterType) {
		if (mMatchListView == null) {
			return;
		}
		mDataRepository.onRequest(new DataSource.GetSendMessageCallback() {
			@Override
			public void onSuccess() {
				mMatchListView.showSuccessfullyRequested();
			}

			@Override
			public void onFailure(Throwable throwable) {
				mMatchListView.showUnsuccessfullyRequested();
				SystemUtils.Dlog.e(throwable.getMessage());
			}

			@Override
			public void onError() {
				mMatchListView.showUnsuccessfullyRequested();
			}
		}, to, title, message, from, date, filterType);
	}

	@Override
	public void onLogout() {
		if (mMatchListView == null) {
			return;
		}

		mDataRepository.onLogout(new DataSource.GetCommonCallback() {
			@Override
			public void onSuccess() {
				mMatchListView.showSuccessfullyLogout();
			}

			@Override
			public void onFailure(Throwable throwable) {
				SystemUtils.Dlog.e(throwable.getMessage());
			}
		});
	}

}
