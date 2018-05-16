package dev.kxxcn.app_squad.ui.main.match;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import org.angmarch.views.NiceSpinner;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dev.kxxcn.app_squad.R;
import dev.kxxcn.app_squad.util.DialogUtils;
import dev.kxxcn.app_squad.util.Dlog;

/**
 * Created by kxxcn on 2018-05-16.
 */

public class MatchDialog extends Dialog {

	private static final int MATCH = 0;
	private static final int RECRUITMENT = 1;
	private static final int PLAYER = 2;

	@BindView(R.id.spinner_region)
	NiceSpinner spinner_region;

	@BindView(R.id.ll_time)
	LinearLayout ll_time;

	@BindView(R.id.tv_title)
	TextView tv_title;
	@BindView(R.id.tv_date)
	TextView tv_date;
	@BindView(R.id.tv_time)
	TextView tv_time;

	private Context mContext;
	private int mType;

	private boolean isEndTime;

	private String mStartTime, mEndTime;
	private int mStartHour, mEndHour;

	public MatchDialog(@NonNull Context context, int type) {
		super(context);
		mContext = context;
		mType = type;
		isEndTime = true;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_match);
		ButterKnife.bind(this);
		initUI();
	}

	private void initUI() {
		switch (mType) {
			case MATCH:
				tv_title.setText(mContext.getString(R.string.match_title_match));
				break;
			case RECRUITMENT:
				tv_title.setText(mContext.getString(R.string.match_title_recruitment));
				break;
			case PLAYER:
				tv_title.setText(mContext.getString(R.string.match_title_player));
				break;
		}
		String[] regions = mContext.getResources().getStringArray(R.array.regions);
		List<String> regionList = new LinkedList<>(Arrays.asList(regions));
		spinner_region.attachDataSource(regionList);
	}

	@OnClick(R.id.ll_date)
	public void showDatePickerDialog() {
		DialogUtils.showDatePickerDialog(mContext, dateSetListener);
	}

	@OnClick(R.id.ll_time)
	public void showTimePickerDialog() {
		DialogUtils.showTimePickerDialog(mContext, timeSetListener);
	}

	private DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
		@Override
		public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
			tv_date.setText(String.format(mContext.getString(R.string.select_date),
					year, (monthOfYear + 1), dayOfMonth));
		}
	};

	private TimePickerDialog.OnTimeSetListener timeSetListener = new TimePickerDialog.OnTimeSetListener() {
		@Override
		public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
			isEndTime = !isEndTime;
			if (isEndTime) {
				mEndHour = hourOfDay;
				if (onVerifyTime(mStartHour, mEndHour)) {
					mEndTime = String.format(mContext.getString(R.string.select_time), hourOfDay, onFormattingTime(minute));
					Dlog.d(mStartTime + " ~ " + mEndTime);
					tv_time.setText(String.format(mContext.getString(R.string.time), mStartTime, mEndTime));
				} else {
					tv_time.setText(null);
					Toast.makeText(mContext, mContext.getString(R.string.re_input_time), Toast.LENGTH_SHORT).show();
				}
			} else {
				mStartHour = hourOfDay;
				mStartTime = String.format(mContext.getString(R.string.select_time), hourOfDay, onFormattingTime(minute));
				DialogUtils.showTimePickerDialog(mContext, timeSetListener);
			}
		}
	};

	private boolean onVerifyTime(int startHour, int endHour) {
		if (startHour < endHour) {
			return true;
		}
		return false;
	}

	private String onFormattingTime(int minute) {
		String min = String.valueOf(minute);
		if (min.equals("0")) {
			min = "00";
		}
		return min;
	}

}
