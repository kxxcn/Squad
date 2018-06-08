package dev.kxxcn.app_squad.ui.main.team;

import dev.kxxcn.app_squad.ui.BasePresenter;
import dev.kxxcn.app_squad.ui.BaseView;

/**
 * Created by kxxcn on 2018-06-08.
 */

public interface TeamContract {
	interface View extends BaseView<TeamContract.Presenter> {
		void setToolbarTitle(String title);
	}

	interface Presenter extends BasePresenter {
		void onLoadRecord();
	}
}
