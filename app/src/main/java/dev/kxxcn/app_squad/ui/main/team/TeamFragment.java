package dev.kxxcn.app_squad.ui.main.team;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.andremion.counterfab.CounterFab;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dev.kxxcn.app_squad.R;
import dev.kxxcn.app_squad.data.DataRepository;
import dev.kxxcn.app_squad.data.model.Information;
import dev.kxxcn.app_squad.data.model.Notification;
import dev.kxxcn.app_squad.data.remote.MyFirebaseMessagingService;
import dev.kxxcn.app_squad.data.remote.RemoteDataSource;
import dev.kxxcn.app_squad.ui.login.LoginActivity;
import dev.kxxcn.app_squad.ui.main.MainActivity;
import dev.kxxcn.app_squad.ui.main.team.notification.NotificationDialog;
import dev.kxxcn.app_squad.util.BusProvider;

import static dev.kxxcn.app_squad.util.Constants.FORMAT_CHARACTER;
import static dev.kxxcn.app_squad.util.Constants.FORMAT_LENGTH;

/**
 * Created by kxxcn on 2018-04-26.
 */
public class TeamFragment extends Fragment implements TeamContract.View, NavigationView.OnNavigationItemSelectedListener, View.OnClickListener, TeamContract.ItemClickListener {

	private static final String DIALOG_FRAGMENT = "dialog";

	private static final int BEGIN_INDEX = 1;

	@BindView(R.id.rv_team)
	RecyclerView rv_team;

	@BindView(R.id.ll_collapstoolbar)
	CollapsingToolbarLayout toolbar;

	@BindView(R.id.iv_collapsing)
	ImageView iv_collapsing;

	@BindView(R.id.fab)
	CounterFab fab;

	@BindView(R.id.navigation_drawer)
	DrawerLayout navigation_drawer;
	@BindView(R.id.navigation_view)
	NavigationView navigation_view;
	@BindView(R.id.rv_notification)
	RecyclerView rv_notification;

	@BindView(R.id.include_drawer_header)
	View include_drawer_header;

	private TeamContract.Presenter mPresenter;

	private String mEnemy;

	private int[] imgs = {R.drawable.team_banner1, R.drawable.team_banner2, R.drawable.team_banner3,
			R.drawable.team_banner4, R.drawable.team_banner5, R.drawable.team_banner6, R.drawable.team_banner7,
			R.drawable.team_banner8, R.drawable.team_banner9, R.drawable.team_banner10};

	private List<Notification> notifications;
	private List<Notification> unReadNotifications;

