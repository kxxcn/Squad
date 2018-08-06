package dev.kxxcn.app_squad.ui.main.team;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
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
import android.widget.ProgressBar;
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
import dev.kxxcn.app_squad.data.model.Battle;
import dev.kxxcn.app_squad.data.model.Information;
import dev.kxxcn.app_squad.data.model.Notification;
import dev.kxxcn.app_squad.data.model.User;
import dev.kxxcn.app_squad.data.remote.MyFirebaseMessagingService;
import dev.kxxcn.app_squad.data.remote.RemoteDataSource;
import dev.kxxcn.app_squad.ui.login.LoginActivity;
import dev.kxxcn.app_squad.ui.main.MainActivity;
import dev.kxxcn.app_squad.ui.main.team.notification.NotificationDialog;
import dev.kxxcn.app_squad.ui.main.team.notification.content.introduce.chat.ChattingDialog;
import dev.kxxcn.app_squad.util.BusProvider;
import dev.kxxcn.app_squad.util.DialogUtils;
import dev.kxxcn.app_squad.util.threading.UiThread;

import static dev.kxxcn.app_squad.data.remote.APIPersistence.TYPE_CHATTING;
import static dev.kxxcn.app_squad.data.remote.APIPersistence.TYPE_REQUEST;
import static dev.kxxcn.app_squad.data.remote.APIPersistence.TYPE_RESPONSE;
import static dev.kxxcn.app_squad.util.Constants.DIALOG_FRAGMENT;
import static dev.kxxcn.app_squad.util.Constants.FORMAT_CHARACTER;
import static dev.kxxcn.app_squad.util.Constants.FORMAT_LENGTH;


/**
 * Created by kxxcn on 2018-04-26.
 */
