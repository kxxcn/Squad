package dev.kxxcn.app_squad.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dev.kxxcn.app_squad.R;
import dev.kxxcn.app_squad.ui.main.MainActivity;
import dev.kxxcn.app_squad.util.EffectUtils;

import static dev.kxxcn.app_squad.util.Constants.CAMERA;

/**
 * Created by kxxcn on 2018-04-30.
 */

public class LoginActivity extends AppCompatActivity implements LoginContract.View {

	private LoginContract.Presenter mPresenter;

	@BindView(R.id.btn_login)
	TextView btn_login;

	@Override
	public void setPresenter(LoginContract.Presenter presenter) {
		mPresenter = presenter;
	}

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		ButterKnife.bind(this);

		new LoginPresenter(this);

		mPresenter.setPermission(this, CAMERA);
	}

	@OnClick(R.id.btn_login)
	public void onLogin() {
		startActivity(new Intent(LoginActivity.this, MainActivity.class));
		EffectUtils.fade(this);
		finish();
	}

}
