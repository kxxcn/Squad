package dev.kxxcn.app_squad.ui.signup;

import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dev.kxxcn.app_squad.R;
import dev.kxxcn.app_squad.data.DataRepository;
import dev.kxxcn.app_squad.data.remote.RemoteDataSource;
import dev.kxxcn.app_squad.util.StateButton;
import dev.kxxcn.app_squad.util.SystemUtils;
import dev.kxxcn.app_squad.util.TransitionUtils;
import dev.kxxcn.app_squad.util.WatcherUtils;
import studio.carbonylgroup.textfieldboxes.ExtendedEditText;


/**
 * Created by kxxcn on 2018-05-04.
 */

public class SignupActivity extends AppCompatActivity implements SignupContract.View {

	public static final int ONE_CHARACTER = 1;
	public static final int LIMITED_MAX_CHARACTER = 10;
	public static final int LIMITED_MIN_CHARACTER = 6;

	@BindView(R.id.et_email)
	ExtendedEditText et_email;
	@BindView(R.id.et_team)
	ExtendedEditText et_team;
	@BindView(R.id.et_contact)
	ExtendedEditText et_contact;
	@BindView(R.id.et_pass)
	ExtendedEditText et_pass;
	@BindView(R.id.et_confirm)
	ExtendedEditText et_confirm;

	@BindView(R.id.btn_signup)
	StateButton btn_signup;

	@BindView(R.id.ll_rootview)
	LinearLayout ll_rootview;
	@BindView(R.id.ll_top)
	LinearLayout ll_top;
	@BindView(R.id.ll_middle)
	LinearLayout ll_middle;

	@BindView(R.id.progressbar)
	ProgressBar progressBar;

	private SignupContract.Presenter mPresenter;

	private ViewTreeObserver.OnGlobalLayoutListener mGlobalListener;

