package dev.kxxcn.app_squad.ui.main.team.notification;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import dev.kxxcn.app_squad.data.model.Information;
import dev.kxxcn.app_squad.data.model.User;
import dev.kxxcn.app_squad.ui.main.team.notification.content.IntroduceFragment;
import dev.kxxcn.app_squad.ui.main.team.notification.content.ScheduleFragment;

/**
 * Created by kxxcn on 2018-06-22.
 */

public class NotificationPagerAdapter extends FragmentStatePagerAdapter {

	private static final int COUNT = 2;

	private Information information;
	private User user;

	public NotificationPagerAdapter(FragmentManager fm, Information information, User user) {
		super(fm);
		this.information = information;
		this.user = user;
	}

	@Override
	public Fragment getItem(int position) {
		switch (position) {
			case 0:
				return ScheduleFragment.newInstance(information);
			case 1:
				return IntroduceFragment.newInstance(user);
		}
		return null;
	}

	@Override
	public int getCount() {
		return COUNT;
	}

}
