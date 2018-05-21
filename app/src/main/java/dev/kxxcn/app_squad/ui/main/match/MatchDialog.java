package dev.kxxcn.app_squad.ui.main.match;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import org.angmarch.views.NiceSpinner;

import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dev.kxxcn.app_squad.R;
import dev.kxxcn.app_squad.data.DataRepository;
import dev.kxxcn.app_squad.data.model.Information;
import dev.kxxcn.app_squad.data.remote.RemoteDataSource;
import dev.kxxcn.app_squad.util.Constants;
import dev.kxxcn.app_squad.util.DialogUtils;
import dev.kxxcn.app_squad.util.Dlog;

import static dev.kxxcn.app_squad.util.Constants.POSITION_SPINNER_DEFAULT;

/**
 * Created by kxxcn on 2018-05-16.
 */

public class MatchDialog extends Dialog implements MatchContract.View {

	private static final int MATCH = 0;
	private static final int RECRUITMENT = 1;
	private static final int PLAYER = 2;

	private static final int FORMAT_LENGTH = 1;
	private static final String FORMAT_CHARACTER = "0";

	private MatchContract.Presenter mPresenter;

	@BindView(R.id.spinner_region)
	NiceSpinner spinner_region;
	@BindView(R.id.spinner_rule)
	NiceSpinner spinner_rule;

	@BindView(R.id.ll_region)
	LinearLayout ll_region;
	@BindView(R.id.ll_place)
	LinearLayout ll_place;
	@BindView(R.id.ll_date)
	LinearLayout ll_date;
	@BindView(R.id.ll_time)
	LinearLayout ll_time;
	@BindView(R.id.ll_money)
	LinearLayout ll_money;
	@BindView(R.id.ll_rule)
	LinearLayout ll_rule;

	@BindView(R.id.tv_title)
	TextView tv_title;
	@BindView(R.id.tv_date)
	TextView tv_date;
	@BindView(R.id.tv_time)
	TextView tv_time;

	@BindView(R.id.et_money)
	EditText et_money;
	@BindView(R.id.et_inquiry)
	EditText et_inquiry;

	@BindView(R.id.ib_register)
	ImageButton ib_register;

	@BindView(R.id.progressbar)
	ProgressBar progressBar;

	private Context mContext;

	private int mPosition;
	private int mStartHour, mEndHour;

	private boolean isEndTime;

	private String mStartTime, mEndTime;
	private String formatted;

	private DecimalFormat format = new DecimalFormat("#,###");

	private Constants.ListsFilterType mFilterType;

	@Override
	public void setPresenter(MatchContract.Presenter presenter) {
		mPresenter = presenter;
	}

	public MatchDialog(@NonNull Context context, int position) {
		super(context);
		mContext = context;
		mPosition = position;
		isEndTime = true;
		new MatchPresenter(this, DataRepository.getInstance(RemoteDataSource.getInstance(
				FirebaseAuth.getInstance(), FirebaseDatabase.getInstance().getReference())));
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_match);
		ButterKnife.bind(this);
		initUI();
	}

	private void initUI() {
		String[] regions = mContext.getResources().getStringArray(R.array.regions);
		String[] rules = getContext().getResources().getStringArray(R.array.group);
		List<String> regionList = new LinkedList<>(Arrays.asList(regions));
		List<String> ruleList = new LinkedList<>(Arrays.asList(rules));
		spinner_region.attachDataSource(regionList);
		et_inquiry.addTextChangedListener(lineWatcher);
		switch (mPosition) {
			case MATCH:
				mFilterType = Constants.ListsFilterType.MATCH_LIST;
				tv_title.setText(mContext.getString(R.string.match_title_match));
				spinner_rule.attachDataSource(ruleList);
				spinner_rule.setSelectedIndex(POSITION_SPINNER_DEFAULT);
				et_money.addTextChangedListener(formatWatcher);
				break;
			case RECRUITMENT:
				mFilterType = Constants.ListsFilterType.RECRUITMENT_LIST;
				tv_title.setText(mContext.getString(R.string.match_title_recruitment));
				spinner_rule.attachDataSource(ruleList);
				spinner_rule.setSelectedIndex(POSITION_SPINNER_DEFAULT);
				ll_money.setVisibility(View.GONE);
				break;
			case PLAYER:
				mFilterType = Constants.ListsFilterType.PLAYER_LIST;
				tv_title.setText(mContext.getString(R.string.match_title_player));
				ll_rule.setVisibility(View.GONE);
				ll_money.setVisibility(View.GONE);
				ll_place.setVisibility(View.GONE);
				break;
		}
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

	@OnClick(R.id.ib_register)
	public void onRegister() {
		progressBar.setVisibility(View.VISIBLE);
		Information information = new Information();
		mPresenter.onRegister(information, mFilterType);
	}

	private TimePickerDialog.OnTimeSetListener timeSetListener = new TimePickerDialog.OnTimeSetListener() {
		@Override
		public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
			isEndTime = !isEndTime;
			if (isEndTime) {
				mEndHour = hourOfDay;
				if (onVerifyTime(mStartHour, mEndHour)) {
					mEndTime = String.format(mContext.getString(R.string.select_time), hourOfDay, onFormattingMinute(minute));
					Dlog.d(mStartTime + " ~ " + mEndTime);
					tv_time.setText(String.format(mContext.getString(R.string.time), mStartTime, mEndTime));
				} else {
					tv_time.setText(null);
					Toast.makeText(mContext, mContext.getString(R.string.re_input_time), Toast.LENGTH_SHORT).show();
				}
			} else {
				mStartHour = hourOfDay;
				mStartTime = String.format(mContext.getString(R.string.select_time), hourOfDay, onFormattingMinute(minute));
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

	private String onFormattingMinute(int minute) {
		String format = String.valueOf(minute);
		if (format.length() == FORMAT_LENGTH) {
			format = FORMAT_CHARACTER + format;
		}
		return format;
	}

	private TextWatcher formatWatcher = new TextWatcher() {
		@Override
		public void beforeTextChanged(CharSequence s, int start, int count, int after) {

		}

		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {
			if (!TextUtils.isEmpty(s.toString()) && !s.toString().equals(formatted)) {
				formatted = format.format(Double.parseDouble(s.toString().replace(",", "")));
				et_money.setText(formatted);
				et_money.setSelection(formatted.length());
			}
		}

		@Override
		public void afterTextChanged(Editable s) {

		}
	};

	private TextWatcher lineWatcher = new TextWatcher() {

		String previous;

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			previous = s.toString();
		}

		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {

		}

		@Override
		public void afterTextChanged(Editable s) {
			if (et_inquiry.getLineCount() > 3) {
				et_inquiry.setText(previous);
				et_inquiry.setSelection(et_inquiry.length());
			}
		}
	};

	@Override
	public void showLoadingIndicator(boolean isShowing) {

	}

}
