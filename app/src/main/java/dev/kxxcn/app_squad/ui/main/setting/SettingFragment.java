package dev.kxxcn.app_squad.ui.main.setting;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.unstoppable.submitbuttonview.SubmitButton;
import com.zcw.togglebutton.ToggleButton;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dev.kxxcn.app_squad.R;
import dev.kxxcn.app_squad.data.DataRepository;
import dev.kxxcn.app_squad.data.model.Notice;
import dev.kxxcn.app_squad.data.model.User;
import dev.kxxcn.app_squad.data.remote.MyFirebaseInstanceIdService;
import dev.kxxcn.app_squad.data.remote.RemoteDataSource;
import dev.kxxcn.app_squad.ui.login.LoginActivity;
import dev.kxxcn.app_squad.util.Constants;
import dev.kxxcn.app_squad.util.DialogUtils;
import dev.kxxcn.app_squad.util.SystemUtils;

/**
 * Created by kxxcn on 2018-04-26.
 */
public class SettingFragment extends Fragment implements SettingContract.View {

	@BindView(R.id.btn_logout)
	SubmitButton btn_logout;

	@BindView(R.id.tb_squad)
	ToggleButton tb_squad;
	@BindView(R.id.tb_notice)
	ToggleButton tb_notice;
	@BindView(R.id.tb_event)
	ToggleButton tb_event;

	private SettingContract.Presenter mPresenter;

	private SharedPreferences preferences;

	@Override
	public void setPresenter(SettingContract.Presenter presenter) {
		this.mPresenter = presenter;
	}

	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_setting, container, false);
		ButterKnife.bind(this, view);

		new SettingPresenter(this, DataRepository.getInstance(RemoteDataSource.getInstance(
				FirebaseAuth.getInstance(), FirebaseDatabase.getInstance().getReference())));

		return view;
	}

	@Override
	public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		preferences = getContext().getSharedPreferences(MyFirebaseInstanceIdService.NAME, Context.MODE_PRIVATE);
		String newToken = preferences.getString(MyFirebaseInstanceIdService.KEY, null);
		SystemUtils.Dlog.i("Preferences Token : " + newToken);
		if (newToken != null) {
			mPresenter.onUpdateToken(newToken);
		} else {
			mPresenter.onLoadAccount();
		}
	}

	@OnClick(R.id.btn_logout)
	public void onLogout() {
		btn_logout.reset();
		DialogUtils.showAlertDialog(getContext(), getString(R.string.setting_want_logout), positiveListener, null);
	}

	public static Fragment newInstance() {
		return new SettingFragment();
	}

	DialogInterface.OnClickListener positiveListener = new DialogInterface.OnClickListener() {
		@Override
		public void onClick(DialogInterface dialog, int which) {
			mPresenter.onLogout();
		}
	};

	@Override
	public void showLoadingIndicator(boolean isShowing) {

	}

	@Override
	public void showSuccessfullyLogout() {
		getContext().startActivity(new Intent(getContext(), LoginActivity.class));
		getActivity().finish();
	}

	@Override
	public void showUnsuccessfullyLogout() {
		getContext().startActivity(new Intent(getContext(), LoginActivity.class));
		getActivity().finish();
	}

	@Override
	public void setToggleButton(User user) {
		Notice notice = user.getNotice();
		initToggle(tb_squad, notice.isSquad());
		tb_squad.setOnToggleChanged(onListenSqaud);
		initToggle(tb_notice, notice.isNotice());
		tb_notice.setOnToggleChanged(onListenNotice);
		initToggle(tb_event, notice.isEvent());
		tb_event.setOnToggleChanged(onListenEvent);
	}

	@Override
	public void showInvalidAccount() {
		mPresenter.onLogout();
	}

	@Override
	public void showSuccessfullyUpdateToken() {
		SharedPreferences.Editor editor = preferences.edit();
		editor.putString(MyFirebaseInstanceIdService.KEY, null);
		editor.apply();
		mPresenter.onLoadAccount();
	}

	@Override
	public void showUnsuccessfullyUpdateToken() {

	}

	private void initToggle(ToggleButton toggleButton, boolean isOn) {
		if (isOn) {
			toggleButton.setToggleOn();
		} else {
			toggleButton.setToggleOff();
		}
	}

	ToggleButton.OnToggleChanged onListenSqaud = new ToggleButton.OnToggleChanged() {
		@Override
		public void onToggle(boolean on) {
			mPresenter.onUpdateNotice(on, Constants.NoticeFilterType.SQAUD);
		}
	};

	ToggleButton.OnToggleChanged onListenNotice = new ToggleButton.OnToggleChanged() {
		@Override
		public void onToggle(boolean on) {
			mPresenter.onUpdateNotice(on, Constants.NoticeFilterType.NOTICE);
		}
	};

	ToggleButton.OnToggleChanged onListenEvent = new ToggleButton.OnToggleChanged() {
		@Override
		public void onToggle(boolean on) {
			mPresenter.onUpdateNotice(on, Constants.NoticeFilterType.EVENT);
		}
	};

}
