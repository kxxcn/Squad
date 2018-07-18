package dev.kxxcn.app_squad.ui.main.list;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import dev.kxxcn.app_squad.data.model.User;
import dev.kxxcn.app_squad.ui.main.list.matchlist.MatchListFragment;
import dev.kxxcn.app_squad.ui.main.list.playerlist.PlayerListFragment;
import dev.kxxcn.app_squad.ui.main.list.recruitmentlist.RecruitmentListFragment;

/**
 * Created by kxxcn on 2018-05-09.
 */

public class ListPagerAdapter extends FragmentStatePagerAdapter {

	private static final int REGISTRATION_MATCH = 0;
	private static final int RECRUITMENT_PLAYER = 1;
	private static final int REGISTRATION_PLAYER = 2;

	private static final int COUNT = 3;

	private String region;
	private String date;
	private User user;

	public ListPagerAdapter(FragmentManager fm, String region, String date, User user) {
		super(fm);
		this.region = region;
		this.date = date;
		this.user = user;
	}

	@Override
	public Fragment getItem(int position) {
		switch (position) {
			case REGISTRATION_MATCH:
				return MatchListFragment.newInstance(user);
			case RECRUITMENT_PLAYER:
				return RecruitmentListFragment.newInstance(user);
			case REGISTRATION_PLAYER:
				return PlayerListFragment.newInstance(user);
		}
		return null;
	}

	@Override
	public int getCount() {
		return COUNT;
	}

}
