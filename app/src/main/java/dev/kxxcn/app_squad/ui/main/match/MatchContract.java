package dev.kxxcn.app_squad.ui.main.match;

import dev.kxxcn.app_squad.data.model.Information;
import dev.kxxcn.app_squad.ui.BasePresenter;
import dev.kxxcn.app_squad.ui.BaseView;

/**
 * Created by kxxcn on 2018-04-30.
 */

public interface MatchContract {
	interface View extends BaseView<MatchContract.Presenter> {

	}

	interface Presenter extends BasePresenter {
		void onRegister(Information information);
	}
}
