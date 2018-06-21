package dev.kxxcn.app_squad.data;

import java.util.List;

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

	public interface GetLoadRecordCallback {
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

	public abstract void onSignup(GetSignupCallback callback, String email, String password, String team);

	public abstract void onLogin(GetCommonCallback callback, String email, String password);

	public abstract void onLogout(GetCommonCallback callback);

	public abstract void onLoadList(GetLoadListCallback callback, Constants.ListsFilterType requestType);

	public abstract void onRegister(GetCommonCallback callback, Information information, Constants.ListsFilterType requestType);

	public abstract void onLoadRecord(GetLoadRecordCallback callback);

	public abstract void onSendMessage(GetSendMessageCallback callback, String to, String title, String message, String from);

	public abstract void onLoadAccount(GetCommonCallback callback);

	public abstract void onLoadNotification(GetNotificationCallback callback);

	public abstract void onReadNotification(GetCommonCallback callback, List<Notification> notifications);

	public abstract void onRemove(GetCommonCallback callback, String date);

}
