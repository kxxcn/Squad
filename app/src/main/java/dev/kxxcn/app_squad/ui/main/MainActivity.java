package dev.kxxcn.app_squad.ui.main;


import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.squareup.otto.Subscribe;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import dev.kxxcn.app_squad.R;
import dev.kxxcn.app_squad.util.BusProvider;
import dev.kxxcn.app_squad.util.DialogUtils;
import dev.kxxcn.app_squad.util.SwipeViewPager;
import dev.kxxcn.app_squad.util.SystemUtils;
import dev.kxxcn.app_squad.util.TransitionUtils;
import devlight.io.library.ntb.NavigationTabBar;

public class MainActivity extends AppCompatActivity {

	private static final int TEAM = 2;

	public static final int SHOW_BADGE = 0;
	public static final int HIDE_BADGE = 1;

	@BindView(R.id.vp_main)
	SwipeViewPager vp_main;

	@BindView(R.id.navigationTabBar)
	NavigationTabBar navigationTabBar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		TransitionUtils.fade(this);
		ButterKnife.bind(this);
		BusProvider.getInstance().register(this);

		initUI();
	}

	@Override
	public void onBackPressed() {
		showDialog();
	}

	private void initUI() {
		vp_main.setPagingEnabled(false);
		vp_main.setAdapter(new MainPagerAdapter(getSupportFragmentManager()));
		vp_main.setOffscreenPageLimit(3); // Resolves the problem of RecyclerView items disappearing

		String[] colors = getResources().getStringArray(R.array.default_preview);
		ArrayList<NavigationTabBar.Model> model = new ArrayList<>();
		model.add(new NavigationTabBar.Model.Builder(getDrawable(R.drawable.ic_game), Color.parseColor(colors[0])).title(getString(R.string.navi_title_match)).build());
		model.add(new NavigationTabBar.Model.Builder(getDrawable(R.drawable.ic_game_list), Color.parseColor(colors[0])).title(getString(R.string.navi_title_list)).build());
		model.add(new NavigationTabBar.Model.Builder(getDrawable(R.drawable.ic_my), Color.parseColor(colors[0])).title(getString(R.string.navi_title_team))
				.badgeTitle(getString(R.string.team_notification_match)).build());
		model.add(new NavigationTabBar.Model.Builder(getDrawable(R.drawable.ic_settings), Color.parseColor(colors[0])).title(getString(R.string.navi_title_setting)).build());
		navigationTabBar.setModels(model);
		navigationTabBar.setViewPager(vp_main, 0);
		navigationTabBar.getModels().get(TEAM).hideBadge();
	}

	private void showDialog() {
		DialogUtils.showAlertDialog(this, getString(R.string.want_to_quit), positiveListener, negativeListener);
	}

	private DialogInterface.OnClickListener positiveListener = new DialogInterface.OnClickListener() {
		@Override
		public void onClick(DialogInterface dialog, int which) {
			SystemUtils.onFinish(MainActivity.this);
		}
	};

	private DialogInterface.OnClickListener negativeListener = new DialogInterface.OnClickListener() {
		@Override
		public void onClick(DialogInterface dialog, int which) {
			return;
		}
	};

	@Subscribe
	public void onRecived(Object o) {
		if ((int) o == SHOW_BADGE) {
			navigationTabBar.getModels().get(TEAM).showBadge();
		} else if ((int) o == HIDE_BADGE) {
			navigationTabBar.getModels().get(TEAM).hideBadge();
		}
	}

}
