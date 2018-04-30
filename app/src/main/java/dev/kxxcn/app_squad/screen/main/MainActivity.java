package dev.kxxcn.app_squad.screen.main;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import dev.kxxcn.app_squad.R;
import devlight.io.library.ntb.NavigationTabBar;

public class MainActivity extends AppCompatActivity implements MainContract.View {

	private static final int MATCH = 0;
	private static final int LIST = 1;
	private static final int TEAM = 2;
	private static final int SETTING = 3;

	private MainContract.Presenter mPresenter;

	@BindView(R.id.vp_main)
	ViewPager viewPager;

	@BindView(R.id.navigationBar)
	NavigationTabBar navigationTabBar;

	@Override
	public void setPresenter(MainContract.Presenter presenter) {
		this.mPresenter = presenter;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		ButterKnife.bind(this);

		initUI();
	}

	private void initUI() {
		viewPager.setAdapter(new MainPagerAdapter(getSupportFragmentManager()));

		String[] colors = getResources().getStringArray(R.array.default_preview);
		ArrayList<NavigationTabBar.Model> model = new ArrayList<>();
		model.add(new NavigationTabBar.Model.Builder(getResources().getDrawable(R.drawable.ic_game), Color.parseColor(colors[0]))
				.title(getString(R.string.navi_title_match)).build());
		model.add(new NavigationTabBar.Model.Builder(getResources().getDrawable(R.drawable.ic_game_list), Color.parseColor(colors[0]))
				.title(getString(R.string.navi_title_list)).build());
		model.add(new NavigationTabBar.Model.Builder(getResources().getDrawable(R.drawable.ic_my), Color.parseColor(colors[0]))
				.title(getString(R.string.navi_title_team)).build());
		model.add(new NavigationTabBar.Model.Builder(getResources().getDrawable(R.drawable.ic_settings), Color.parseColor(colors[0]))
				.title(getString(R.string.navi_title_setting)).build());
		navigationTabBar.setModels(model);
		navigationTabBar.setViewPager(viewPager, 0);
		navigationTabBar.setOnPageChangeListener(onPageChangeListener);
	}

	ViewPager.OnPageChangeListener onPageChangeListener = new ViewPager.OnPageChangeListener() {
		@Override
		public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

		}

		@Override
		public void onPageSelected(int position) {

		}

		@Override
		public void onPageScrollStateChanged(int state) {

		}
	};

}
