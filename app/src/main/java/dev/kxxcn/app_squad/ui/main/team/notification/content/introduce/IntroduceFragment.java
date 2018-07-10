package dev.kxxcn.app_squad.ui.main.team.notification.content.introduce;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dev.kxxcn.app_squad.R;
import dev.kxxcn.app_squad.data.DataRepository;
import dev.kxxcn.app_squad.data.model.User;
import dev.kxxcn.app_squad.data.remote.RemoteDataSource;
import dev.kxxcn.app_squad.util.DialogUtils;

/**
 * Created by kxxcn on 2018-07-09.
 */

public class IntroduceFragment extends Fragment implements IntroduceContract.View {

	@BindView(R.id.tv_enemy)
	TextView tv_enemy;

	private static final String USER = "object";

	private IntroduceContract.Presenter mPresenter;

	private User mUser;

	public static IntroduceFragment newInstance(User user) {
		IntroduceFragment fragment = new IntroduceFragment();

		Bundle args = new Bundle();
		args.putParcelable(USER, user);

		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public void setPresenter(IntroduceContract.Presenter presenter) {
		this.mPresenter = presenter;
	}

	@Override
	public void showLoadingIndicator(boolean isShowing) {

	}

	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_introduce, container, false);
		ButterKnife.bind(this, view);

		new IntroducePresenter(this, DataRepository.getInstance(RemoteDataSource.getInstance(
				FirebaseAuth.getInstance(), FirebaseDatabase.getInstance().getReference())));

		initUI();

		return view;
	}

	private void initUI() {
		mUser = getArguments().getParcelable(USER);
		tv_enemy.setText(mUser.getTeam());
	}

	@OnClick({R.id.ll_call, R.id.ib_call})
	public void onCall() {
		DialogUtils.showAlertDialog(getContext(), getString(R.string.team_want_call), positiveListener, null);
	}

	@OnClick({R.id.ll_sms, R.id.ib_sms})
	public void onTransfer() {

	}

	DialogInterface.OnClickListener positiveListener = new DialogInterface.OnClickListener() {
		@Override
		public void onClick(DialogInterface dialog, int which) {
			Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse(String.format(getString(R.string.team_information_call), mUser.getPhone())));
			startActivity(intent);
		}
	};

}
