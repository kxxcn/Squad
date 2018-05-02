package dev.kxxcn.app_squad.ui.main.match.fab;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Rect;
import android.os.Build;
import android.os.Handler;
import android.text.TextUtils;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.allattentionhere.fabulousfilter.AAH_FabulousFragment;
import com.unstoppable.submitbuttonview.SubmitButton;

import org.angmarch.views.NiceSpinner;

import java.util.Arrays;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dev.kxxcn.app_squad.R;

import static dev.kxxcn.app_squad.util.Constants.STATE_EXPANDED;

/**
 * Created by kxxcn on 2018-04-30.
 */
public class FabFragment extends AAH_FabulousFragment {

	private static final int POSITION_SPINNER_DEFAULT = 1;

	@BindView(R.id.rl_content)
	RelativeLayout rl_content;
	@BindView(R.id.ll_bottom)
	LinearLayout ll_bottom;
	@BindView(R.id.ll_age)
	LinearLayout ll_age;
	@BindView(R.id.ll_group)
	LinearLayout ll_group;
	@BindView(R.id.ll_collapse)
	LinearLayout ll_collapse;
	@BindView(R.id.ll_expanded)
	LinearLayout ll_expanded;

	@BindView(R.id.btn_submit)
	SubmitButton btn_submit;

	@BindView(R.id.spinner_region)
	NiceSpinner spinner_region;
	@BindView(R.id.spinner_age)
	NiceSpinner spinner_age;
	@BindView(R.id.spinner_group)
	NiceSpinner spinner_group;

	@BindView(R.id.tv_title)
	TextView tv_title;
	@BindView(R.id.tv_date)
	TextView tv_date;

	private ViewTreeObserver.OnGlobalLayoutListener mGlobalListener;

	private boolean isShowing = false;

	public static FabFragment newInstance() {
		return new FabFragment();
	}

	@Override
	public void setupDialog(Dialog dialog, int style) {
		View contentView = View.inflate(getContext(), R.layout.fragment_fab, null);
		ButterKnife.bind(this, contentView);
		initUI();
		setAnimationDuration(600);
		setPeekHeight(300);
		setViewgroupStatic(ll_bottom);                   // Layout to stick at bottom on slide
		setViewMain(rl_content);                         // Necessary! main bottomsheet view
		setMainContentView(contentView);                 // Necessary! call at end before super
		setAnimationListener((AnimationListener) getActivity());
		// setCallbacks((Callbacks) getActivity());
		// setViewPager(vp_types);                       // If you use viewpager that has scrollview

		addOnGlobalLayoutListener(rl_content);
		super.setupDialog(dialog, style);
	}

	@OnClick(R.id.btn_submit)
	public void onSubmit() {
		if (!TextUtils.isEmpty(tv_date.getText())) {
			new Handler().postDelayed(new Runnable() {
				@Override
				public void run() {
					btn_submit.doResult(true);
				}
			}, 3000);
		} else {
			btn_submit.reset();
			Toast.makeText(getContext(), getString(R.string.input_date), Toast.LENGTH_SHORT).show();
		}
	}

	@OnClick(R.id.tv_date)
	public void showCalendarDialog() {
		Calendar calendar = Calendar.getInstance();
		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH);
		int day = calendar.get(Calendar.DAY_OF_MONTH);
		Context context = new ContextThemeWrapper(getContext(), android.R.style.Theme_Holo_Light_Dialog);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
			// API 24 이상일 경우 시스템 기본 테마 사용
			context = getContext();
		}
		DatePickerDialog datePickerDialog = new DatePickerDialog(context, dateSetListener, year, month, day);
		datePickerDialog.getDatePicker().setMinDate(calendar.getTime().getTime());
		datePickerDialog.show();
	}

	private DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
		@Override
		public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
			tv_date.setText(String.format(getString(R.string.select_date),
					year, (monthOfYear + 1), dayOfMonth));
		}
	};

	private void initUI() {
		String[] regions = getContext().getResources().getStringArray(R.array.fab_regions);
		String[] ages = getContext().getResources().getStringArray(R.array.fab_ages);
		String[] totals = getContext().getResources().getStringArray(R.array.fab_group);
		List<String> regionList = new LinkedList<>(Arrays.asList(regions));
		List<String> ageList = new LinkedList<>(Arrays.asList(ages));
		List<String> totalList = new LinkedList<>(Arrays.asList(totals));
		spinner_region.attachDataSource(regionList);
		spinner_age.attachDataSource(ageList);
		spinner_group.attachDataSource(totalList);
		spinner_age.setSelectedIndex(POSITION_SPINNER_DEFAULT);
		spinner_group.setSelectedIndex(POSITION_SPINNER_DEFAULT);
	}

	private void addOnGlobalLayoutListener(final View view) {
		if (mGlobalListener == null) {
			mGlobalListener = new ViewTreeObserver.OnGlobalLayoutListener() {
				@Override
				public void onGlobalLayout() {
					Rect r = new Rect();
					view.getGlobalVisibleRect(r);
					if (r.top == STATE_EXPANDED) {
						isShowing = true;
					} else {
						isShowing = false;
					}
					showDetailedSearchConditions(isShowing);
				}
			};
		}
		view.getViewTreeObserver().addOnGlobalLayoutListener(mGlobalListener);
	}

	private void showDetailedSearchConditions(boolean isShowing) {
		if (isShowing) {
			ll_age.setVisibility(View.VISIBLE);
			ll_group.setVisibility(View.VISIBLE);
			ll_expanded.setVisibility(View.VISIBLE);
			ll_collapse.setVisibility(View.GONE);
		} else {
			ll_age.setVisibility(View.GONE);
			ll_group.setVisibility(View.GONE);
			ll_expanded.setVisibility(View.GONE);
			ll_collapse.setVisibility(View.VISIBLE);
		}
	}

}
