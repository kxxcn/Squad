package dev.kxxcn.app_squad.ui.splash;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ProgressBar;

import com.github.ybq.android.spinkit.style.WanderingCubes;

import butterknife.BindView;
import butterknife.ButterKnife;
import dev.kxxcn.app_squad.R;
import dev.kxxcn.app_squad.ui.login.LoginActivity;
import dev.kxxcn.app_squad.util.threading.UiThread;

import static dev.kxxcn.app_squad.util.Constants.LOADING;

/**
 * Created by kxxcn on 2018-05-02.
 */

public class SplashActivity extends AppCompatActivity {

	@BindView(R.id.progressbar)
	ProgressBar progressbar;

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);
		ButterKnife.bind(this);

		appStart();
	}

	private void appStart() {
		WanderingCubes wanderingCubes = new WanderingCubes();
		progressbar.setIndeterminateDrawable(wanderingCubes);
		UiThread.getInstance().postDelayed(new Runnable() {
			@Override
			public void run() {
				startActivity(new Intent(SplashActivity.this, LoginActivity.class));
				finish();
			}
		}, LOADING);
	}

}
