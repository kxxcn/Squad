package dev.kxxcn.app_squad.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dev.kxxcn.app_squad.R;
import dev.kxxcn.app_squad.data.DataRepository;
import dev.kxxcn.app_squad.data.remote.RemoteDataSource;
import dev.kxxcn.app_squad.ui.main.MainActivity;
import dev.kxxcn.app_squad.ui.signup.SignupActivity;
import dev.kxxcn.app_squad.util.Dlog;
import dev.kxxcn.app_squad.util.TransitionUtils;
import studio.carbonylgroup.textfieldboxes.ExtendedEditText;

import static dev.kxxcn.app_squad.util.Constants.CAMERA;

/**
 * Created by kxxcn on 2018-04-30.
 */

public class LoginActivity extends AppCompatActivity implements LoginContract.View {

	@BindView(R.id.et_email)
	ExtendedEditText et_email;
	@BindView(R.id.et_pass)
	ExtendedEditText et_pass;

	@BindView(R.id.progressbar)
	ProgressBar progressBar;

	private LoginContract.Presenter mPresenter;

	private FirebaseAuth mAuth;
	private FirebaseAuth.AuthStateListener mAuthListener;

	@Override
	public void setPresenter(LoginContract.Presenter presenter) {
		this.mPresenter = presenter;
	}

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		TransitionUtils.fade(this);
		ButterKnife.bind(this);

		mAuth = FirebaseAuth.getInstance();

		new LoginPresenter(this, DataRepository.getInstance(RemoteDataSource.getInstance(
				mAuth, FirebaseDatabase.getInstance().getReference())));

		mPresenter.setPermission(this, CAMERA);

		mAuthListener = new FirebaseAuth.AuthStateListener() {
			@Override
			public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
				FirebaseUser user = firebaseAuth.getCurrentUser();
				if (user != null) {
					Dlog.v("UID : " + user.getUid());
					startActivity(new Intent(LoginActivity.this, MainActivity.class));
					finish();
				}
			}
		};
	}

	@Override
	protected void onStart() {
		super.onStart();
		mAuth.addAuthStateListener(mAuthListener);
	}

	@Override
	protected void onStop() {
		super.onStop();
		if (mAuthListener != null) {
			mAuth.removeAuthStateListener(mAuthListener);
		}
	}

	@OnClick(R.id.btn_login)
	public void onLogin() {
		if (!TextUtils.isEmpty(et_email.getText()) && !TextUtils.isEmpty(et_pass.getText())) {
			mPresenter.login(et_email.getText().toString(), et_pass.getText().toString());
		} else {
			Toast.makeText(this, getString(R.string.input_information), Toast.LENGTH_SHORT).show();
		}
	}

	@OnClick(R.id.btn_signup)
	public void onSignup() {
		startActivity(new Intent(LoginActivity.this, SignupActivity.class));
	}

	@Override
	public void showLoadingIndicator(final boolean isShowing) {
		if (isShowing) {
			progressBar.setVisibility(View.VISIBLE);
		} else {
			progressBar.setVisibility(View.GONE);
		}
	}

	@Override
	public void showSuccessfullyAuth() {
		startActivity(new Intent(LoginActivity.this, MainActivity.class));
		finish();
	}

	@Override
	public void showUnsuccessfullyAuth() {
		Toast.makeText(this, getString(R.string.check_member_information), Toast.LENGTH_SHORT).show();
	}

}
