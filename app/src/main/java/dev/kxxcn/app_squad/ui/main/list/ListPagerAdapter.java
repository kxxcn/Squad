package dev.kxxcn.app_squad.ui.main.list;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import dev.kxxcn.app_squad.ui.main.list.recruitmentlist.RecruitmentListFragment;
import dev.kxxcn.app_squad.ui.main.list.matchlist.MatchListFragment;
import dev.kxxcn.app_squad.ui.main.list.playerlist.PlayerListFragment;

/**
 * Created by kxxcn on 2018-05-09.
 */

public class ListPagerAdapter extends FragmentStatePagerAdapter {

	private static final int REGISTRATION_MATCH = 0;
	private static final int RECRUITMENT_PLAYER = 1;
	private static final int REGISTRATION_PLAYER = 2;

	private static final int COUNT = 3;

	public ListPagerAdapter(FragmentManager fm) {
		super(fm);
	}

	@Override
	public Fragment getItem(int position) {
		switch (position) {
			case REGISTRATION_MATCH:
				return MatchListFragment.newInstance();
			case RECRUITMENT_PLAYER:
				return RecruitmentListFragment.newInstance();
			case REGISTRATION_PLAYER:
				return PlayerListFragment.newInstance();
		}
		return null;
	}

	@Override
	public int getCount() {
		return COUNT;
	}

}
