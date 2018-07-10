package dev.kxxcn.app_squad.ui.main.team.notification;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.unstoppable.submitbuttonview.SubmitButton;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dev.kxxcn.app_squad.R;
import dev.kxxcn.app_squad.data.DataRepository;
import dev.kxxcn.app_squad.data.model.Information;
import dev.kxxcn.app_squad.data.model.User;
import dev.kxxcn.app_squad.data.remote.RemoteDataSource;
import dev.kxxcn.app_squad.util.StateButton;
import dev.kxxcn.app_squad.util.threading.UiThread;
import me.relex.circleindicator.CircleIndicator;

import static dev.kxxcn.app_squad.util.Constants.DISMISS_NOTI_DIALOG;

/**
 * Created by kxxcn on 2018-06-22.
 */

public class NotificationDialog extends DialogFragment implements NotificationContract.View {

	private static final String INFORMATION = "object";
	private static final String ENEMY = "enemy";
	private static final String FROM = "from";
	private static final String UID = "uid";

	@BindView(R.id.vp_information)
	ViewPager vp_information;

	@BindView(R.id.indicator)
	CircleIndicator indicator;

	@BindView(R.id.tv_enemy)
	TextView tv_enemy;

	@BindView(R.id.btn_agree)
	SubmitButton btn_agree;

	@BindView(R.id.btn_complete)
	StateButton btn_complete;

	private NotificationContract.Presenter mPresenter;

	private Information mInformation;

	@Override
	public void setPresenter(NotificationContract.Presenter presenter) {
		this.mPresenter = presenter;
	}

	public static NotificationDialog newInstance(Information information, String enemy, String from, String uid) {
		NotificationDialog dialog = new NotificationDialog();

		Bundle args = new Bundle();
		args.putParcelable(INFORMATION, information);
		args.putString(ENEMY, enemy);
		args.putString(FROM, from);
		args.putString(UID, uid);

		dialog.setArguments(args);
		return dialog;
	}

	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.dialog_team, container, false);
		ButterKnife.bind(this, view);
		getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

		new NotificationPresenter(this, DataRepository.getInstance(RemoteDataSource.getInstance(
				FirebaseAuth.getInstance(), FirebaseDatabase.getInstance().getReference())));

		mPresenter.onLoadEnemyData(getArguments().getString(ENEMY));

		return view;
	}

	@Override
	public void onStart() {
		super.onStart();
		Dialog dialog = getDialog();
		if (dialog != null) {
			int width = ViewGroup.LayoutParams.MATCH_PARENT;
			int height = ViewGroup.LayoutParams.MATCH_PARENT;
			dialog.getWindow().setLayout(width, height);
		}
	}

	@OnClick(R.id.ib_cancel)
	public void onCancel() {
		dismiss();
	}

	@OnClick(R.id.btn_agree)
	public void onAgree() {
		mPresenter.onAgree(mInformation, getString(R.string.app_name), String.format(getString(R.string.notification_agree), mInformation.getTeam()));
	}

	@Override
	public void showLoadingIndicator(boolean isShowing) {

	}

	@Override
	public void showEnemyData(User user) {
		mInformation = getArguments().getParcelable(INFORMATION);
		mInformation.setEnemy(user.getTeam());
		tv_enemy.setText(getString(R.string.notification_schedule));
		vp_information.setAdapter(new NotificationPagerAdapter(getChildFragmentManager(), mInformation, user,
				getArguments().getString(FROM), getArguments().getString(UID)));
		indicator.setViewPager(vp_information);
		if (mInformation.isConnect()) {
			btn_agree.setVisibility(View.GONE);
			btn_complete.setVisibility(View.VISIBLE);
		} else {
			btn_agree.setVisibility(View.VISIBLE);
			btn_complete.setVisibility(View.GONE);
		}
	}

	@Override
	public void showSuccessfullyAgree() {
		btn_agree.doResult(true);
		UiThread.getInstance().postDelayed(new Runnable() {
			@Override
			public void run() {
				dismiss();
			}
		}, DISMISS_NOTI_DIALOG);
	}

	@Override
	public void showUnsuccessfullyAgree() {
		btn_agree.doResult(false);
		Toast.makeText(getContext(), getString(R.string.notification_unsuccessfully_agree), Toast.LENGTH_SHORT).show();
	}

}
