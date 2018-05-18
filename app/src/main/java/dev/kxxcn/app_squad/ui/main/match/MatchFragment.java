package dev.kxxcn.app_squad.ui.main.match;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gigamole.infinitecycleviewpager.HorizontalInfiniteCycleViewPager;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dev.kxxcn.app_squad.R;
import dev.kxxcn.app_squad.ui.main.match.fab.FabFragment;

/**
 * Created by kxxcn on 2018-04-30.
 */

public class MatchFragment extends Fragment {


	@BindView(R.id.vp_hic)
	HorizontalInfiniteCycleViewPager vp_hic;

	@BindView(R.id.fab)
	FloatingActionButton fab;

	private FabFragment dialog;

	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_match, container, false);
		ButterKnife.bind(this, view);

		return view;
	}

	@OnClick(R.id.fab)
	public void onMatch() {
		if (dialog != null) {
			dialog.onDestroyView();
		}
		dialog = FabFragment.newInstance();
		dialog.setParentFab(fab);
		dialog.show(getActivity().getSupportFragmentManager(), dialog.getTag());
	}

	@Override
	public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		vp_hic.setAdapter(new MatchAdpater(getContext()));
	}

	public static Fragment newInstance() {
		return new MatchFragment();
	}

}
