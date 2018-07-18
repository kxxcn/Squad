package dev.kxxcn.app_squad.ui.main.team.notification.content.introduce;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
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
import dev.kxxcn.app_squad.ui.main.team.notification.content.introduce.chat.ChattingDialog;
import dev.kxxcn.app_squad.util.DialogUtils;

import static dev.kxxcn.app_squad.util.Constants.DIALOG_FRAGMENT;

/**
 * Created by kxxcn on 2018-07-09.
 */

public class IntroduceFragment extends Fragment implements IntroduceContract.View {

	private static final String USER = "object";
	private static final String FROM = "from";
	private static final String UID = "uid";
	private static final String MATCH_DAY = "day";
	private static final String IS_CONNECT = "isConnect";

	@BindView(R.id.tv_enemy)
	TextView tv_enemy;

	@BindView(R.id.ll_call)
	LinearLayout ll_call;
	@BindView(R.id.ll_sms)
	LinearLayout ll_sms;

	private IntroduceContract.Presenter mPresenter;

	private User mEnemy;

	public static IntroduceFragment newInstance(User user, String from, String uid, String matchDay, boolean isConnect) {
		IntroduceFragment fragment = new IntroduceFragment();

		Bundle args = new Bundle();
		args.putParcelable(USER, user);
		args.putString(FROM, from);
		args.putString(UID, uid);
		args.putString(MATCH_DAY, matchDay);
		args.putBoolean(IS_CONNECT, isConnect);

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
		mEnemy = getArguments().getParcelable(USER);
		tv_enemy.setText(mEnemy.getTeam());
		if (getArguments().getBoolean(IS_CONNECT)) {
			ll_call.setVisibility(View.VISIBLE);
			ll_sms.setVisibility(View.VISIBLE);
		} else {
			ll_call.setVisibility(View.GONE);
			ll_sms.setVisibility(View.GONE);
		}
	}

	@OnClick({R.id.ll_call, R.id.ib_call})
	public void onCall() {
		DialogUtils.showAlertDialog(getContext(), getString(R.string.team_want_call), positiveListener, null);
	}

	@OnClick({R.id.ll_sms, R.id.ib_sms})
	public void onTransfer() {
		DialogFragment newFragment = ChattingDialog.newInstance(mEnemy, getArguments().getString(FROM), mEnemy.getUid(),
				getArguments().getString(UID), getArguments().getString(MATCH_DAY));
		newFragment.show(getChildFragmentManager(), DIALOG_FRAGMENT);
	}

	DialogInterface.OnClickListener positiveListener = new DialogInterface.OnClickListener() {
		@Override
		public void onClick(DialogInterface dialog, int which) {
			Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse(String.format(getString(R.string.team_information_call), mEnemy.getPhone())));
			startActivity(intent);
		}
	};

}