	@Override
	public void setPresenter(TeamContract.Presenter presenter) {
		this.mPresenter = presenter;
	}

	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_team, container, false);
		ButterKnife.bind(this, view);

		new TeamPresenter(this, DataRepository.getInstance(RemoteDataSource.getInstance(
				FirebaseAuth.getInstance(), FirebaseDatabase.getInstance().getReference())));

		navigation_drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
		navigation_view.setNavigationItemSelectedListener(this);

		ImageButton ib_cancel = view.findViewById(R.id.ib_cancel);
		ib_cancel.setOnClickListener(this);

		return view;
	}

	@Override
	public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		mPresenter.onLoadAccount();
		mPresenter.onLoadRecord();
		mPresenter.onLoadNotification();
		Glide.with(this).load(imgs[new Random().nextInt(imgs.length)]).diskCacheStrategy(DiskCacheStrategy.NONE).into(iv_collapsing);
		List<String> list = new ArrayList<>(0);
		rv_team.setAdapter(new TeamAdapter(list));
	}

	public static Fragment newInstance() {
		return new TeamFragment();
	}

	@Override
	public void showLoadingIndicator(boolean isShowing) {

	}

	@Override
	public void setToolbarTitle(String title) {
		toolbar.setTitle(title);
	}

	@Override
	public void showErrorBadRequest() {
		getContext().startActivity(new Intent(getContext(), LoginActivity.class));
		getActivity().finish();
	}

	@Override
	public void showSuccessLoadNotification(List<Notification> list) {
		notifications = new ArrayList<>(0);
		unReadNotifications = new ArrayList<>(0);
		notifications = list;
		for (int i = 0; i < notifications.size(); i++) {
			if (!notifications.get(i).isCheck()) {
				unReadNotifications.add(notifications.get(i));
			}
		}
		if (unReadNotifications.size() != 0) {
			BusProvider.getInstance().post(MainActivity.SHOW_BADGE);
		} else {
			BusProvider.getInstance().post(MainActivity.HIDE_BADGE);
		}
		fab.setCount(unReadNotifications.size());

		Collections.sort(notifications, new Compare());
		rv_notification.setAdapter(new LoadAdapter(notifications, this));
	}

	@Override
	public void showFailureLoadNotification() {

	}

	@OnClick(R.id.fab)
	public void onClickFab() {
		if (unReadNotifications.size() != 0) {
			fab.setCount(0);
			for (int i = 0; i < unReadNotifications.size(); i++) {
				unReadNotifications.get(i).setCheck(true);
			}
			mPresenter.onReadNotification(unReadNotifications);
		}
		if (notifications.size() != 0) {
			if (MyFirebaseMessagingService.notificationManager != null) {
				if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
					MyFirebaseMessagingService.notificationManager.deleteNotificationChannel(getString(R.string.app_name));
				} else {
					MyFirebaseMessagingService.notificationManager.cancelAll();
				}
			}
			navigation_drawer.openDrawer(GravityCompat.END);
		} else {
			Toast.makeText(getContext(), getString(R.string.team_no_notification), Toast.LENGTH_SHORT).show();
		}
	}

	@Override
	public boolean onNavigationItemSelected(@NonNull MenuItem item) {
		navigation_drawer.closeDrawer(GravityCompat.END);
		return true;
	}

	@Override
	public void onClick(View v) {
		navigation_drawer.closeDrawer(GravityCompat.END);
	}

	class Compare implements Comparator<Notification> {
		@Override
		public int compare(Notification o1, Notification o2) {
			int ret = 0;
			String date1 = o1.getTimestamp().substring(0, o1.getTimestamp().indexOf(" "));
			String date2 = o2.getTimestamp().substring(0, o2.getTimestamp().indexOf(" "));
			if (date1.equals(date2)) {
				String time1 = o1.getTimestamp().substring(o1.getTimestamp().indexOf(" ") + 1, o1.getTimestamp().length());
				String time2 = o2.getTimestamp().substring(o2.getTimestamp().indexOf(" ") + 1, o2.getTimestamp().length());
				if (time1.length() == FORMAT_LENGTH) {
					time1 = FORMAT_CHARACTER + time1;
				}
				if (time2.length() == FORMAT_LENGTH) {
					time2 = FORMAT_CHARACTER + time2;
				}
				ret = time2.compareTo(time1);
			} else {
				ret = date2.compareTo(date1);
			}
			return ret;
		}
	}

	@Override
	public void onClick(int position) {
		int index = notifications.get(position).getMessage().indexOf("]");
		mEnemy = notifications.get(position).getMessage().substring(BEGIN_INDEX, index);
		mPresenter.onLoadMatch(notifications.get(position).getDate());
	}

	@Override
	public void showSuccessfullyLoadInformation(Information information) {
		if (!information.isConnect()) {
			navigation_drawer.closeDrawer(GravityCompat.END);
			DialogFragment newFragment = NotificationDialog.newInstance(information, mEnemy);
			newFragment.show(getChildFragmentManager(), DIALOG_FRAGMENT);
		} else {
			showScheduledMatch();
		}
	}

	private void showScheduledMatch() {
		Toast.makeText(getContext(), getString(R.string.team_scheduled_match), Toast.LENGTH_SHORT).show();
	}

}
