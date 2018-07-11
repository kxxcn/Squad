package dev.kxxcn.app_squad.ui.main.team.notification.content.introduce.chat;

import java.util.List;

import dev.kxxcn.app_squad.data.DataRepository;
import dev.kxxcn.app_squad.data.DataSource;
import dev.kxxcn.app_squad.data.model.Chatting;

/**
 * Created by kxxcn on 2018-07-10.
 */

public class ChattingPresenter implements ChattingContract.Presenter {

	private ChattingContract.View mChattingView;
	private DataRepository mDataRepository;

	public ChattingPresenter(ChattingContract.View chattingView, DataRepository dataRepository) {
		this.mChattingView = chattingView;
		this.mDataRepository = dataRepository;
		mChattingView.setPresenter(this);
	}

	@Override
	public void onChat(Chatting chatting, String title, String date, String roomName) {
		if (mChattingView == null) {
			return;
		}

		mDataRepository.onChat(new DataSource.GetCommonCallback() {
			@Override
			public void onSuccess() {
				mChattingView.showSuccessfullySend();
			}

			@Override
			public void onFailure(Throwable throwable) {
				mChattingView.showUnsuccessfullySend();
			}
		}, chatting, title, date, roomName);
	}

	@Override
	public void onSubscribe(String date, String roomName) {
		if (mChattingView == null) {
			return;
		}

		mDataRepository.onSubscribe(new DataSource.GetChattingCallback() {
			@Override
			public void onSuccess(List<Chatting> chattingList) {
				mChattingView.showChattingList(chattingList);
			}

			@Override
			public void onFailure(Throwable throwable) {

			}
		}, date, roomName);
	}

}
