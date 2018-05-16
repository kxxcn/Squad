package dev.kxxcn.app_squad.ui.main.list;

import dev.kxxcn.app_squad.data.DataRepository;

/**
 * Created by kxxcn on 2018-04-30.
 */

public class ListPresenter implements ListContract.Presenter {

	private ListContract.View mListView;
	private DataRepository mDataRepository;

	public ListPresenter(ListContract.View listView, DataRepository dataRepository) {
		this.mListView = listView;
		this.mDataRepository = dataRepository;
		this.mListView.setPresenter(this);
	}

}
