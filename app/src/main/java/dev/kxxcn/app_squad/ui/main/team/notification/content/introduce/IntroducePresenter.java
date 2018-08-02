package dev.kxxcn.app_squad.ui.main.team.notification.content.introduce;

import java.util.List;

import dev.kxxcn.app_squad.data.DataRepository;
import dev.kxxcn.app_squad.data.DataSource;
import dev.kxxcn.app_squad.data.model.Chatting;

/**
 * Created by kxxcn on 2018-07-09.
 */

public class IntroducePresenter implements IntroduceContract.Presenter {

	private IntroduceContract.View mIntroduceView;
	private DataRepository mDataRepository;

	public IntroducePresenter(IntroduceContract.View introduceView, DataRepository dataRepository) {
		this.mIntroduceView = introduceView;
		this.mDataRepository = dataRepository;
		mIntroduceView.setPresenter(this);
	}

	@Override
	public void onSubscribe(String date, String roomName) {
		if (mIntroduceView == null) {
			return;
		}

		mDataRepository.onSubscribe(new DataSource.GetChattingCallback() {
			@Override
			public void onSuccess(List<Chatting> chattingList) {
				mIntroduceView.showBadgeForUnreadMessages(chattingList);
			}

			@Override
			public void onFailure(Throwable throwable) {

			}
		}, date, roomName);
	}

	@Override
	public void onUpdateReadMessages(List<Chatting> chattingList, String room, String date) {
		if (mIntroduceView == null) {
			return;
		}

		mDataRepository.onUpdateReadMessages(new DataSource.GetCommonCallback() {
			@Override
			public void onSuccess() {

			}

			@Override
			public void onFailure(Throwable throwable) {

			}
		}, chattingList, room, date);
	}

}
