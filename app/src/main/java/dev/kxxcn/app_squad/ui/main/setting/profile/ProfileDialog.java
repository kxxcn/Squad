package dev.kxxcn.app_squad.ui.main.setting.profile;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
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
import dev.kxxcn.app_squad.data.model.User;
import dev.kxxcn.app_squad.data.remote.RemoteDataSource;
import dev.kxxcn.app_squad.util.KeyboardUtils;
import dev.kxxcn.app_squad.util.threading.UiThread;

/**
 * Created by kxxcn on 2018-08-01.
 */
public class ProfileDialog extends DialogFragment implements ProfileContract.View {

	private static final int LIMITED_LINE_COUNT = 3;

	@BindView(R.id.et_introduce)
	EditText et_introduce;

	@BindView(R.id.tv_team_name)
	TextView tv_team_name;

	@BindView(R.id.btn_save)
	SubmitButton btn_save;

	@BindView(R.id.progressbar)
	ProgressBar progressBar;

	private ProfileContract.Presenter mPresenter;

	private User mUser;

	public static ProfileDialog newInstance() {
		return new ProfileDialog();
	}

	@Override
	public void setPresenter(ProfileContract.Presenter presenter) {
		this.mPresenter = presenter;
	}

	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.dialog_profile, container, false);
		ButterKnife.bind(this, view);
		getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
		getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

		new ProfilePresenter(this, DataRepository.getInstance(RemoteDataSource.getInstance(
				FirebaseAuth.getInstance(), FirebaseDatabase.getInstance().getReference())));

		initUI();

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

	private void initUI() {
		mPresenter.onLoadAccount();
		et_introduce.addTextChangedListener(lineWatcher);
	}

	@OnClick(R.id.ib_cancel)
	public void onCancel() {
		dismiss();
	}

	@OnClick(R.id.btn_save)
	public void onSaveIntroduce() {
		if (!TextUtils.isEmpty(et_introduce.getText())) {
			KeyboardUtils.hideKeyboard(getActivity(), et_introduce);
			mUser.setIntroduce(et_introduce.getText().toString());
			mPresenter.onSaveIntroduce(mUser);
		} else {
			btn_save.reset();
			Toast.makeText(getContext(), getString(R.string.profile_introduce), Toast.LENGTH_SHORT).show();
		}
	}

	private TextWatcher lineWatcher = new TextWatcher() {

		private String previous;

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			previous = s.toString();
		}

		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {

		}

		@Override
		public void afterTextChanged(Editable s) {
			if (et_introduce.getLineCount() > LIMITED_LINE_COUNT) {
				et_introduce.setText(previous);
				et_introduce.setSelection(et_introduce.length());
			}
		}
	};

	@Override
	public void showLoadingIndicator(boolean isShowing) {
		if (isShowing) {
			progressBar.setVisibility(View.VISIBLE);
		} else {
			progressBar.setVisibility(View.GONE);
		}
	}

	@Override
	public void showSuccessfullyLoadAccount(User user) {
		this.mUser = user;
		tv_team_name.setText(user.getTeam());
		et_introduce.setText(user.getIntroduce());
	}

	@Override
	public void showUnsuccessfullyLoadAccount() {
		Toast.makeText(getContext(), getString(R.string.contact_administrator), Toast.LENGTH_SHORT).show();
		btn_save.setEnabled(false);
	}

	@Override
	public void showSuccessfullySaveIntroduce() {
		btn_save.doResult(true);
		UiThread.getInstance().postDelayed(new Runnable() {
			@Override
			public void run() {
				dismiss();
			}
		}, 1000);
	}

	@Override
	public void showUnsuccessfullySaveIntroduce() {
		btn_save.doResult(false);
		UiThread.getInstance().postDelayed(new Runnable() {
			@Override
			public void run() {
				btn_save.reset();
			}
		}, 1000);
	}

}
