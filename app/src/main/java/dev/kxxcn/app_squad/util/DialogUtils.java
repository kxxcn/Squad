package dev.kxxcn.app_squad.util;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Build;
import android.view.ContextThemeWrapper;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import static dev.kxxcn.app_squad.util.Constants.FORMAT_CHARACTER;
import static dev.kxxcn.app_squad.util.Constants.FORMAT_DATE;
import static dev.kxxcn.app_squad.util.Constants.FORMAT_LENGTH;
import static dev.kxxcn.app_squad.util.Constants.TYPE_COLLECTION;
import static dev.kxxcn.app_squad.util.Constants.TYPE_SORT;

/**
 * Created by kxxcn on 2018-05-16.
 */

public class DialogUtils {
	public static void showDatePickerDialog(Context context, DatePickerDialog.OnDateSetListener dateSetListener) {
		Calendar calendar = Calendar.getInstance();
		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH);
		int day = calendar.get(Calendar.DAY_OF_MONTH);
		Context instance = new ContextThemeWrapper(context, android.R.style.Theme_Holo_Light_Dialog);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
			// API 24 이상일 경우 시스템 기본 테마 사용
			instance = context;
		}
		DatePickerDialog datePickerDialog = new DatePickerDialog(instance, dateSetListener, year, month, day);
		datePickerDialog.getDatePicker().setMinDate(calendar.getTime().getTime());
		datePickerDialog.show();
	}

	public static void showTimePickerDialog(Context context, TimePickerDialog.OnTimeSetListener timeSetListener) {
		Date hour = new Date();
		String hourOfDay = new SimpleDateFormat("HH", Locale.KOREA).format(hour);
		Date min = new Date();
		String minute = new SimpleDateFormat("mm", Locale.KOREA).format(min);
		Context instance = new ContextThemeWrapper(context, android.R.style.Theme_Holo_Light_Dialog);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
			// API 24 이상일 경우 시스템 기본 테마 사용
			instance = context;
		}
		TimePickerDialog timePickerDialog = new TimePickerDialog(instance, timeSetListener,
				Integer.parseInt(hourOfDay), Integer.parseInt(minute), false);
		timePickerDialog.show();
	}

	public static String getFormattedDate(String date, int type) {
		String[] dateArray = date.split("-");
		if (dateArray[1].length() == FORMAT_LENGTH) {
			dateArray[1] = FORMAT_CHARACTER + dateArray[1];
		}
		if (dateArray[2].length() == FORMAT_LENGTH) {
			dateArray[2] = FORMAT_CHARACTER + dateArray[2];
		}
		String format = null;
		switch (type) {
			case TYPE_COLLECTION:
				format = dateArray[0] + dateArray[1] + dateArray[2];
				break;
			case TYPE_SORT:
				format = dateArray[0] + FORMAT_DATE + dateArray[1] + FORMAT_DATE + dateArray[2];
				break;
		}
		return format;
	}
}
