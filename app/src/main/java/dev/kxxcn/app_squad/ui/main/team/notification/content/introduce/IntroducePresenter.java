package dev.kxxcn.app_squad.ui.main.team.notification.content.introduce;

import dev.kxxcn.app_squad.data.DataRepository;

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

}
