package dev.kxxcn.app_squad.ui.main.list;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;
import butterknife.OnClick;
import dev.kxxcn.app_squad.R;

/**
 * Created by kxxcn on 2018-07-17.
 */

public class ListDialog extends DialogFragment {

	private ListContract.OnDialogDismissed mDialogDismissedCallback;

	public static ListDialog newInstance() {
		return new ListDialog();
	}

	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.dialog_list, container, false);
		ButterKnife.bind(this, view);
		return view;
	}

	@Override
	public void onStart() {
		super.onStart();
		Dialog dialog = getDialog();
		if (dialog != null) {
			int width = ViewGroup.LayoutParams.MATCH_PARENT;
			int height = ViewGroup.LayoutParams.WRAP_CONTENT;
			dialog.getWindow().setLayout(width, height);
		}
	}

	public void setOnDialogDismissedListener(ListContract.OnDialogDismissed listener) {
		this.mDialogDismissedCallback = listener;
	}

	@OnClick(R.id.btn_total)
	public void choidTotal() {
		mDialogDismissedCallback.onDialogDismissed(getString(R.string.total));
		dismiss();
	}

	@OnClick(R.id.btn_seoul)
	public void choiceSeoul() {
		mDialogDismissedCallback.onDialogDismissed(getString(R.string.seoul));
		dismiss();
	}

	@OnClick(R.id.btn_busan)
	public void choiceBusan() {
		mDialogDismissedCallback.onDialogDismissed(getString(R.string.busan));
		dismiss();
	}

	@OnClick(R.id.btn_gwangju)
	public void choiceGwangju() {
		mDialogDismissedCallback.onDialogDismissed(getString(R.string.gwangju));
		dismiss();
	}

	@OnClick(R.id.btn_daegu)
	public void choiceDaegu() {
		mDialogDismissedCallback.onDialogDismissed(getString(R.string.daegu));
		dismiss();
	}

	@OnClick(R.id.btn_jeju)
	public void choiceJeju() {
		mDialogDismissedCallback.onDialogDismissed(getString(R.string.jeju));
		dismiss();
	}

	@OnClick(R.id.btn_incheon)
	public void choiceIncheon() {
		mDialogDismissedCallback.onDialogDismissed(getString(R.string.incheon));
		dismiss();
	}

	@OnClick(R.id.btn_daejeon)
	public void choiceDaejeon() {
		mDialogDismissedCallback.onDialogDismissed(getString(R.string.daejeon));
		dismiss();
	}

	@OnClick(R.id.btn_ulsan)
	public void choiceUlsan() {
		mDialogDismissedCallback.onDialogDismissed(getString(R.string.ulsan));
		dismiss();
	}

}
