package dev.kxxcn.app_squad.view.main;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import dev.kxxcn.app_squad.view.list.ListFragment;
import dev.kxxcn.app_squad.view.match.MatchFragment;
import dev.kxxcn.app_squad.view.setting.SettingFragment;
import dev.kxxcn.app_squad.view.team.TeamFragment;

/**
 * Created by kxxcn on 2018-04-26.
 */
public class MainPagerAdapter extends FragmentStatePagerAdapter {

	private static final int COUNT = 4;

	private static final int MATCH = 0;
	private static final int LIST = 1;
	private static final int TEAM = 2;
	private static final int SETTING = 3;

	public MainPagerAdapter(FragmentManager fm) {
		super(fm);
	}

	@Override
	public Fragment getItem(int position) {
		switch (position) {
			case MATCH:
				return MatchFragment.newInstance();
			case LIST:
				return ListFragment.newInstance();
			case TEAM:
				return TeamFragment.newInstance();
			case SETTING:
				return SettingFragment.newInstance();
		}
		return null;
	}

	@Override
	public int getCount() {
		return COUNT;
	}

}
