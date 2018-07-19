package dev.kxxcn.app_squad.ui.main.team.notification.content.schedule;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.DecimalFormat;

import butterknife.BindView;
import butterknife.ButterKnife;
import dev.kxxcn.app_squad.R;
import dev.kxxcn.app_squad.data.model.Information;

/**
 * Created by kxxcn on 2018-07-09.
 */

public class ScheduleFragment extends Fragment {

	@BindView(R.id.tv_place)
	TextView tv_place;
	@BindView(R.id.tv_date)
	TextView tv_date;
	@BindView(R.id.tv_time)
	TextView tv_time;
	@BindView(R.id.tv_money)
	TextView tv_money;

	private static final String INFORMATION = "object";

	public static ScheduleFragment newInstance(Information information) {
		ScheduleFragment fragment = new ScheduleFragment();

		Bundle args = new Bundle();
		args.putParcelable(INFORMATION, information);

		fragment.setArguments(args);
		return fragment;
	}

	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_schedule, container, false);
		ButterKnife.bind(this, view);
		initUI();
		return view;
	}

	private void initUI() {
		Information information = getArguments().getParcelable(INFORMATION);
		tv_money.addTextChangedListener(formatWatcher);
		tv_place.setText(information.getPlace());
		tv_date.setText(information.getDate());
		tv_time.setText(information.getTime());
		tv_money.setText(information.getMoney());
	}

	private TextWatcher formatWatcher = new TextWatcher() {

		String formatted;
		DecimalFormat format = new DecimalFormat("#,###");

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count, int after) {

		}

		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {
			if (!TextUtils.isEmpty(s.toString()) && !s.toString().equals(formatted)) {
				formatted = format.format(Double.parseDouble(s.toString().replace(",", "")));
				tv_money.setText(formatted);
			}
		}

		@Override
		public void afterTextChanged(Editable s) {

		}
	};

}
