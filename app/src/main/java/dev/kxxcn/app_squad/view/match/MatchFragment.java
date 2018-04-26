package dev.kxxcn.app_squad.view.match;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gigamole.infinitecycleviewpager.HorizontalInfiniteCycleViewPager;

import butterknife.BindView;
import butterknife.ButterKnife;
import dev.kxxcn.app_squad.R;

/**
 * Created by Jun on 2018-04-26.
 */

public class MatchFragment extends Fragment {

	@BindView(R.id.vp_hic)
	HorizontalInfiniteCycleViewPager horizontalInfiniteCycleViewPager;

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_match, container, false);
		ButterKnife.bind(this, view);
		return view;
	}

	@Override
	public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		horizontalInfiniteCycleViewPager.setAdapter(new MatchAdpater(getContext()));
	}

	public static Fragment newInstance() {
		return new MatchFragment();
	}

}
