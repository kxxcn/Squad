package dev.kxxcn.app_squad.util;

import android.Manifest;

/**
 * Created by kxxcn on 2018-05-01.
 */

public class Constants {
	public static final String TAG = "kxxcn";
	public static final String CAMERA = Manifest.permission.CAMERA;
	public static final String FORMAT_CHARACTER = "0";
	public static final String FORMAT_DATE = "-";

	public static final int LOADING = 1000;
	public static final int POSITION_SPINNER_DEFAULT = 1;
	public static final int FORMAT_LENGTH = 1;
	public static final int TYPE_COLLECTION = 0;
	public static final int TYPE_SORT = 1;
	public static final int VIBRATE_NOTIFICATION = 1000;

	public enum ListsFilterType {
		MATCH_LIST,
		RECRUITMENT_LIST,
		PLAYER_LIST
	}
}
