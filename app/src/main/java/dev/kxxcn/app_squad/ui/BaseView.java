package dev.kxxcn.app_squad.ui;

/**
 * Created by kxxcn on 2018-04-26.
 */
public interface BaseView<T> {
	void setPresenter(T presenter);
	void showLoadingIndicator(final boolean isShowing);
}