public class TeamFragment extends Fragment implements TeamContract.View, NavigationView.OnNavigationItemSelectedListener, View.OnClickListener,
		TeamContract.ItemClickListener, TeamContract.OnReadMessageCallback {

	public static final int NOTIFICATION = 0;
	public static final int BATTLE = 1;
	public static final int CHATTING = 2;

	@BindView(R.id.rv_team)
	RecyclerView rv_team;

	@BindView(R.id.layout_appbar)
	AppBarLayout layout_appbar;
	@BindView(R.id.layout_collapsing)
	CollapsingToolbarLayout layout_collapsing;

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

	@BindView(R.id.progressbar)
	ProgressBar progressBar;

	private TeamContract.Presenter mPresenter;

	private String mEnemy;

	private int[] imgs = {R.drawable.team_banner1, R.drawable.team_banner2, R.drawable.team_banner3,
			R.drawable.team_banner4, R.drawable.team_banner5, R.drawable.team_banner6, R.drawable.team_banner7,
			R.drawable.team_banner8, R.drawable.team_banner9, R.drawable.team_banner10};
	private int mPosition;

	private boolean isFinishedLoad = false;

	private List<Notification> notifications;
	private List<Notification> unReadNotifications;

	private User mUser;

	private List<Battle> mBattleList;

	private TeamAdapter adapter;

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

		ImageButton ib_remove = view.findViewById(R.id.ib_remove);
		ImageButton ib_cancel = view.findViewById(R.id.ib_cancel);
		ib_remove.setOnClickListener(this);
		ib_cancel.setOnClickListener(this);

		return view;
	}

	@Override
	public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		mPresenter.onLoadAccount();
		Glide.with(this).load(imgs[new Random().nextInt(imgs.length)]).diskCacheStrategy(DiskCacheStrategy.NONE).into(iv_collapsing);
		layout_collapsing.setContentScrimResource(R.drawable.background);
	}

	public static Fragment newInstance() {
		return new TeamFragment();
	}

	@Override
	public void showLoadingIndicator(boolean isShowing) {

	}

	@Override
	public void setToolbarTitle(User user) {
		this.mUser = user;
		layout_collapsing.setTitle(user.getTeam());
		mPresenter.onLoadRecord();
	}

	@Override
	public void showSuccessfullyLogout() {
		startActivity(new Intent(getContext(), LoginActivity.class));
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
			for (int i = 0; i < unReadNotifications.size(); i++) {
				if (unReadNotifications.get(i).getType().equals(TYPE_CHATTING)) {
					for (int j = 0; j < mBattleList.size(); j++) {
						if (unReadNotifications.get(i).getDate().equals(mBattleList.get(j).getDate())) {
							if (ChattingDialog.ROOM_NAME.equals("") && ChattingDialog.DAY.equals("")) {
								adapter.receivedMessage(j);
								break;
							}
						}
					}
				}
			}
		} else {
			BusProvider.getInstance().post(MainActivity.HIDE_BADGE);
		}
		fab.setCount(unReadNotifications.size());

		Collections.sort(notifications, new CompareNotification());
		rv_notification.setAdapter(new LoadAdapter(notifications, this));
		isFinishedLoad = true;
	}

	@Override
	public void showFailureLoadNotification() {

	}

	@OnClick(R.id.fab)
	public void onClickFab() {
		if (isFinishedLoad) {
			if (unReadNotifications.size() != 0) {
				fab.setCount(0);
				for (int i = 0; i < unReadNotifications.size(); i++) {
					if (!unReadNotifications.get(i).getType().equals(TYPE_CHATTING)) {
						unReadNotifications.get(i).setCheck(true);
					}
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
	}

	@Override
	public boolean onNavigationItemSelected(@NonNull MenuItem item) {
		navigation_drawer.closeDrawer(GravityCompat.END);
		return true;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.ib_remove:
				DialogUtils.showAlertDialog(getContext(), getString(R.string.team_want_remove), positiveListener, null);
				break;
			case R.id.ib_cancel:
				navigation_drawer.closeDrawer(GravityCompat.END);
				break;
		}
	}

	DialogInterface.OnClickListener positiveListener = new DialogInterface.OnClickListener() {
		@Override
		public void onClick(DialogInterface dialog, int which) {
			mPresenter.onRemoveNotification();
		}
	};

	@Override
	public void onReadMessageCallback() {
		adapter.readMessage(mPosition);
	}

	class CompareNotification implements Comparator<Notification> {
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

	class CompareBattle implements Comparator<Battle> {
		@Override
		public int compare(Battle o1, Battle o2) {
			int ret = 0;
			String date1 = o1.getDate();
			String date2 = o2.getDate();
			ret = date2.compareTo(date1);
			return ret;
		}
	}

	@Override
	public void onClick(int position, int type) {
		progressBar.setVisibility(View.VISIBLE);
		mPosition = position;
		if (type == NOTIFICATION) {
			int index = notifications.get(position).getMessage().indexOf("]");
			mEnemy = notifications.get(position).getMessage().substring(1, index);
			boolean isHome = false;
			if (notifications.get(position).getType().equals(TYPE_REQUEST)) {
				isHome = true;
			} else if (notifications.get(position).getType().equals(TYPE_RESPONSE)) {
				isHome = false;
			}

			mPresenter.onLoadMatch(isHome, notifications.get(position).getDate(), mEnemy, notifications.get(position).getFlag(), type);
		} else if (type == BATTLE) {
			for (int i = 0; i < unReadNotifications.size(); i++) {
				if (unReadNotifications.get(i).getDate().equals(mBattleList.get(position).getDate())) {
					unReadNotifications.get(i).setCheck(true);
				}
			}
			mPresenter.onReadNotification(unReadNotifications);
			mEnemy = mBattleList.get(position).getEnemy();
			mPresenter.onLoadMatch(mBattleList.get(position).isHome(), mBattleList.get(position).getDate(), mEnemy, mBattleList.get(position).getFlag(), type);
		} else if (type == CHATTING) {
			mPresenter.onLoadEnemyData(notifications.get(position).getSender(), notifications.get(position).getDate());
		}
	}

	@Override
	public void showSuccessfullyLoadInformation(Information information, String flag, int type) {
		progressBar.setVisibility(View.GONE);
		navigation_drawer.closeDrawer(GravityCompat.END);
		if (type != BATTLE) {
			if (flag.equals(String.valueOf(RemoteDataSource.FLAG_PLAYER_LIST))) {
				if (notifications.get(mPosition).getType().equals(TYPE_REQUEST)) {
					information.setPlace(notifications.get(mPosition).getUncertainPlace());
					information.setDate(notifications.get(mPosition).getUncertainDate());
					information.setTime(notifications.get(mPosition).getUncertainTime());
					information.setMoney(notifications.get(mPosition).getUncertainMoney());
				}
			}
		}
		adapter.readMessage(mPosition);
		NotificationDialog newFragment = NotificationDialog.newInstance(information, mEnemy, mUser.getTeam(), mUser.getUid(), flag);
		newFragment.show(getChildFragmentManager(), DIALOG_FRAGMENT);
		newFragment.setOnReadMessageCallback(this);
		UiThread.getInstance().postDelayed(new Runnable() {
			@Override
			public void run() {
				adapter.completedClick();
			}
		}, 1000);
	}

	@Override
	public void showSuccessfullyLoadBattle(List<Battle> battleList) {
		this.mBattleList = battleList;
		Collections.sort(battleList, new CompareBattle());
		adapter = new TeamAdapter(getContext(), battleList, mUser.getTeam(), this);
		rv_team.setAdapter(adapter);

		mPresenter.onLoadNotification();
	}

	@Override
	public void showUnsuccessfullyLoadBattle() {

	}

	@Override
	public void showInvalidAccount() {
		mPresenter.onLogout();
	}

	@Override
	public void showSuccessfullyRemoveNotification() {
		navigation_drawer.closeDrawer(GravityCompat.END);
		Toast.makeText(getContext(), getString(R.string.successfully_remove), Toast.LENGTH_SHORT).show();
	}

	@Override
	public void showUnsuccessfullyRemoveNotification() {
		Toast.makeText(getContext(), getString(R.string.unsuccessfully_remove), Toast.LENGTH_SHORT).show();
	}

	@Override
	public void showEnemyData(User enemy, String date) {
		progressBar.setVisibility(View.GONE);
		navigation_drawer.closeDrawer(GravityCompat.END);
		int position = 0;
		for (int i = 0; i < mBattleList.size(); i++) {
			if (mBattleList.get(i).getDate().equals(date)) {
				position = i;
				break;
			}
		}
		adapter.readMessage(position);
		DialogFragment newFragment = ChattingDialog.newInstance(enemy, mUser.getTeam(), enemy.getUid(), mUser.getUid(), date);
		newFragment.show(getChildFragmentManager(), DIALOG_FRAGMENT);
	}

}
