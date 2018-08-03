package dev.kxxcn.app_squad.ui.main.match.fab;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.graphics.Rect;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.allattentionhere.fabulousfilter.AAH_FabulousFragment;
import com.github.ybq.android.spinkit.style.ChasingDots;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.unstoppable.submitbuttonview.SubmitButton;

import org.angmarch.views.NiceSpinner;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dev.kxxcn.app_squad.R;
import dev.kxxcn.app_squad.data.DataRepository;
import dev.kxxcn.app_squad.data.model.Information;
import dev.kxxcn.app_squad.data.model.User;
import dev.kxxcn.app_squad.data.remote.RemoteDataSource;
import dev.kxxcn.app_squad.ui.main.match.fab.quick.QuickDialog;
import dev.kxxcn.app_squad.util.DialogUtils;

import static dev.kxxcn.app_squad.util.Constants.DIALOG_FRAGMENT;
import static dev.kxxcn.app_squad.util.Constants.POSITION_SPINNER_DEFAULT;
import static dev.kxxcn.app_squad.util.Constants.TYPE_SORT;

/**
 * Created by kxxcn on 2018-04-30.
 */
public class FabFragment extends AAH_FabulousFragment implements FabContract.View, FabContract.OnDialogDismissed {

	private static final int STATE_EXPANDED = 0;

	@BindView(R.id.rl_content)
	RelativeLayout rl_content;
	@BindView(R.id.ll_bottom)
	LinearLayout ll_bottom;
	@BindView(R.id.ll_date)
	LinearLayout ll_date;
	@BindView(R.id.ll_rule)
	LinearLayout ll_rule;
	@BindView(R.id.ll_collapse)
	LinearLayout ll_collapse;
	@BindView(R.id.ll_expanded)
	LinearLayout ll_expanded;

	@BindView(R.id.btn_match)
	SubmitButton btn_match;

	@BindView(R.id.spinner_region)
	NiceSpinner spinner_region;
	@BindView(R.id.spinner_rule)
	NiceSpinner spinner_rule;

	@BindView(R.id.tv_date)
	TextView tv_date;

	@BindView(R.id.progressbar)
	ProgressBar progressBar;

	private boolean mIsExpandable;

	private ViewTreeObserver.OnGlobalLayoutListener mGlobalListener;

	private FabContract.Presenter mPresenter;

	private User mUser;

	public static FabFragment newInstance() {
		return new FabFragment();
	}

	@Override
	public void setPresenter(FabContract.Presenter presenter) {
		this.mPresenter = presenter;
	}

	@Override
	public void setupDialog(Dialog dialog, int style) {
		View contentView = View.inflate(getContext(), R.layout.fragment_fab, null);
		ButterKnife.bind(this, contentView);
		new FabPresenter(this, DataRepository.getInstance(RemoteDataSource.getInstance(
				FirebaseAuth.getInstance(), FirebaseDatabase.getInstance().getReference())));
		initUI();
		setAnimationDuration(600);
		setPeekHeight(350);
		setViewgroupStatic(ll_bottom);                                 // Layout to stick at bottom on slide
		setViewMain(rl_content);                                       // Necessary! main bottomsheet view
		setMainContentView(contentView);                               // Necessary! call at end before super
		// setAnimationListener((AnimationListener) getActivity());
		// setCallbacks((Callbacks) getActivity());
		// setViewPager(vp_types);                                     // If you use viewpager that has scrollview

		addOnGlobalLayoutListener(rl_content);
		super.setupDialog(dialog, style);
	}

	@OnClick(R.id.btn_match)
	public void onMatch() {
		btn_match.reset();
		if (!TextUtils.isEmpty(tv_date.getText())) {
			btn_match.setVisibility(View.GONE);
			if (mUser != null) {
				if (mIsExpandable) {
					mPresenter.onQuickMatch(mUser.getTeam(), mUser.getUid(), spinner_region.getText().toString(), tv_date.getText().toString(), spinner_rule.getText().toString());
				} else {
					mPresenter.onQuickMatch(mUser.getTeam(), mUser.getUid(), spinner_region.getText().toString(), tv_date.getText().toString(), null);
				}
			}
		} else {
			Toast.makeText(getContext(), getString(R.string.input_date), Toast.LENGTH_SHORT).show();
		}
	}

