package dev.kxxcn.app_squad.ui.main.match;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import dev.kxxcn.app_squad.data.model.User;
import dev.kxxcn.app_squad.data.remote.RemoteDataSource;
import dev.kxxcn.app_squad.util.Constants;
import dev.kxxcn.app_squad.util.DialogUtils;
import dev.kxxcn.app_squad.util.KeyboardUtils;
import dev.kxxcn.app_squad.util.SystemUtils;

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
	public static final int MATCH_LIST = 4;
	public static final int RECRUITMENT_LIST = 5;
	public static final int PLAYER_LIST = 6;

	private static final int LIMITED_LINE_COUNT = 3;

	private MatchContract.Presenter mPresenter;

	@BindView(R.id.spinner_region)
	NiceSpinner spinner_region;
	@BindView(R.id.spinner_age)
	NiceSpinner spinner_age;
	@BindView(R.id.spinner_rule)
	NiceSpinner spinner_rule;
	@BindView(R.id.spinner_people)
	NiceSpinner spinner_people;

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
	@BindView(R.id.ll_people)
	LinearLayout ll_people;

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
	@BindView(R.id.ib_remove)
	ImageButton ib_remove;
	@BindView(R.id.ib_cancel)
	ImageButton ib_cancel;

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

	private FirebaseAuth mAuth;

	private Information mInformation;

	private boolean isUseable = true;

	private User mUser;

	private Constants.ListsFilterType mFilterType;

	@Override
	public void setPresenter(MatchContract.Presenter presenter) {
		mPresenter = presenter;
	}

	public MatchDialog(@NonNull Activity activity, @NonNull Context context, int position, Information information) {
		super(context);
		mActivity = activity;
		mContext = context;
		mPosition = position;
		mInformation = information;
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
		mPresenter.onLoadAccount();
	}

	private void initUI() {
		String[] regions = mContext.getResources().getStringArray(R.array.regions);
		String[] rules = getContext().getResources().getStringArray(R.array.group);
		String[] ages = getContext().getResources().getStringArray(R.array.ages);
		String[] people = getContext().getResources().getStringArray(R.array.people);
		List<String> regionList = new LinkedList<>(Arrays.asList(regions));
		List<String> ruleList = new LinkedList<>(Arrays.asList(rules));
		List<String> ageList = new LinkedList<>(Arrays.asList(ages));
		List<String> peopleList = new LinkedList<>(Arrays.asList(people));
		spinner_region.attachDataSource(regionList);
		et_inquiry.addTextChangedListener(lineWatcher);
		switch (mPosition) {
			case MATCH:
				mFilterType = Constants.ListsFilterType.MATCH_LIST;
				tv_title.setText(mContext.getString(R.string.match_title_match));
				spinner_rule.attachDataSource(ruleList);
				spinner_rule.setSelectedIndex(POSITION_SPINNER_DEFAULT);
				et_money.addTextChangedListener(formatWatcher);
				ll_age.setVisibility(View.GONE);
				ll_people.setVisibility(View.GONE);
				break;
			case RECRUITMENT:
				mFilterType = Constants.ListsFilterType.RECRUITMENT_LIST;
				tv_title.setText(mContext.getString(R.string.match_title_recruitment));
				spinner_rule.attachDataSource(ruleList);
				spinner_rule.setSelectedIndex(POSITION_SPINNER_DEFAULT);
				spinner_people.attachDataSource(peopleList);
				ll_age.setVisibility(View.GONE);
				break;
			case PLAYER:
				mFilterType = Constants.ListsFilterType.PLAYER_LIST;
				spinner_age.attachDataSource(ageList);
				spinner_age.setSelectedIndex(POSITION_SPINNER_DEFAULT);
				spinner_people.attachDataSource(peopleList);
				tv_title.setText(mContext.getString(R.string.match_title_player));
				ll_rule.setVisibility(View.GONE);
				ll_money.setVisibility(View.GONE);
				ll_place.setVisibility(View.GONE);
				break;
			case MATCH_LIST:
			case PLAYER_LIST:
			case RECRUITMENT_LIST:
				isUseable = false;
				tv_title.setText(mContext.getString(R.string.match_title_registration));
				spinner_region.setText(mInformation.getRegion());
				spinner_region.setEnabled(false);
				et_place.setText(mInformation.getPlace());
				et_place.setFocusable(false);
				et_place.setClickable(false);
				tv_date.setText(mInformation.getDate());
				tv_time.setText(mInformation.getTime());
				et_money.setText(mInformation.getMoney());
				et_money.setFocusable(false);
				et_money.setClickable(false);
				spinner_rule.setText(mInformation.getRule());
				spinner_rule.setEnabled(false);
				et_inquiry.setText(mInformation.getInquiry());
				et_inquiry.setFocusable(false);
				et_inquiry.setClickable(false);
				spinner_age.setText(mInformation.getAge());
				spinner_age.setEnabled(false);
				spinner_people.setText(mInformation.getPeople());
				spinner_people.setEnabled(false);
				ib_cancel.setVisibility(View.VISIBLE);
				if (mInformation.getEmail().equals(mUser.getEmail())) {
					ib_remove.setVisibility(View.VISIBLE);
				}
				ib_register.setVisibility(View.GONE);
				if (mPosition == MATCH_LIST) {
					mFilterType = Constants.ListsFilterType.MATCH_LIST;
					ll_age.setVisibility(View.GONE);
					ll_people.setVisibility(View.GONE);
				} else if (mPosition == RECRUITMENT_LIST) {
					mFilterType = Constants.ListsFilterType.RECRUITMENT_LIST;
					ll_age.setVisibility(View.GONE);
				} else if (mPosition == PLAYER_LIST) {
					mFilterType = Constants.ListsFilterType.PLAYER_LIST;
					ll_place.setVisibility(View.GONE);
					ll_money.setVisibility(View.GONE);
					ll_rule.setVisibility(View.GONE);
				}
				break;
		}
	}

	@OnClick(R.id.ll_date)
	public void showDatePickerDialog() {
		if (isUseable) {
			KeyboardUtils.hideKeyboard(mActivity, getCurrentFocus());
			DialogUtils.showDatePickerDialog(mContext, dateSetListener);
		}
	}

	@OnClick(R.id.ll_time)
	public void showTimePickerDialog() {
		if (isUseable) {
			KeyboardUtils.hideKeyboard(mActivity, getCurrentFocus());
			DialogUtils.showTimePickerDialog(mContext, timeSetListener, getContext().getString(R.string.input_start_time));
		}
	}

	@OnClick(R.id.ib_register)
	public void onRegister() {
		KeyboardUtils.hideKeyboard(mActivity, getCurrentFocus());
		String team = mUser.getTeam();
		String email = mUser.getEmail();
		String region = spinner_region.getText().toString();
		String place = et_place.getText().toString();
		String date = tv_date.getText().toString();
		if (!TextUtils.isEmpty(date)) {
			date = DialogUtils.getFormattedDate(date, TYPE_SORT);
		}
		String time = tv_time.getText().toString();
		String money = et_money.getText().toString();
		String rule = spinner_rule.getText().toString();
		String age = spinner_age.getText().toString();
		String inquiry = et_inquiry.getText().toString();
		String people = spinner_people.getText().toString();

		if (onVerifyUsability(ll_place, place) && onVerifyUsability(ll_date, date) &&
				onVerifyUsability(ll_time, time) && onVerifyUsability(ll_money, money) && onVerifyUsability(ll_people, people)) {
			SystemUtils.Dlog.i(String.format(getContext().getString(R.string.log_information),
					email, region, place, date, time, money, rule, age, inquiry));
			Information information = new Information(team, email, region, place, date, time, money, rule, age, inquiry, false, people);
			mPresenter.onRegister(information, mFilterType);
		} else {
			Toast.makeText(mContext, getContext().getString(R.string.input_all), Toast.LENGTH_SHORT).show();
		}
	}

	@OnClick(R.id.ib_remove)
	public void onRemove() {
		DialogUtils.showAlertDialog(getContext(), getContext().getString(R.string.want_to_remove), positiveListener, null);
	}

	private OnClickListener positiveListener = new OnClickListener() {
		@Override
		public void onClick(DialogInterface dialog, int which) {
			mPresenter.onRemove(mFilterType, mInformation.getDate().replace("-", ""));
		}
	};

	@OnClick(R.id.ib_cancel)
	public void onCancel() {
		dismiss();
	}

	private boolean onVerifyUsability(View view, String str) {
		boolean noEmpty = false;
		if (view.getVisibility() == View.VISIBLE) {
			if (!TextUtils.isEmpty(str)) {
				noEmpty = true;
			}
		} else {
			noEmpty = true;
		}
		return noEmpty;
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
				DialogUtils.showTimePickerDialog(mContext, timeSetListener, getContext().getString(R.string.input_end_time));
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
			if (et_inquiry.getLineCount() > LIMITED_LINE_COUNT) {
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

	@Override
	public void showSuccessfullyRemove() {
		Toast.makeText(mActivity, getContext().getString(R.string.successfully_remove), Toast.LENGTH_SHORT).show();
		dismiss();
	}

	@Override
	public void showUnsuccessfullyRemove() {
		Toast.makeText(mActivity, getContext().getString(R.string.unsuccessfully_remove), Toast.LENGTH_SHORT).show();
	}

	@Override
	public void showSuccessfullyLoadAccount(User user) {
		this.mUser = user;
		initUI();
	}

}
