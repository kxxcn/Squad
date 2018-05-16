package dev.kxxcn.app_squad.util;

import android.Manifest;

/**
 * Created by kxxcn on 2018-05-01.
 */

public class Constants {
	public static final String TAG = "kxxcn";

	public static final int LOADING = 1000;

	public static final int STATE_EXPANDED = 0;

	public static final String CAMERA = Manifest.permission.CAMERA;

	public enum ListsFilterType {
		MATCH_LIST,
		RECRUITMENT_LIST,
		PLAYER_LIST
	}
}