	@OnClick(R.id.ll_date)
	public void showDatePickerDialog() {
		DialogUtils.showDatePickerDialog(getContext(), dateSetListener);
	}

	private DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
		@Override
		public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
			tv_date.setText(DialogUtils.getFormattedDate(String.format(getString(R.string.select_date),
					year, (monthOfYear + 1), dayOfMonth), TYPE_SORT));
		}
	};

	private void initUI() {
		mPresenter.onLoadAccount();
		String[] regions = getContext().getResources().getStringArray(R.array.regions);
		String[] rules = getContext().getResources().getStringArray(R.array.group);
		List<String> regionList = new LinkedList<>(Arrays.asList(regions));
		List<String> ruleList = new LinkedList<>(Arrays.asList(rules));
		spinner_region.attachDataSource(regionList);
		spinner_rule.attachDataSource(ruleList);
		spinner_rule.setSelectedIndex(POSITION_SPINNER_DEFAULT);
		ChasingDots chasingDots = new ChasingDots();
		chasingDots.setColor(getResources().getColor(R.color.progressbar_background));
		progressBar.setIndeterminateDrawable(chasingDots);
		tv_date.setText(DialogUtils.getDate());
	}

	private void addOnGlobalLayoutListener(final View view) {
		if (mGlobalListener == null) {
			mGlobalListener = new ViewTreeObserver.OnGlobalLayoutListener() {
				@Override
				public void onGlobalLayout() {
					Rect r = new Rect();
					view.getGlobalVisibleRect(r);
					if (r.top == STATE_EXPANDED) {
						mIsExpandable = true;
					} else {
						mIsExpandable = false;
					}
					showDetailedSearchConditions(mIsExpandable);
				}
			};
		}
		view.getViewTreeObserver().addOnGlobalLayoutListener(mGlobalListener);
	}

	private void showDetailedSearchConditions(boolean isShowing) {
		RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) ll_bottom.getLayoutParams();
		if (isShowing) {
			ll_rule.setVisibility(View.VISIBLE);
			ll_expanded.setVisibility(View.VISIBLE);
			ll_collapse.setVisibility(View.GONE);
			params.bottomMargin = 30;
		} else {
			ll_rule.setVisibility(View.GONE);
			ll_expanded.setVisibility(View.GONE);
			ll_collapse.setVisibility(View.VISIBLE);
			params.bottomMargin = 0;
		}
		ll_bottom.setLayoutParams(params);
	}

	@Override
	public void showLoadingIndicator(boolean isShowing) {
		if (isShowing) {
			progressBar.setVisibility(View.VISIBLE);
		} else {
			progressBar.setVisibility(View.GONE);
		}
	}

	@Override
	public void showSuccessfullyLoadAccount(User user) {
		this.mUser = user;
	}

	@Override
	public void isUsableComponent(boolean isUsable) {
		spinner_region.setEnabled(isUsable);
		spinner_rule.setEnabled(isUsable);
		ll_date.setEnabled(isUsable);
	}

	@Override
	public void showResults(final Information information) {
		btn_match.setVisibility(View.VISIBLE);
		QuickDialog newFragment = QuickDialog.newInstance(information, information.getTeam(), mUser.getTeam(),
				mUser.getUid(), String.valueOf(RemoteDataSource.FLAG_MATCH_LIST));
		newFragment.setOnDialogDismissedListener(this);
		newFragment.show(getChildFragmentManager(), DIALOG_FRAGMENT);
	}

	@Override
	public void showFailedToLoad() {
		btn_match.setVisibility(View.VISIBLE);
		Toast.makeText(getContext(), getString(R.string.quick_failed_search), Toast.LENGTH_SHORT).show();
	}

	@Override
	public void noResults() {
		btn_match.setVisibility(View.VISIBLE);
		Toast.makeText(getContext(), getString(R.string.quick_no_result), Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onDialogDismissed() {
		dismiss();
	}

}
