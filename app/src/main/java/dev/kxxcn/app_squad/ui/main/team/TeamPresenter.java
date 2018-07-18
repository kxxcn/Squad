package dev.kxxcn.app_squad.ui.main.team;

import java.util.List;

import dev.kxxcn.app_squad.data.DataRepository;
import dev.kxxcn.app_squad.data.DataSource;
import dev.kxxcn.app_squad.data.model.Battle;
import dev.kxxcn.app_squad.data.model.Information;
import dev.kxxcn.app_squad.data.model.Notification;
import dev.kxxcn.app_squad.data.model.User;
import dev.kxxcn.app_squad.util.SystemUtils;


/**
 * Created by kxxcn on 2018-04-30.
 */

public class TeamPresenter implements TeamContract.Presenter {

	private TeamContract.View mTeamView;
	private DataRepository mDataRepository;

	public TeamPresenter(TeamContract.View teamView, DataRepository dataRepository) {
		this.mTeamView = teamView;
		this.mDataRepository = dataRepository;
		mTeamView.setPresenter(this);
	}

	@Override
	public void onLoadRecord() {
		if (mTeamView == null) {
			return;
		}

		mDataRepository.onLoadRecord(new DataSource.GetBattleCallback() {
			@Override
			public void onSuccess(List<Battle> battleList) {
				mTeamView.showSuccessfullyLoadBattle(battleList);
			}

			@Override
			public void onFailure(Throwable throwable) {
				SystemUtils.Dlog.e(throwable.getMessage());
				mTeamView.showUnsuccessfullyLoadBattle();
			}
		});
	}

	@Override
	public void onLoadAccount() {
		if (mTeamView == null) {
			return;
		}

		mDataRepository.onLoadAccount(new DataSource.GetUserCallback() {
			@Override
			public void onSuccess(User user) {
				mTeamView.setToolbarTitle(user);
			}

			@Override
			public void onFailure(Throwable throwable) {
				mTeamView.showInvalidAccount();
				SystemUtils.Dlog.e(throwable.getMessage());
			}
		});
	}

	@Override
	public void onLogout() {
		if (mTeamView == null) {
			return;
		}

		mDataRepository.onLogout(new DataSource.GetCommonCallback() {
			@Override
			public void onSuccess() {
				mTeamView.showSuccessfullyLogout();
			}

			@Override
			public void onFailure(Throwable throwable) {
				SystemUtils.Dlog.e(throwable.getMessage());
			}
		});
	}

	@Override
	public void onLoadNotification() {
		if (mTeamView == null) {
			return;
		}

		mDataRepository.onLoadNotification(new DataSource.GetNotificationCallback() {
			@Override
			public void onSuccess(List<Notification> list) {
				mTeamView.showSuccessLoadNotification(list);
			}

			@Override
			public void onFailure(Throwable throwable) {
				mTeamView.showFailureLoadNotification();
			}
		});
	}

	@Override
	public void onReadNotification(List<Notification> notifications) {
		if (mTeamView == null) {
			return;
		}

		mDataRepository.onReadNotification(new DataSource.GetCommonCallback() {
			@Override
			public void onSuccess() {

			}

			@Override
			public void onFailure(Throwable throwable) {

			}
		}, notifications);

	}

	@Override
	public void onLoadMatch(boolean isHome, String date, String enemy, final String flag) {
		if (mTeamView == null) {
			return;
		}

		mDataRepository.onLoadMatch(new DataSource.GetInformationCallback() {
			@Override
			public void onSuccess(Information information) {
				mTeamView.showSuccessfullyLoadInformation(information, flag);
			}

			@Override
			public void onFailure(Throwable throwable) {
				SystemUtils.Dlog.e(throwable.getMessage());
			}
		}, isHome, date, enemy, flag);
	}

	@Override
	public void onRemoveNotification() {
		if (mTeamView == null) {
			return;
		}

		mDataRepository.onRemoveNotification(new DataSource.GetCommonCallback() {
			@Override
			public void onSuccess() {
				mTeamView.showSuccessfullyRemoveNotification();
			}

			@Override
			public void onFailure(Throwable throwable) {
				mTeamView.showUnsuccessfullyRemoveNotification();
			}
		});
	}

}
