package dev.kxxcn.app_squad.util;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.view.ContextThemeWrapper;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import dev.kxxcn.app_squad.R;

import static dev.kxxcn.app_squad.util.Constants.FORMAT_CHARACTER;
import static dev.kxxcn.app_squad.util.Constants.FORMAT_DATE;
import static dev.kxxcn.app_squad.util.Constants.FORMAT_LENGTH;
import static dev.kxxcn.app_squad.util.Constants.SIMPLE_DATE_FORMAT2;
import static dev.kxxcn.app_squad.util.Constants.SIMPLE_DATE_FORMAT3;
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
		DatePickerDialog datePickerDialog = new DatePickerDialog(instance, android.app.AlertDialog.THEME_DEVICE_DEFAULT_DARK, dateSetListener,
				year, month, day);
		datePickerDialog.getDatePicker().setMinDate(calendar.getTime().getTime());
		datePickerDialog.show();
	}

	public static void showTimePickerDialog(Context context, TimePickerDialog.OnTimeSetListener timeSetListener, String title) {
		Date hour = new Date();
		String hourOfDay = new SimpleDateFormat("HH", Locale.KOREA).format(hour);
		Date min = new Date();
		String minute = new SimpleDateFormat("mm", Locale.KOREA).format(min);
		Context instance = new ContextThemeWrapper(context, android.R.style.Theme_Holo_Light_Dialog);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
			// API 24 이상일 경우 시스템 기본 테마 사용
			instance = context;
		}
		TimePickerDialog timePickerDialog = new TimePickerDialog(instance, android.R.style.Theme_Holo_Dialog, timeSetListener,
				Integer.parseInt(hourOfDay), 0, false);
		timePickerDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
		timePickerDialog.setTitle(title);
		timePickerDialog.show();
	}

	public static void showAlertDialog(Context context, String message, DialogInterface.OnClickListener positiveListener, DialogInterface.OnClickListener negativeListener) {
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setMessage(message);
		builder.setPositiveButton(context.getString(R.string.yes), positiveListener)
				.setNegativeButton(context.getString(R.string.no), negativeListener).show();
	}

	public static void showPositiveDialog(Context context, String message, DialogInterface.OnClickListener positiveListener) {
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setMessage(message);
		builder.setPositiveButton(context.getString(R.string.yes), positiveListener).show();
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

	public static String getDate() {
		long now = System.currentTimeMillis();
		Date date = new Date(now);
		SimpleDateFormat format = new SimpleDateFormat(SIMPLE_DATE_FORMAT2, Locale.KOREA);
		return format.format(date);
	}

	public static String getTime() {
		long now = System.currentTimeMillis();
		Date date = new Date(now);
		SimpleDateFormat format = new SimpleDateFormat(SIMPLE_DATE_FORMAT3, Locale.KOREA);
		return format.format(date);
	}
}
