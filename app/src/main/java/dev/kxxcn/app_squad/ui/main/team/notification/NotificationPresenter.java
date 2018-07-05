package dev.kxxcn.app_squad.ui.main.team.notification;

import dev.kxxcn.app_squad.data.DataRepository;
import dev.kxxcn.app_squad.data.DataSource;
import dev.kxxcn.app_squad.data.model.Information;
import dev.kxxcn.app_squad.data.model.User;
import dev.kxxcn.app_squad.util.SystemUtils;


/**
 * Created by kxxcn on 2018-06-22.
 */

public class NotificationPresenter implements NotificationContract.Presenter {

	private NotificationContract.View mNotificationView;

	private DataRepository mDataRepository;

	public NotificationPresenter(NotificationContract.View notificationView, DataRepository dataRepository) {
		this.mNotificationView = notificationView;
		this.mDataRepository = dataRepository;
		mNotificationView.setPresenter(this);
	}

	@Override
	public void onAgree(Information information, String title, String message) {
		if (mNotificationView == null) {
			return;
		}

		mDataRepository.onAgree(new DataSource.GetSendMessageCallback() {
			@Override
			public void onSuccess() {
				mNotificationView.showSuccessfullyAgree();
			}

			@Override
			public void onFailure(Throwable throwable) {
				SystemUtils.Dlog.e(throwable.getMessage());
				mNotificationView.showUnsuccessfullyAgree();
			}

			@Override
			public void onError() {
				mNotificationView.showUnsuccessfullyAgree();
			}
		}, information, title, message);
	}

	@Override
	public void onLoadEnemyData(final String enemy) {
		if (mNotificationView == null) {
			return;
		}

		mDataRepository.onLoadEnemyData(new DataSource.GetUserCallback() {
			@Override
			public void onSuccess(User user) {
				mNotificationView.showEnemyData(user);
			}

			@Override
			public void onFailure(Throwable throwable) {
				SystemUtils.Dlog.e(throwable.getMessage());
			}
		}, enemy);
	}

}
