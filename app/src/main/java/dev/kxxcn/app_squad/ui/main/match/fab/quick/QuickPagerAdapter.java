package dev.kxxcn.app_squad.ui.main.match.fab.quick;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import dev.kxxcn.app_squad.data.model.Information;
import dev.kxxcn.app_squad.data.model.User;
import dev.kxxcn.app_squad.ui.main.team.notification.content.introduce.IntroduceFragment;
import dev.kxxcn.app_squad.ui.main.team.notification.content.schedule.ScheduleFragment;

/**
 * Created by kxxcn on 2018-08-03.
 */
public class QuickPagerAdapter extends FragmentStatePagerAdapter {

	private static final int COUNT = 2;

	private Information information;
	private User user;
	private String from;
	private String uid;

	public QuickPagerAdapter(FragmentManager fm, Information information, User user, String from, String uid) {
		super(fm);
		this.information = information;
		this.user = user;
		this.from = from;
		this.uid = uid;
	}

	@Override
	public Fragment getItem(int position) {
		switch (position) {
			case 0:
				return ScheduleFragment.newInstance(information);
			case 1:
				IntroduceFragment newFragment = IntroduceFragment.newInstance(user, from, uid, information.getDate().replace("-", ""), information.isConnect());
				return newFragment;
		}
		return null;
	}

	@Override
	public int getCount() {
		return COUNT;
	}
}
