package dev.kxxcn.app_squad.ui.main.match.fab.quick;

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
import android.widget.ProgressBar;
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
import dev.kxxcn.app_squad.ui.main.match.fab.FabContract;
import dev.kxxcn.app_squad.util.Constants;
import dev.kxxcn.app_squad.util.threading.UiThread;
import me.relex.circleindicator.CircleIndicator;

import static dev.kxxcn.app_squad.util.Constants.DISMISS_DIALOG;

/**
 * Created by kxxcn on 2018-08-03.
 */
public class QuickDialog extends DialogFragment implements QuickContract.View {

	private static final String INFORMATION = "object";
	private static final String ENEMY = "enemy";
	private static final String FROM = "from";
	private static final String UID = "uid";
	private static final String FLAG = "flag";

	@BindView(R.id.vp_information)
	ViewPager vp_information;

	@BindView(R.id.indicator)
	CircleIndicator indicator;

	@BindView(R.id.tv_title)
	TextView tv_title;

	@BindView(R.id.btn_request)
	SubmitButton btn_request;

	@BindView(R.id.progressbar)
	ProgressBar progressBar;

	private QuickContract.Presenter mPresenter;

	private User mUser;

	private Information mInformation;

	private FabContract.OnDialogDismissed mDialogDismissedCallback;

	public static QuickDialog newInstance(Information information, String enemy, String from, String uid, String flag) {
		QuickDialog dialog = new QuickDialog();

		Bundle args = new Bundle();
		args.putParcelable(INFORMATION, information);
		args.putString(ENEMY, enemy);
		args.putString(FROM, from);
		args.putString(UID, uid);
		args.putString(FLAG, flag);

		dialog.setArguments(args);
		return dialog;
	}


	@Override
	public void setPresenter(QuickContract.Presenter presenter) {
		this.mPresenter = presenter;
	}

	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.dialog_quick, container, false);
		ButterKnife.bind(this, view);
		getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

		new QuickPresenter(this, DataRepository.getInstance(RemoteDataSource.getInstance(
				FirebaseAuth.getInstance(), FirebaseDatabase.getInstance().getReference())));

		mPresenter.onLoadAccount();

		btn_request.setEnabled(false);

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

	@OnClick(R.id.btn_request)
	public void onRequest() {
		mPresenter.onRequest(mInformation.getEmail(), getString(R.string.app_name), String.format(getString(R.string.list_request_match),
				mUser.getTeam()), mUser.getUid(), mInformation.getDate().replace("-", ""), Constants.ListsFilterType.MATCH_LIST);
	}

	@Override
	public void showLoadingIndicator(boolean isShowing) {

	}

	@Override
	public void showSuccessfullyLoadAccount(User user) {
		this.mUser = user;
		mPresenter.onLoadEnemyData(getArguments().getString(ENEMY));
	}

	@Override
	public void showEnemyData(User user) {
		mInformation = getArguments().getParcelable(INFORMATION);
		mInformation.setEnemy(user.getTeam());
		if (getActivity() != null && isAdded()) {
			tv_title.setText(getString(R.string.quick_match));
			vp_information.setAdapter(new QuickPagerAdapter(getChildFragmentManager(), mInformation, user,
					getArguments().getString(FROM), getArguments().getString(UID)));
			indicator.setViewPager(vp_information);
		}
		btn_request.setEnabled(true);
		progressBar.setVisibility(View.GONE);
	}

	@Override
	public void showSuccessfullyRequested() {
		Toast.makeText(getContext(), getString(R.string.list_successfully_request_match), Toast.LENGTH_SHORT).show();
		btn_request.doResult(true);
		UiThread.getInstance().postDelayed(new Runnable() {
			@Override
			public void run() {
				mDialogDismissedCallback.onDialogDismissed();
				dismiss();
			}
		}, DISMISS_DIALOG);
	}

	@Override
	public void showUnsuccessfullyRequested() {
		Toast.makeText(getContext(), getString(R.string.list_unsuccessfully_request), Toast.LENGTH_SHORT).show();
	}

	public void setOnDialogDismissedListener(FabContract.OnDialogDismissed listener) {
		mDialogDismissedCallback = listener;
	}

}
