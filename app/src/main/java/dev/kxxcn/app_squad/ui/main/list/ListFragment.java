package dev.kxxcn.app_squad.ui.main.list;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gigamole.navigationtabstrip.NavigationTabStrip;

import butterknife.BindView;
import butterknife.ButterKnife;
import dev.kxxcn.app_squad.R;
import dev.kxxcn.app_squad.util.SwipeViewPager;

/**
 * Created by kxxcn on 2018-04-26.
 */
public class ListFragment extends Fragment {

	@BindView(R.id.nts)
	NavigationTabStrip nts;

	@BindView(R.id.vp_list)
	SwipeViewPager vp_list;

	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		final View view = inflater.inflate(R.layout.fragment_list, container, false);
		ButterKnife.bind(this, view);

		vp_list.setPagingEnabled(false);
		vp_list.setAdapter(new ListPagerAdapter(getActivity().getSupportFragmentManager()));

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
					vp_list.setCurrentItem(index);
				}
			};

}
