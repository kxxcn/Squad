package dev.kxxcn.app_squad.ui.main.setting.version;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dev.kxxcn.app_squad.R;
import dev.kxxcn.app_squad.data.DataRepository;
import dev.kxxcn.app_squad.data.remote.RemoteDataSource;
import dev.kxxcn.app_squad.ui.main.setting.version.license.LicenseActivity;
import dev.kxxcn.app_squad.util.TransitionUtils;
import dev.kxxcn.app_squad.util.threading.UiThread;

/**
 * Created by kxxcn on 2018-07-31.
 */
public class VersionActivity extends AppCompatActivity implements VersionContract.View {

	@BindView(R.id.tv_current_version)
	TextView tv_current_version;
	@BindView(R.id.tv_latest_version)
	TextView tv_latest_version;

	private VersionContract.Presenter mPresenter;

	@Override
	public void setPresenter(VersionContract.Presenter presenter) {
		this.mPresenter = presenter;
	}

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_version);
		TransitionUtils.fade(this);
		ButterKnife.bind(this);

		new VersionPresenter(this, DataRepository.getInstance(RemoteDataSource.getInstance(
				FirebaseAuth.getInstance(), FirebaseDatabase.getInstance().getReference())));

		initUI();
	}

	private void initUI() {
		mPresenter.onCheckVersion(getPackageName());
		try {
			tv_current_version.setText(String.format(getString(R.string.version_current), getPackageManager().getPackageInfo(getPackageName(), 0).versionName));
		} catch (PackageManager.NameNotFoundException e) {
			e.printStackTrace();
		}
	}

	@OnClick(R.id.ib_back)
	public void onBack() {
		onBackPressed();
	}

	@OnClick(R.id.ll_license)
	public void showAppLicense() {
		startActivity(new Intent(VersionActivity.this, LicenseActivity.class));
	}

	@Override
	public void showSuccessfulyCheckVersion(final String latestVersion) {
		UiThread.getInstance().post(new Runnable() {
			@Override
			public void run() {
				tv_latest_version.setText(String.format(getString(R.string.version_latest), latestVersion));
			}
		});
	}

	@Override
	public void showUnsuccessfulyCheckVersion() {
		UiThread.getInstance().post(new Runnable() {
			@Override
			public void run() {
				tv_latest_version.setText(getString(R.string.version_failure_latest));
			}
		});
	}

	@Override
	public void showLoadingIndicator(boolean isShowing) {

	}

}
