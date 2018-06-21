package dev.kxxcn.app_squad.ui.main.team;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;

import dev.kxxcn.app_squad.R;

/**
 * Created by kxxcn on 2018-06-21.
 */

public class TeamDialog extends Dialog {

	public TeamDialog(@NonNull Context context) {
		super(context);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_team);
	}
}
