package dev.kxxcn.app_squad.view.match;

import android.app.Dialog;
import android.os.Handler;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.allattentionhere.fabulousfilter.AAH_FabulousFragment;
import com.unstoppable.submitbuttonview.SubmitButton;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dev.kxxcn.app_squad.R;

/**
 * Created by kxxcn on 2018-04-27.
 */

public class FabFragment extends AAH_FabulousFragment {

	@BindView(R.id.rl_content)
	RelativeLayout rl_content;

	@BindView(R.id.ll_buttons)
	LinearLayout ll_buttons;

	@BindView(R.id.btn_submit)
	SubmitButton submitBtn;

	public static FabFragment newInstance() {
		return new FabFragment();
	}

	@Override
	public void setupDialog(Dialog dialog, int style) {
		View contentView = View.inflate(getContext(), R.layout.fragment_fab, null);
		ButterKnife.bind(this, contentView);

		setAnimationDuration(600);
		setPeekHeight(300);
		setViewgroupStatic(ll_buttons);                  // Layout to stick at bottom on slide
		setViewMain(rl_content);                         // Necessary! main bottomsheet view
		setMainContentView(contentView);                 // Necessary! call at end before super
		// setCallbacks((Callbacks) getActivity());
		// setViewPager(vp_types);                       // If you use viewpager that has scrollview
		super.setupDialog(dialog, style);
	}

	@OnClick(R.id.btn_submit)
	public void onSubmit() {
		submitBtn.setProgress(0);

		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				submitBtn.doResult(true);
			}
		}, 3000);
	}

}
