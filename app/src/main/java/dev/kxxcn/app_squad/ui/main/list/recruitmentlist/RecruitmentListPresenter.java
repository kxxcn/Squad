package dev.kxxcn.app_squad.ui.main.list.recruitmentlist;

import java.util.List;

import dev.kxxcn.app_squad.data.DataRepository;
import dev.kxxcn.app_squad.data.DataSource;
import dev.kxxcn.app_squad.data.model.Information;
import dev.kxxcn.app_squad.util.Constants;
import dev.kxxcn.app_squad.util.SystemUtils;

/**
 * Created by kxxcn on 2018-07-18.
 */

public class RecruitmentListPresenter implements RecruitmentListContract.Presenter {

	private RecruitmentListContract.View mRecruitmentView;
	private DataRepository mDataRepository;

	private Constants.ListsFilterType mCurrentFiltering;

	public RecruitmentListPresenter(RecruitmentListContract.View recruitmentView, DataRepository dataRepository) {
		this.mRecruitmentView = recruitmentView;
		this.mDataRepository = dataRepository;
		mRecruitmentView.setPresenter(this);
	}

	@Override
	public void setFiltering(Constants.ListsFilterType requestType) {
		mCurrentFiltering = requestType;
	}

	@Override
	public void onLoadList(String region, String date) {
		if (mRecruitmentView == null && mCurrentFiltering == null) {
			return;
		}

		mDataRepository.onLoadList(new DataSource.GetLoadListCallback() {
			@Override
			public void onSuccess(List<Information> list) {
				mRecruitmentView.showRecruitmentList(list);
			}

			@Override
			public void onFailure(Throwable throwable) {

			}
		}, mCurrentFiltering, region, date);
	}

	@Override
	public void onRequest(String to, String title, String message, String from, String date, Constants.ListsFilterType filterType) {
		if (mRecruitmentView == null) {
			return;
		}

		mDataRepository.onRequest(new DataSource.GetSendMessageCallback() {
			@Override
			public void onSuccess() {
				mRecruitmentView.showSuccessfullyRequested();
			}

			@Override
			public void onFailure(Throwable throwable) {
				mRecruitmentView.showUnuccessfullyRequested();
				SystemUtils.Dlog.e(throwable.getMessage());
			}

			@Override
			public void onError() {
				mRecruitmentView.showUnuccessfullyRequested();
			}
		}, to, title, message, from, date, filterType);
	}

}
