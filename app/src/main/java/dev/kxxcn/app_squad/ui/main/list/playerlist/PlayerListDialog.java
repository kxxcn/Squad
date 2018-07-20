package dev.kxxcn.app_squad.ui.main.list.playerlist;

import android.app.TimePickerDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.unstoppable.submitbuttonview.SubmitButton;

import java.text.DecimalFormat;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dev.kxxcn.app_squad.R;
import dev.kxxcn.app_squad.ui.main.list.ListContract;
import dev.kxxcn.app_squad.util.DialogUtils;
import dev.kxxcn.app_squad.util.KeyboardUtils;

import static dev.kxxcn.app_squad.util.Constants.FORMAT_CHARACTER;
import static dev.kxxcn.app_squad.util.Constants.FORMAT_LENGTH;

/**
 * Created by kxxcn on 2018-07-19.
 */

public class PlayerListDialog extends DialogFragment {

	@BindView(R.id.et_place)
	EditText et_place;
	@BindView(R.id.tv_date)
	TextView tv_date;
	@BindView(R.id.tv_time)
	TextView tv_time;
	@BindView(R.id.et_money)
	EditText et_money;

	@BindView(R.id.btn_request)
	SubmitButton btn_request;

	@BindView(R.id.tv_title)
	TextView tv_title;

	@BindView(R.id.ib_cancel)
	ImageButton ib_cancel;

	private static final String DATE = "date";
	private static final String LISTENER = "listener";

	private ListContract.OnDialogRequested mDialogRequestedCallback;

	private boolean isEndTime;

	private int mStartHour, mEndHour;

	private String mStartTime, mEndTime;

	public static PlayerListDialog newInstance(String date) {
		PlayerListDialog dialog = new PlayerListDialog();

		Bundle args = new Bundle();
		args.putString(DATE, date);

		dialog.setArguments(args);
		return dialog;
	}

	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.dialog_player, container, false);
		ButterKnife.bind(this, view);
		getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

		isEndTime = true;

		initUI();

		return view;
	}

	private void initUI() {
		tv_title.setText(getString(R.string.player_title));
		tv_date.setText(getArguments().getString(DATE));
		et_money.addTextChangedListener(formatWatcher);
	}

	@OnClick(R.id.btn_request)
	public void onRequest() {
		if (!TextUtils.isEmpty(et_place.getText()) && !TextUtils.isEmpty(tv_time.getText()) && !TextUtils.isEmpty(et_money.getText())) {
			btn_request.reset();
			mDialogRequestedCallback.onDialogRequested(et_place.getText().toString(), tv_date.getText().toString(),
					tv_time.getText().toString(), et_money.getText().toString());
			dismiss();
		} else {
			btn_request.reset();
			Toast.makeText(getContext(), getString(R.string.input_all), Toast.LENGTH_SHORT).show();
		}
	}

	@OnClick({R.id.tv_time, R.id.ll_time})
	public void showTimePickerDialog() {
		KeyboardUtils.hideKeyboard(getActivity(), tv_time);
		DialogUtils.showTimePickerDialog(getContext(), timeSetListener, getContext().getString(R.string.input_start_time));
	}

	@OnClick(R.id.ib_cancel)
	public void onCancel() {
		dismiss();
	}

	public void setOnDialogRequestedListener(ListContract.OnDialogRequested listener) {
		this.mDialogRequestedCallback = listener;
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
				et_money.setText(formatted);
				et_money.setSelection(et_money.getText().length());
			}
		}

		@Override
		public void afterTextChanged(Editable s) {

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
					mEndTime = String.format(getContext().getString(R.string.select_time), hourOfDay, formattedMin);
					tv_time.setText(String.format(getString(R.string.time), mStartTime, mEndTime));
				} else {
					tv_time.setText(null);
					Toast.makeText(getContext(), getString(R.string.re_input_time), Toast.LENGTH_SHORT).show();
				}
			} else {
				mStartHour = hourOfDay;
				mStartTime = String.format(getString(R.string.select_time), hourOfDay, formattedMin);
				DialogUtils.showTimePickerDialog(getContext(), timeSetListener, getContext().getString(R.string.input_end_time));
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

}
