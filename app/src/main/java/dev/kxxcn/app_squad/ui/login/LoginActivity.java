package dev.kxxcn.app_squad.ui.login;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import static dev.kxxcn.app_squad.util.Constants.CAMERA;

/**
 * Created by kxxcn on 2018-04-30.
 */

public class LoginActivity extends AppCompatActivity implements LoginContract.View {

	LoginContract.Presenter mPresenter;

	@Override
	public void setPresenter(LoginContract.Presenter presenter) {
		mPresenter = presenter;
	}

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		new LoginPresenter(this);

		mPresenter.setPermission(this, CAMERA);
	}

}
