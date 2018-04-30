package dev.kxxcn.app_squad.screen.main.match;

/**
 * Created by kxxcn on 2018-04-30.
 */
public class MatchPresenter implements MatchContract.Presenter {

	private final MatchContract.View mMatchView;

	public MatchPresenter(MatchContract.View matchView) {
		mMatchView = matchView;
		mMatchView.setPresenter(this);
	}

}
