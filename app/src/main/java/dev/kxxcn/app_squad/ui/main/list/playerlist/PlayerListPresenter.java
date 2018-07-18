package dev.kxxcn.app_squad.ui.main.list.playerlist;

import java.util.List;

import dev.kxxcn.app_squad.data.DataRepository;
import dev.kxxcn.app_squad.data.DataSource;
import dev.kxxcn.app_squad.data.model.Information;
import dev.kxxcn.app_squad.util.Constants;
import dev.kxxcn.app_squad.util.SystemUtils;

/**
 * Created by kxxcn on 2018-07-18.
 */

public class PlayerListPresenter implements PlayerListContract.Presenter {

	private PlayerListContract.View mPlayerView;
	private DataRepository mDataRepository;

	private Constants.ListsFilterType mCurrentFiltering;

	public PlayerListPresenter(PlayerListContract.View playerView, DataRepository dataRepository) {
		this.mPlayerView = playerView;
		this.mDataRepository = dataRepository;
		mPlayerView.setPresenter(this);
	}

	@Override
	public void setFiltering(Constants.ListsFilterType requestType) {
		mCurrentFiltering = requestType;
	}

	@Override
	public void onLoadList(String region, String date) {
		if (mPlayerView == null && mCurrentFiltering == null) {
			return;
		}

		mDataRepository.onLoadList(new DataSource.GetLoadListCallback() {
			@Override
			public void onSuccess(List<Information> list) {
				mPlayerView.showPlayerList(list);
			}

			@Override
			public void onFailure(Throwable throwable) {
				SystemUtils.Dlog.e(throwable.getMessage());
			}
		}, mCurrentFiltering, region, date);
	}

	@Override
	public void onRequest(String to, String title, String message, String from, String date, Constants.ListsFilterType filterType) {
		if (mPlayerView == null) {
			return;
		}

		mDataRepository.onRequest(new DataSource.GetSendMessageCallback() {
			@Override
			public void onSuccess() {
				mPlayerView.showSuccessfullyRequested();
			}

			@Override
			public void onFailure(Throwable throwable) {
				mPlayerView.showUnuccessfullyRequested();
				SystemUtils.Dlog.e(throwable.getMessage());
			}

			@Override
			public void onError() {
				mPlayerView.showUnuccessfullyRequested();
			}
		}, to, title, message, from, date, filterType);
	}

}