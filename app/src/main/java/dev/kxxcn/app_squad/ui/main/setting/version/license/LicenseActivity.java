package dev.kxxcn.app_squad.ui.main.setting.version.license;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dev.kxxcn.app_squad.R;
import dev.kxxcn.app_squad.util.TransitionUtils;

/**
 * Created by kxxcn on 2018-07-31.
 */
public class LicenseActivity extends AppCompatActivity {

	@BindView(R.id.tv_license)
	TextView tv_license;

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_license);
		TransitionUtils.fade(this);
		ButterKnife.bind(this);

		initUI();
	}

	private void initUI() {
		tv_license.setText(getLicenseFile());
	}

	private String getLicenseFile() {
		Resources r = getResources();
		InputStream is = r.openRawResource(R.raw.license);
		InputStreamReader isr = new InputStreamReader(is);
		StringBuilder sb = new StringBuilder();
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		try {
			while (true) {
				int i = isr.read();
				if (i == -1) {
					break;
				} else {
					char c = (char) i;
					sb.append(c);
					os.write(c);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return os.toString();
	}

	@OnClick(R.id.ib_back)
	public void onBack() {
		onBackPressed();
	}

}
