package dev.kxxcn.app_squad.ui.main;


import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import dev.kxxcn.app_squad.R;
import dev.kxxcn.app_squad.util.SwipeViewPager;
import dev.kxxcn.app_squad.util.TransitionUtils;
import devlight.io.library.ntb.NavigationTabBar;

public class MainActivity extends AppCompatActivity {

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

		initUI();
	}

	@Override
	public void onBackPressed() {
		showDialog();
	}

	private void initUI() {
		vp_main.setPagingEnabled(false);
		vp_main.setAdapter(new MainPagerAdapter(getSupportFragmentManager()));

		String[] colors = getResources().getStringArray(R.array.default_preview);
		ArrayList<NavigationTabBar.Model> model = new ArrayList<>();
		model.add(new NavigationTabBar.Model.Builder(getDrawable(R.drawable.ic_game), Color.parseColor(colors[0])).title(getString(R.string.navi_title_match)).build());
		model.add(new NavigationTabBar.Model.Builder(getDrawable(R.drawable.ic_game_list), Color.parseColor(colors[0])).title(getString(R.string.navi_title_list)).build());
		model.add(new NavigationTabBar.Model.Builder(getDrawable(R.drawable.ic_my), Color.parseColor(colors[0])).title(getString(R.string.navi_title_team)).build());
		model.add(new NavigationTabBar.Model.Builder(getDrawable(R.drawable.ic_settings), Color.parseColor(colors[0])).title(getString(R.string.navi_title_setting)).build());
		navigationTabBar.setModels(model);
		navigationTabBar.setViewPager(vp_main, 0);
	}

	private void showDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage(getString(R.string.want_to_quit));
		builder.setPositiveButton(getString(R.string.yes),
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						moveTaskToBack(true);
						finish();
						android.os.Process.killProcess(android.os.Process.myPid());
					}
				}).setNegativeButton(getString(R.string.no),
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						return;
					}
				}).show();
	}

}
