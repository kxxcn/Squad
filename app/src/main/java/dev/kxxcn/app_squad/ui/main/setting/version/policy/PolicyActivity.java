package dev.kxxcn.app_squad.ui.main.setting.version.policy;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dev.kxxcn.app_squad.R;
import dev.kxxcn.app_squad.util.TransitionUtils;

/**
 * Created by kxxcn on 2018-07-31.
 */
public class PolicyActivity extends AppCompatActivity {

	private static final String CHARSET = "UTF-8";

	@BindView(R.id.tv_policy)
	TextView tv_policy;

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_policy);
		TransitionUtils.fade(this);
		ButterKnife.bind(this);

		initUI();
	}

	private void initUI() {
		tv_policy.setText(getPolicyFile());
	}

	private String getPolicyFile() {
		String data = null;
		InputStream is = getResources().openRawResource(R.raw.policy);
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		int i;
		try {
			i = is.read();
			while (i != -1) {
				byteArrayOutputStream.write(i);
				i = is.read();
			}
			data = new String(byteArrayOutputStream.toByteArray(), CHARSET);
			is.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return data;
	}

	@OnClick(R.id.ib_back)
	public void onBack() {
		finish();
	}

}
