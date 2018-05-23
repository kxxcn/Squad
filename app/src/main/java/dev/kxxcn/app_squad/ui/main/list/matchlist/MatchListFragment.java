package dev.kxxcn.app_squad.ui.main.list.matchlist;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import dev.kxxcn.app_squad.R;
import dev.kxxcn.app_squad.data.DataRepository;
import dev.kxxcn.app_squad.data.model.Information;
import dev.kxxcn.app_squad.data.remote.RemoteDataSource;
import dev.kxxcn.app_squad.util.Constants;

/**
 * Created by kxxcn on 2018-05-09.
 */

public class MatchListFragment extends Fragment implements MatchListContract.View {

	@BindView(R.id.rv_list)
	RecyclerView rv_list;

	private MatchListContract.Presenter mPresenter;

	@Override
	public void setPresenter(MatchListContract.Presenter presenter) {
		this.mPresenter = presenter;
	}

	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_matchlist, container, false);
		ButterKnife.bind(this, view);

		new MatchListPresenter(this, DataRepository.getInstance(RemoteDataSource.getInstance(
				FirebaseAuth.getInstance(), FirebaseDatabase.getInstance().getReference())));

		mPresenter.setFiltering(Constants.ListsFilterType.MATCH_LIST);

		return view;
	}

	@Override
	public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		mPresenter.onLoad();
	}

	public static Fragment newInstance() {
		return new MatchListFragment();
	}

	@Override
	public void showLoadingIndicator(boolean isShowing) {

	}

	@Override
	public void showMatchList(List<Information> list) {
		Collections.sort(list, new Compare());
		MatchListAdapter adapter = new MatchListAdapter(getContext(), list);
		rv_list.setAdapter(adapter);
	}

	class Compare implements Comparator<Information> {
		@Override
		public int compare(Information o1, Information o2) {
			return o2.getDate().compareTo(o1.getDate());
		}
	}

}
