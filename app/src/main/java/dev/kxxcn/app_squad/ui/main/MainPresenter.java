package dev.kxxcn.app_squad.ui.main;

import dev.kxxcn.app_squad.data.DataRepository;

/**
 * Created by kxxcn on 2018-04-26.
 */
public class MainPresenter implements MainContract.Presenter {

	private MainContract.View mMainView;
	private DataRepository dataRepository;

	public MainPresenter(MainContract.View mMainView, DataRepository dataRepository) {
		this.mMainView = mMainView;
		this.dataRepository = dataRepository;
		mMainView.setPresenter(this);
	}

}
