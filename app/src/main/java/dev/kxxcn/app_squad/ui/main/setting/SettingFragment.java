package dev.kxxcn.app_squad.ui.main.setting;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.unstoppable.submitbuttonview.SubmitButton;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dev.kxxcn.app_squad.R;
import dev.kxxcn.app_squad.data.DataRepository;
import dev.kxxcn.app_squad.data.remote.RemoteDataSource;
import dev.kxxcn.app_squad.ui.login.LoginActivity;

/**
 * Created by kxxcn on 2018-04-26.
 */
public class SettingFragment extends Fragment implements SettingContract.View {

	@BindView(R.id.btn_logout)
	SubmitButton btn_logout;

	private SettingContract.Presenter mPresenter;

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
	}

	@OnClick(R.id.btn_logout)
	public void onLogout() {
		mPresenter.onLogout();
	}

	public static Fragment newInstance() {
		return new SettingFragment();
	}

	@Override
	public void showLoadingIndicator(boolean isShowing) {

	}

	@Override
	public void showSuccessfullyLogout() {
		Toast.makeText(getContext(), getString(R.string.setting_success_logout), Toast.LENGTH_SHORT).show();
		getContext().startActivity(new Intent(getContext(), LoginActivity.class));
		getActivity().finish();
	}

	@Override
	public void showUnsuccessfullyLogout() {
		btn_logout.doResult(false);
		Toast.makeText(getContext(), getString(R.string.setting_failure_logout), Toast.LENGTH_SHORT).show();
	}

}
