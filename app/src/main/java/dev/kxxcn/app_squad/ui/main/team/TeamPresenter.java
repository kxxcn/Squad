package dev.kxxcn.app_squad.ui.main.team;

import dev.kxxcn.app_squad.data.DataRepository;
import dev.kxxcn.app_squad.data.DataSource;
import dev.kxxcn.app_squad.data.model.User;

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
	public void onLoadAccount() {
		if (mTeamView == null) {
			return;
		}
		mDataRepository.onLoadRecord(new DataSource.GetLoadRecordCallback() {
			@Override
			public void onSuccess(User user) {
				mTeamView.setToolbarTitle(user.getTeam());
			}

			@Override
			public void onFailure(Throwable throwable) {

			}
		});
	}

}
