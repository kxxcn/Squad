package dev.kxxcn.app_squad.ui.main.match;

import android.app.Activity;
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
import dev.kxxcn.app_squad.util.KeyboardUtils;

import static dev.kxxcn.app_squad.util.Constants.FORMAT_CHARACTER;
import static dev.kxxcn.app_squad.util.Constants.FORMAT_LENGTH;
import static dev.kxxcn.app_squad.util.Constants.POSITION_SPINNER_DEFAULT;
import static dev.kxxcn.app_squad.util.Constants.TYPE_SORT;

/**
 * Created by kxxcn on 2018-05-16.
 */

public class MatchDialog extends Dialog implements MatchContract.View {

	private static final int MATCH = 0;
	private static final int RECRUITMENT = 1;
	private static final int PLAYER = 2;

	private MatchContract.Presenter mPresenter;

	@BindView(R.id.spinner_region)
	NiceSpinner spinner_region;
	@BindView(R.id.spinner_age)
	NiceSpinner spinner_age;
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
	@BindView(R.id.ll_age)
	LinearLayout ll_age;

	@BindView(R.id.tv_title)
	TextView tv_title;
	@BindView(R.id.tv_date)
	TextView tv_date;
	@BindView(R.id.tv_time)
	TextView tv_time;

	@BindView(R.id.et_place)
	EditText et_place;
	@BindView(R.id.et_money)
	EditText et_money;
	@BindView(R.id.et_inquiry)
	EditText et_inquiry;

	@BindView(R.id.ib_register)
	ImageButton ib_register;

	@BindView(R.id.progressbar)
	ProgressBar progressBar;

	private Activity mActivity;
	private Context mContext;

	private int mPosition;
	private int mStartHour, mEndHour;

	private boolean isEndTime;

	private String mStartTime, mEndTime;
	private String formatted;

	private DecimalFormat format = new DecimalFormat("#,###");

	private Constants.ListsFilterType mFilterType;

	private FirebaseAuth mAuth;

	@Override
	public void setPresenter(MatchContract.Presenter presenter) {
		mPresenter = presenter;
	}

	public MatchDialog(@NonNull Activity activity, @NonNull Context context, int position) {
		super(context);
		mActivity = activity;
		mContext = context;
		mPosition = position;
		isEndTime = true;
		mAuth = FirebaseAuth.getInstance();
		new MatchPresenter(this, DataRepository.getInstance(RemoteDataSource.getInstance(
				mAuth, FirebaseDatabase.getInstance().getReference())));
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
		String[] ages = getContext().getResources().getStringArray(R.array.ages);
		List<String> regionList = new LinkedList<>(Arrays.asList(regions));
		List<String> ruleList = new LinkedList<>(Arrays.asList(rules));
		List<String> ageList = new LinkedList<>(Arrays.asList(ages));
		spinner_region.attachDataSource(regionList);
		et_inquiry.addTextChangedListener(lineWatcher);
		switch (mPosition) {
			case MATCH:
				mFilterType = Constants.ListsFilterType.MATCH_LIST;
				tv_title.setText(mContext.getString(R.string.match_title_match));
				spinner_rule.attachDataSource(ruleList);
				spinner_age.attachDataSource(ageList);
				spinner_rule.setSelectedIndex(POSITION_SPINNER_DEFAULT);
				spinner_age.setSelectedIndex(POSITION_SPINNER_DEFAULT);
				et_money.addTextChangedListener(formatWatcher);
				break;
			case RECRUITMENT:
				mFilterType = Constants.ListsFilterType.RECRUITMENT_LIST;
				tv_title.setText(mContext.getString(R.string.match_title_recruitment));
				spinner_rule.attachDataSource(ruleList);
				spinner_rule.setSelectedIndex(POSITION_SPINNER_DEFAULT);
				ll_money.setVisibility(View.GONE);
				ll_age.setVisibility(View.GONE);
				break;
			case PLAYER:
				mFilterType = Constants.ListsFilterType.PLAYER_LIST;
				tv_title.setText(mContext.getString(R.string.match_title_player));
				ll_rule.setVisibility(View.GONE);
				ll_money.setVisibility(View.GONE);
				ll_place.setVisibility(View.GONE);
				ll_time.setVisibility(View.GONE);
				ll_age.setVisibility(View.GONE);
				break;
		}
	}

