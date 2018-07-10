package dev.kxxcn.app_squad.ui.main.team.notification.content.introduce.chat;

import java.util.List;

import dev.kxxcn.app_squad.data.model.Chatting;
import dev.kxxcn.app_squad.ui.BasePresenter;
import dev.kxxcn.app_squad.ui.BaseView;

/**
 * Created by kxxcn on 2018-07-10.
 */

public interface ChattingContract {
	interface View extends BaseView<Presenter> {
		void showSuccessfullySend();

		void showUnsuccessfullySend();

		void showChattingList(List<Chatting> chattingList);
	}

	interface Presenter extends BasePresenter {
		void onChat(Chatting chatting, String roomName);

		void onSubscribe(String roomName);
	}
}
