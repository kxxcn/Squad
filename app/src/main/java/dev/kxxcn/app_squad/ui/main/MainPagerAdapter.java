package dev.kxxcn.app_squad.ui.main;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import dev.kxxcn.app_squad.ui.main.list.ListFragment;
import dev.kxxcn.app_squad.ui.main.match.MatchFragment;
import dev.kxxcn.app_squad.ui.main.setting.SettingFragment;
import dev.kxxcn.app_squad.ui.main.team.TeamFragment;

import static dev.kxxcn.app_squad.util.Constants.*;

/**
 * Created by kxxcn on 2018-04-26.
 */
public class MainPagerAdapter extends FragmentStatePagerAdapter {

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
