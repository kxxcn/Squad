package dev.kxxcn.app_squad.view.match;

/**
 * Created by Jun on 2018-04-27.
 */

public class MatchPresenter implements MatchContract.Presenter {

	private final MatchContract.View mMatchView;

	public MatchPresenter(MatchContract.View matchView) {
		mMatchView = matchView;
		mMatchView.setPresenter(this);
	}

}
