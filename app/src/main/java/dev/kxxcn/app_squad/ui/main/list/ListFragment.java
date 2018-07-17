package dev.kxxcn.app_squad.ui.main.list;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;

import com.gigamole.navigationtabstrip.NavigationTabStrip;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dev.kxxcn.app_squad.R;
import dev.kxxcn.app_squad.util.DialogUtils;
import dev.kxxcn.app_squad.util.StateButton;
import dev.kxxcn.app_squad.util.SwipeViewPager;

import static dev.kxxcn.app_squad.util.Constants.DIALOG_FRAGMENT;
import static dev.kxxcn.app_squad.util.Constants.TYPE_SORT;

/**
 * Created by kxxcn on 2018-04-26.
 */
public class ListFragment extends Fragment implements ListContract.OnDialogDismissed {

	@BindView(R.id.nts)
	NavigationTabStrip nts;

	@BindView(R.id.vp_list)
	SwipeViewPager vp_list;

	@BindView(R.id.btn_region)
	StateButton btn_region;
	@BindView(R.id.btn_date)
	StateButton btn_date;

	private String mRegion;
	private String mDate;

	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		final View view = inflater.inflate(R.layout.fragment_list, container, false);
		ButterKnife.bind(this, view);

		vp_list.setPagingEnabled(false);
		vp_list.setAdapter(new ListPagerAdapter(getActivity().getSupportFragmentManager(), null, null));

		nts.setTabIndex(0, true);
		nts.setOnTabStripSelectedIndexListener(onTabStripSelectedIndexListener);

		return view;
	}

	@Override
	public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
	}

	public static Fragment newInstance() {
		return new ListFragment();
	}

	private NavigationTabStrip.OnTabStripSelectedIndexListener onTabStripSelectedIndexListener =
			new NavigationTabStrip.OnTabStripSelectedIndexListener() {
				@Override
				public void onStartTabSelected(String title, int index) {

				}

				@Override
				public void onEndTabSelected(String title, int index) {
					vp_list.setAdapter(new ListPagerAdapter(getActivity().getSupportFragmentManager(), null, null));
					btn_region.setText(getString(R.string.list_region));
					btn_date.setText(getString(R.string.list_date));
					mRegion = null;
					mDate = null;
					vp_list.setCurrentItem(index);
				}
			};

	@OnClick(R.id.btn_region)
	public void showRegionListings() {
		ListDialog newFragment = ListDialog.newInstance();
		newFragment.setOnDialogDismissedListener(this);
		newFragment.show(getChildFragmentManager(), DIALOG_FRAGMENT);
	}

	@OnClick(R.id.btn_date)
	public void showCalendar() {
		DialogUtils.showDatePickerDialog(getContext(), dateSetListener);
	}

	private DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
		@Override
		public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
			mDate = DialogUtils.getFormattedDate(String.format(getContext().getString(R.string.select_date),
					year, (monthOfYear + 1), dayOfMonth), TYPE_SORT);
			btn_date.setText(mDate);
			vp_list.setAdapter(new ListPagerAdapter(getActivity().getSupportFragmentManager(), mRegion, mDate));
		}
	};

	@Override
	public void onDialogDismissed(String region) {
		mRegion = region;
		btn_region.setText(mRegion);
		if (mRegion.equals(getString(R.string.total))) {
			mRegion = null;
		}
		vp_list.setAdapter(new ListPagerAdapter(getActivity().getSupportFragmentManager(), mRegion, mDate));
	}

}
