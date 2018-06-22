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
import me.relex.circleindicator.CircleIndicator;

/**
 * Created by kxxcn on 2018-06-22.
 */

public class NotificationDialog extends DialogFragment implements NotificationContract.View {

	private static final String INFORMATION = "object";
	private static final String ENEMY = "enemy";

	@BindView(R.id.vp_information)
	ViewPager vp_information;

	@BindView(R.id.indicator)
	CircleIndicator indicator;

	@BindView(R.id.tv_enemy)
	TextView tv_enemy;

	@BindView(R.id.btn_agree)
	SubmitButton btn_agree;

	private NotificationContract.Presenter mPresenter;

	private String mEnemy;

	@Override
	public void setPresenter(NotificationContract.Presenter presenter) {
		this.mPresenter = presenter;
	}

	public static NotificationDialog newInstance(Information information, String enemy) {
		NotificationDialog dialog = new NotificationDialog();

		Bundle args = new Bundle();
		args.putParcelable(INFORMATION, information);
		args.putString(ENEMY, enemy);

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

		mEnemy = getArguments().getString(ENEMY);

		mPresenter.onLoadEnemyData(mEnemy);

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
		mPresenter.onAgree();
	}

	@Override
	public void showLoadingIndicator(boolean isShowing) {

	}

	@Override
	public void showEnemyData(User user) {
		Information information = getArguments().getParcelable(INFORMATION);
		information.setEnemy(mEnemy);
		tv_enemy.setText(getString(R.string.notification_schedule));
		vp_information.setAdapter(new NotificationPagerAdapter(getChildFragmentManager(), information, user));
		indicator.setViewPager(vp_information);
	}

}
