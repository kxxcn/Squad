package dev.kxxcn.app_squad.data;

import java.util.List;

import dev.kxxcn.app_squad.data.model.Battle;
import dev.kxxcn.app_squad.data.model.Chatting;
import dev.kxxcn.app_squad.data.model.Information;
import dev.kxxcn.app_squad.data.model.Notification;
import dev.kxxcn.app_squad.data.model.User;
import dev.kxxcn.app_squad.util.Constants;

/**
 * Created by kxxcn on 2018-05-01.
 */

public abstract class DataSource {

	public interface GetCommonCallback {
		void onSuccess();

		void onFailure(Throwable throwable);
	}

	public interface GetSignupCallback {
		void onSuccess();

		void onFailure(Throwable throwable);

		void onDuplicatedTeam();
	}

	public interface GetLoadListCallback {
		void onSuccess(List<Information> list);

		void onFailure(Throwable throwable);
	}

	public interface GetUserCallback {
		void onSuccess(User user);

		void onFailure(Throwable throwable);
	}

	public interface GetSendMessageCallback {
		void onSuccess();

		void onFailure(Throwable throwable);

		void onError();
	}

	public interface GetNotificationCallback {
		void onSuccess(List<Notification> list);

		void onFailure(Throwable throwable);
	}

	public interface GetInformationCallback {
		void onSuccess(Information information);

		void onFailure(Throwable throwable);
	}

	public interface GetBattleCallback {
		void onSuccess(List<Battle> battleList);

		void onFailure(Throwable throwable);
	}

	public interface GetChattingCallback {
		void onSuccess(List<Chatting> chattingList);

		void onFailure(Throwable throwable);
	}

	public abstract void onSignup(GetSignupCallback callback, String email, String contact, String password, String team);

	public abstract void onLogin(GetCommonCallback callback, String email, String password);

	public abstract void onLogout(GetCommonCallback callback);

	public abstract void onLoadList(GetLoadListCallback callback, Constants.ListsFilterType requestType, String region, String date);

	public abstract void onRegister(GetCommonCallback callback, Information information, Constants.ListsFilterType requestType);

	public abstract void onLoadRecord(GetBattleCallback callback);

	public abstract void onRequest(GetSendMessageCallback callback, String to, String title, String message, String from, String date, Constants.ListsFilterType filterType);

	public abstract void onLoadAccount(GetUserCallback callback);

	public abstract void onLoadNotification(GetNotificationCallback callback);

	public abstract void onReadNotification(GetCommonCallback callback, List<Notification> notifications);

	public abstract void onRemove(GetCommonCallback callback, Constants.ListsFilterType filterType, String date);

	public abstract void onLoadMatch(GetInformationCallback callback, boolean isHome, String date, String enemy, String flag);

	public abstract void onLoadEnemyData(GetUserCallback callback, String enemy);

	public abstract void onAgree(GetSendMessageCallback callback, Information information, String title, String message, String flag);

	public abstract void onUpdateNotice(GetCommonCallback callback, boolean on, Constants.NoticeFilterType requestType);

	public abstract void onRemoveNotification(GetCommonCallback callback);

	public abstract void onUpdateToken(GetCommonCallback callback, String token);

	public abstract void onChat(GetCommonCallback callback, Chatting chatting, String title, String date, String roomName);

	public abstract void onSubscribe(GetChattingCallback callback, String date, String roomName);

}
