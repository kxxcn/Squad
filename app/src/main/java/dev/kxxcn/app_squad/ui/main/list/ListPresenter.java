package dev.kxxcn.app_squad.ui.main.list;

import dev.kxxcn.app_squad.data.DataRepository;
import dev.kxxcn.app_squad.data.DataSource;
import dev.kxxcn.app_squad.data.model.User;

/**
 * Created by kxxcn on 2018-07-18.
 */

public class ListPresenter implements ListContract.Presenter {

	private ListContract.View mListView;
	private DataRepository mDataRepository;

	public ListPresenter(ListContract.View listView, DataRepository dataRepository) {
		this.mListView = listView;
		this.mDataRepository = dataRepository;
		mListView.setPresenter(this);
	}

	@Override
	public void onLoadAccount() {
		if (mListView == null) {
			return;
		}

		mDataRepository.onLoadAccount(new DataSource.GetUserCallback() {
			@Override
			public void onSuccess(User user) {
				mListView.showSuccessfullyLoadAccount(user);
			}

			@Override
			public void onFailure(Throwable throwable) {

			}
		});
	}

}
