package dev.kxxcn.app_squad.ui.main.setting.version.clause;

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
public class ClauseActivity extends AppCompatActivity {

	private static final String CHARSET = "EUC-KR";

	@BindView(R.id.tv_clause)
	TextView tv_clause;

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_clause);
		TransitionUtils.fade(this);
		ButterKnife.bind(this);

		initUI();
	}

	private void initUI() {
		tv_clause.setText(getClauseFile());
	}

	private String getClauseFile() {
		String data = null;
		InputStream is = getResources().openRawResource(R.raw.clause);
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
