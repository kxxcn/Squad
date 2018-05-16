package dev.kxxcn.app_squad.ui.main.list;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gigamole.navigationtabstrip.NavigationTabStrip;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import butterknife.BindView;
import butterknife.ButterKnife;
import dev.kxxcn.app_squad.R;
import dev.kxxcn.app_squad.data.DataRepository;
import dev.kxxcn.app_squad.data.remote.RemoteDataSource;

/**
 * Created by kxxcn on 2018-04-26.
 */
public class ListFragment extends Fragment implements ListContract.View {

	@BindView(R.id.nts)
	NavigationTabStrip nts;

	@BindView(R.id.vp_lists)
	ViewPager vp_lists;

	private ListContract.Presenter mPresenter;

	@Override
	public void setPresenter(ListContract.Presenter presenter) {
		this.mPresenter = presenter;
	}

	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		final View view = inflater.inflate(R.layout.fragment_list, container, false);
		ButterKnife.bind(this, view);

		new ListPresenter(this, DataRepository.getInstance(RemoteDataSource.getInstance(
				FirebaseAuth.getInstance(), FirebaseDatabase.getInstance().getReference())));

		vp_lists.setAdapter(new ListPagerAdapter(getActivity().getSupportFragmentManager()));

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

	@Override
	public void showLoadingIndicator(boolean isShowing) {

	}

	private NavigationTabStrip.OnTabStripSelectedIndexListener onTabStripSelectedIndexListener =
			new NavigationTabStrip.OnTabStripSelectedIndexListener() {
				@Override
				public void onStartTabSelected(String title, int index) {

				}

				@Override
				public void onEndTabSelected(String title, int index) {
					vp_lists.setCurrentItem(index);
				}
			};

}
