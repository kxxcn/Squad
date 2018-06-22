package dev.kxxcn.app_squad.ui.main.team.notification;

import dev.kxxcn.app_squad.data.DataRepository;
import dev.kxxcn.app_squad.data.DataSource;
import dev.kxxcn.app_squad.data.model.User;
import dev.kxxcn.app_squad.util.Dlog;

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
	public void onAgree() {

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
				Dlog.e(throwable.getMessage());
			}
		}, enemy);
	}

}
