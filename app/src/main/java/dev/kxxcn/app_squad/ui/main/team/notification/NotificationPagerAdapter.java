package dev.kxxcn.app_squad.ui.main.team.notification;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import dev.kxxcn.app_squad.data.model.Information;
import dev.kxxcn.app_squad.data.model.User;
import dev.kxxcn.app_squad.ui.main.team.TeamContract;
import dev.kxxcn.app_squad.ui.main.team.notification.content.introduce.IntroduceFragment;
import dev.kxxcn.app_squad.ui.main.team.notification.content.schedule.ScheduleFragment;


/**
 * Created by kxxcn on 2018-06-22.
 */

public class NotificationPagerAdapter extends FragmentStatePagerAdapter implements TeamContract.OnReadMessageCallback {

	private static final int COUNT = 2;

	private Information information;
	private User user;
	private String from;
	private String uid;

	private TeamContract.OnReadMessageCallback mReadMessageCallback;

	public NotificationPagerAdapter(FragmentManager fm, Information information, User user, String from, String uid) {
		super(fm);
		this.information = information;
		this.user = user;
		this.from = from;
		this.uid = uid;
	}

	public void setOnReadMessageCallback(TeamContract.OnReadMessageCallback listener) {
		this.mReadMessageCallback = listener;
	}

	@Override
	public Fragment getItem(int position) {
		switch (position) {
			case 0:
				return ScheduleFragment.newInstance(information);
			case 1:
				IntroduceFragment newFragment = IntroduceFragment.newInstance(user, from, uid, information.getDate().replace("-", ""), information.isConnect());
				newFragment.setOnReadMessageCallback(this);
				return newFragment;
		}
		return null;
	}

	@Override
	public int getCount() {
		return COUNT;
	}

	@Override
	public void onReadMessageCallback() {
		mReadMessageCallback.onReadMessageCallback();
	}

}