	@Override
	public void setPresenter(SignupContract.Presenter presenter) {
		mPresenter = presenter;
	}

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_signup);
		TransitionUtils.fade(this);
		ButterKnife.bind(this);

		new SignupPresenter(this, DataRepository.getInstance(RemoteDataSource.getInstance(
				FirebaseAuth.getInstance(), FirebaseDatabase.getInstance().getReference())));

		et_email.addTextChangedListener(WatcherUtils.noSpaceWatcher(et_email));
		et_team.addTextChangedListener(WatcherUtils.noSpaceWatcher(et_team));
		et_confirm.addTextChangedListener(validatePassWatcher);

		initUI();
	}

	private void initUI() {
		registerShowAndHideView(ll_rootview, ll_top, ll_middle);
		try {
			TelephonyManager manager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
			String contact = manager.getLine1Number();
			contact = contact.replace("+82", "0");
			et_contact.setText(contact);
		} catch (SecurityException e) {
			SystemUtils.Dlog.e(e.getMessage());
			e.printStackTrace();
		}
	}

	@OnClick(R.id.btn_signup)
	public void onSignup() {
		if (!TextUtils.isEmpty(et_email.getText()) && !TextUtils.isEmpty(et_team.getText()) &&
				!TextUtils.isEmpty(et_contact.getText()) && !TextUtils.isEmpty(et_pass.getText()) && !TextUtils.isEmpty(et_confirm.getText())) {
			if (et_team.getText().length() <= LIMITED_MAX_CHARACTER) {
				if (et_confirm.getText().length() >= LIMITED_MIN_CHARACTER) {
					mPresenter.signup(et_email.getText().toString(), et_contact.getText().toString(),
							et_confirm.getText().toString(), et_team.getText().toString());
				} else {
					Toast.makeText(this, getString(R.string.password_rule), Toast.LENGTH_SHORT).show();
				}
			} else {
				Toast.makeText(this, getString(R.string.exceed_characters), Toast.LENGTH_SHORT).show();
			}
		} else {
			Toast.makeText(this, getString(R.string.input_all), Toast.LENGTH_SHORT).show();
		}
	}

	TextWatcher validatePassWatcher = new TextWatcher() {
		@Override
		public void beforeTextChanged(CharSequence s, int start, int count, int after) {

		}

		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {
			onPasswordValidation();
		}

		@Override
		public void afterTextChanged(Editable s) {

		}
	};

	/**
	 * 패스워드 검증
	 *
	 * @author kxxcn
	 * @since 2018-05-04 오후 2:37
	 */
	private void onPasswordValidation() {
		if (TextUtils.isEmpty(et_pass.getText())) {
			if (et_confirm.getText().length() == ONE_CHARACTER) {
				et_confirm.setText(null);
				Toast.makeText(SignupActivity.this, getString(R.string.input_pass), Toast.LENGTH_SHORT).show();
			}
		} else {
			if (!et_confirm.getText().toString().equals(et_pass.getText().toString())) {
				et_confirm.setError(getString(R.string.not_match_password));
				btn_signup.setEnabled(false);
			} else {
				et_confirm.setError(null);
				btn_signup.setEnabled(true);
			}
		}
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
	public void showSuccessfullyRegister() {
		Toast.makeText(SignupActivity.this, getString(R.string.successfully_signup), Toast.LENGTH_SHORT).show();
		finish();
	}

	@Override
	public void showUnsuccessfullyRegister() {
		Toast.makeText(SignupActivity.this, getString(R.string.failure_register), Toast.LENGTH_SHORT).show();
	}

	@Override
	public void showAlreadyExistAccount() {
		Toast.makeText(this, getString(R.string.exist_account), Toast.LENGTH_SHORT).show();
	}

	@Override
	public void showAlreadyExistTeam() {
		Toast.makeText(this, getString(R.string.exist_team), Toast.LENGTH_SHORT).show();
	}

	@Override
	public void showBadlyFormatted() {
		Toast.makeText(this, getString(R.string.badly_formatted), Toast.LENGTH_SHORT).show();
	}

	public void registerShowAndHideView(final View rootView, final View top, final LinearLayout middle) {
		if (mGlobalListener == null) {
			mGlobalListener = new ViewTreeObserver.OnGlobalLayoutListener() {
				@Override
				public void onGlobalLayout() {
					Rect r = new Rect();
					// 해당 루트뷰에서 윈도우가 보이는 영역을 얻어옴
					rootView.getWindowVisibleDisplayFrame(r);

					// 루트뷰의 실제 높이와, 윈도우 영역의 높이를 비교
					// 키보드는 윈도우 영역에 위치하므로 뷰와 윈도우의 높이비교를 통해 키보드의 여부를 알 수 있다.
					int heightDiff = rootView.getRootView().getHeight() - (r.bottom - r.top);

					// Status Bar 높이 구하기
					Rect CheckRect = new Rect();
					Window window = getWindow();
					window.getDecorView().getWindowVisibleDisplayFrame(CheckRect);
					int statusBarHeight = CheckRect.top;

					int keyboardThreshold = statusBarHeight + getSoftButtonsBarHeight();

					// keyboardThreshold는 윈도우가 기본적으로 차지하고있는 영역(StatusBar / Soft Back Button)
					int keyboardHeight = heightDiff - keyboardThreshold;
					if (keyboardHeight != 0) {
						top.setVisibility(View.GONE);
						middle.setGravity(Gravity.CENTER_HORIZONTAL);
					} else {
						top.setVisibility(View.VISIBLE);
						middle.setGravity(Gravity.CENTER);
					}
				}
			};
		}
		rootView.getViewTreeObserver().addOnGlobalLayoutListener(mGlobalListener);
	}

	private int getSoftButtonsBarHeight() {
		/* getRealMetrics is only available with API 17 and + */
		DisplayMetrics metrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metrics);
		int usableHeight = metrics.heightPixels;
		getWindowManager().getDefaultDisplay().getRealMetrics(metrics);
		int realHeight = metrics.heightPixels;
		if (realHeight > usableHeight)
			return realHeight - usableHeight;
		else
			return 0;
	}

}
