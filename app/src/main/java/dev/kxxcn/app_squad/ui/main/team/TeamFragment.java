package dev.kxxcn.app_squad.ui.main.team;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import dev.kxxcn.app_squad.R;
import dev.kxxcn.app_squad.data.DataRepository;
import dev.kxxcn.app_squad.data.remote.RemoteDataSource;
import dev.kxxcn.app_squad.ui.login.LoginActivity;

/**
 * Created by kxxcn on 2018-04-26.
 */
public class TeamFragment extends Fragment implements TeamContract.View {

	@BindView(R.id.rv_team)
	RecyclerView rv_team;

	@BindView(R.id.ll_collapstoolbar)
	CollapsingToolbarLayout toolbar;

	@BindView(R.id.iv_collapsing)
	ImageView iv_collapsing;

	@BindView(R.id.fab)
	FloatingActionButton fab;

	private TeamContract.Presenter mPresenter;

	private int[] imgs = {R.drawable.team_banner1, R.drawable.team_banner2, R.drawable.team_banner3,
			R.drawable.team_banner4, R.drawable.team_banner5, R.drawable.team_banner6, R.drawable.team_banner7,
			R.drawable.team_banner8, R.drawable.team_banner9, R.drawable.team_banner10};

	@Override
	public void setPresenter(TeamContract.Presenter presenter) {
		this.mPresenter = presenter;
	}

	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_team, container, false);
		ButterKnife.bind(this, view);

		new TeamPresenter(this, DataRepository.getInstance(RemoteDataSource.getInstance(
				FirebaseAuth.getInstance(), FirebaseDatabase.getInstance().getReference())));

		return view;
	}

	@Override
	public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		mPresenter.onLoadAccount();
		mPresenter.onLoadRecord();
		Glide.with(this).load(imgs[new Random().nextInt(imgs.length)]).diskCacheStrategy(DiskCacheStrategy.NONE).into(iv_collapsing);
		List<String> list = new ArrayList<>(0);
		TeamAdapter adapter = new TeamAdapter(list);
		rv_team.setAdapter(adapter);
	}

	public static Fragment newInstance() {
		return new TeamFragment();
	}

	@Override
	public void showLoadingIndicator(boolean isShowing) {

	}

	@Override
	public void setToolbarTitle(String title) {
		toolbar.setTitle(title);
	}

	@Override
	public void showErrorBadRequest() {
		getContext().startActivity(new Intent(getContext(), LoginActivity.class));
		getActivity().finish();
	}

}
