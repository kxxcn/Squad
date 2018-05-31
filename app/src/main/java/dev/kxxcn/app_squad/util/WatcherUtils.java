package dev.kxxcn.app_squad.util;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

/**
 * Created by kxxcn on 2018-05-31.
 */

public class WatcherUtils {
	public static TextWatcher noSpaceWatcher(final EditText editText) {
		TextWatcher watcher = new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {

			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {

			}

			@Override
			public void afterTextChanged(Editable s) {
				String result = s.toString().replaceAll(" ", "");
				if (!s.toString().equals(result)) {
					editText.setText(result);
					editText.setSelection(result.length());
				}
			}
		};

		return watcher;
	}
}
