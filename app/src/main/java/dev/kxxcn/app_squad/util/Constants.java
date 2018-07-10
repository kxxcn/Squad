package dev.kxxcn.app_squad.util;

import android.Manifest;

/**
 * Created by kxxcn on 2018-05-01.
 */

public class Constants {
	public static final String TAG = "kxxcn";
	public static final String CAMERA = Manifest.permission.CAMERA;
	public static final String READ_PHONE_STATE = Manifest.permission.READ_PHONE_STATE;
	public static final String CALL_PHONE = Manifest.permission.CALL_PHONE;
	public static final String FORMAT_CHARACTER = "0";
	public static final String FORMAT_DATE = "-";
	public static final String SIMPLE_DATE_FORMAT1 = "yyyy/MM/dd HH:mm";
	public static final String SIMPLE_DATE_FORMAT2 = "yyyy-MM-dd";
	public static final String SIMPLE_DATE_FORMAT3 = "aa hh:mm";
	public static final String GSON_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
	public static final String DIALOG_FRAGMENT = "dialog";

	public static final int LOADING = 500;
	public static final int POSITION_SPINNER_DEFAULT = 1;
	public static final int FORMAT_LENGTH = 1;
	public static final int TYPE_COLLECTION = 0;
	public static final int TYPE_SORT = 1;
	public static final int VIBRATE_NOTIFICATION = 1000;
	public static final int DISMISS_NOTI_DIALOG = 1500;
	public static final int TYPE_REQUEST = 0;
	public static final int TYPE_RESPONSE = 1;

	public enum ListsFilterType {
		MATCH_LIST,
		RECRUITMENT_LIST,
		PLAYER_LIST
	}

	public enum NoticeFilterType {
		SQAUD,
		NOTICE,
		EVENT
	}
}
