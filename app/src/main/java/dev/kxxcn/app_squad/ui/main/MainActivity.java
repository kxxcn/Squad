package dev.kxxcn.app_squad.ui.main;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.allattentionhere.fabulousfilter.AAH_FabulousFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import dev.kxxcn.app_squad.R;
import dev.kxxcn.app_squad.data.DataRepository;
import dev.kxxcn.app_squad.data.remote.RemoteDataSource;
import dev.kxxcn.app_squad.util.TransitionUtils;
import dev.kxxcn.app_squad.util.threading.UiThread;
import devlight.io.library.ntb.NavigationTabBar;

public class MainActivity extends AppCompatActivity implements MainContract.View, AAH_FabulousFragment.AnimationListener {

	private MainContract.Presenter mPresenter;

	@BindView(R.id.vp_main)
	ViewPager vp_main;

	@BindView(R.id.navigationTabBar)
	NavigationTabBar navigationTabBar;

	@Override
	public void setPresenter(MainContract.Presenter presenter) {
		this.mPresenter = presenter;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		TransitionUtils.fade(this);
		ButterKnife.bind(this);

		new MainPresenter(this, DataRepository.getInstance(RemoteDataSource.getInstance(FirebaseAuth.getInstance(), FirebaseDatabase.getInstance().getReference())));

		initUI();
	}

	private void initUI() {
		vp_main.setAdapter(new MainPagerAdapter(getSupportFragmentManager()));

		String[] colors = getResources().getStringArray(R.array.default_preview);
		ArrayList<NavigationTabBar.Model> model = new ArrayList<>();
		model.add(new NavigationTabBar.Model.Builder(getDrawable(R.drawable.ic_game), Color.parseColor(colors[0])).title(getString(R.string.navi_title_match)).build());
		model.add(new NavigationTabBar.Model.Builder(getDrawable(R.drawable.ic_game_list), Color.parseColor(colors[0])).title(getString(R.string.navi_title_list)).build());
		model.add(new NavigationTabBar.Model.Builder(getDrawable(R.drawable.ic_my), Color.parseColor(colors[0])).title(getString(R.string.navi_title_team)).build());
		model.add(new NavigationTabBar.Model.Builder(getDrawable(R.drawable.ic_settings), Color.parseColor(colors[0])).title(getString(R.string.navi_title_setting)).build());
		navigationTabBar.setModels(model);
		navigationTabBar.setViewPager(vp_main, 0);
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

	@Override
	public void onOpenAnimationStart() {

	}

	@Override
	public void onOpenAnimationEnd() {

	}

	@Override
	public void onCloseAnimationStart() {

	}

	@Override
	public void onCloseAnimationEnd() {

	}

	@Override
	public void showLoadingIndicator(final boolean isShowing) {
		UiThread.getInstance().post(new Runnable() {
			@Override
			public void run() {
				if (isShowing) {

				} else {

				}
			}
		});
	}

}