	@OnClick(R.id.ll_date)
	public void showDatePickerDialog() {
		KeyboardUtils.hideKeyboard(mActivity, getCurrentFocus());
		DialogUtils.showDatePickerDialog(mContext, dateSetListener);
	}

	@OnClick(R.id.ll_time)
	public void showTimePickerDialog() {
		KeyboardUtils.hideKeyboard(mActivity, getCurrentFocus());
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
		KeyboardUtils.hideKeyboard(mActivity, getCurrentFocus());
		String email = mAuth.getCurrentUser().getEmail();
		String region = spinner_region.getText().toString();
		String place = et_place.getText().toString();
		String date = tv_date.getText().toString();
		date = DialogUtils.getFormattedDate(date, TYPE_SORT);
		String time = tv_time.getText().toString();
		String money = et_money.getText().toString();
		String rule = spinner_rule.getText().toString();
		String age = spinner_age.getText().toString();
		String inquiry = et_inquiry.getText().toString();

		if (onVerifyUsability(ll_place.getVisibility(), place) && onVerifyUsability(ll_date.getVisibility(), date) &&
				onVerifyUsability(ll_time.getVisibility(), time) && onVerifyUsability(ll_money.getVisibility(), money)) {
			Dlog.i(String.format(getContext().getString(R.string.log_information),
					email, region, place, date, time, money, rule, age, inquiry));
			Information information = new Information(email, region, place, date, time, money, rule, age, inquiry, false);
			mPresenter.onRegister(information, mFilterType);
		} else {
			Toast.makeText(mContext, getContext().getString(R.string.input_all), Toast.LENGTH_SHORT).show();
		}
	}

	private boolean onVerifyUsability(int visibility, String str) {
		boolean noEmpty = false;
		if (visibility == View.VISIBLE) {
			if (!TextUtils.isEmpty(str)) {
				noEmpty = true;
			}
		} else {
			noEmpty = true;
		}
		return noEmpty;
	}

	private TimePickerDialog.OnTimeSetListener timeSetListener = new TimePickerDialog.OnTimeSetListener() {
		@Override
		public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
			isEndTime = !isEndTime;
			String formattedMin = onFormattedMinute(minute);
			if (isEndTime) {
				mEndHour = hourOfDay;
				if (onVerifyTime(mStartHour, mEndHour)) {
					mEndTime = String.format(mContext.getString(R.string.select_time), hourOfDay, formattedMin);
					tv_time.setText(String.format(mContext.getString(R.string.time), mStartTime, mEndTime));
				} else {
					tv_time.setText(null);
					Toast.makeText(mContext, mContext.getString(R.string.re_input_time), Toast.LENGTH_SHORT).show();
				}
			} else {
				mStartHour = hourOfDay;
				mStartTime = String.format(mContext.getString(R.string.select_time), hourOfDay, formattedMin);
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

	private String onFormattedMinute(int minute) {
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
		if (isShowing) {
			progressBar.setVisibility(View.VISIBLE);
		} else {
			progressBar.setVisibility(View.GONE);
		}
	}

	@Override
	public void showSuccessfullyRegister() {
		Toast.makeText(mActivity, getContext().getString(R.string.successfully_registration), Toast.LENGTH_SHORT).show();
		dismiss();
	}

	@Override
	public void showUnsuccessfullyRegister() {
		Toast.makeText(mActivity, getContext().getString(R.string.unsuccessfully_registration), Toast.LENGTH_SHORT).show();
	}

}
