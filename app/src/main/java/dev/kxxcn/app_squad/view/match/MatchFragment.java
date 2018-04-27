package dev.kxxcn.app_squad.view.match;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gigamole.infinitecycleviewpager.HorizontalInfiniteCycleViewPager;

import butterknife.BindView;
import butterknife.ButterKnife;
import dev.kxxcn.app_squad.R;

/**
 * Created by kxxcn on 2018-04-26.
 */
public class MatchFragment extends Fragment implements MatchContract.View {

	private MatchContract.Presenter mPresenter;

	@BindView(R.id.vp_hic)
	HorizontalInfiniteCycleViewPager horizontalInfiniteCycleViewPager;

	@BindView(R.id.fab)
	FloatingActionButton fab;

	@Override
	public void setPresenter(MatchContract.Presenter presenter) {
		this.mPresenter = presenter;
	}

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_match, container, false);
		ButterKnife.bind(this, view);

		new MatchPresenter(this);

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
