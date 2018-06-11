package dev.kxxcn.app_squad.ui.main.team;

import dev.kxxcn.app_squad.data.DataRepository;
import dev.kxxcn.app_squad.data.DataSource;
import dev.kxxcn.app_squad.data.model.User;
import dev.kxxcn.app_squad.util.Dlog;

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
		mDataRepository.onLoadRecord(new DataSource.GetLoadRecordCallback() {
			@Override
			public void onSuccess(User user) {
				try {
					mTeamView.setToolbarTitle(user.getTeam());
				} catch (NullPointerException e) {
					onLogout();
					e.printStackTrace();
				}
			}

			@Override
			public void onFailure(Throwable throwable) {

			}
		});
	}

	@Override
	public void onLoadAccount() {
		if (mTeamView == null) {
			return;
		}

		mDataRepository.onLoadAccount(new DataSource.GetCommonCallback() {
			@Override
			public void onSuccess() {

			}

			@Override
			public void onFailure(Throwable throwable) {
				onLogout();
				Dlog.d(throwable.getMessage());
			}
		});
	}

	private void onLogout() {
		if (mTeamView == null) {
			return;
		}

		mDataRepository.onLogout(new DataSource.GetCommonCallback() {
			@Override
			public void onSuccess() {
				mTeamView.showErrorBadRequest();
			}

			@Override
			public void onFailure(Throwable throwable) {
				Dlog.e(throwable.getMessage());
			}
		});
	}

}
