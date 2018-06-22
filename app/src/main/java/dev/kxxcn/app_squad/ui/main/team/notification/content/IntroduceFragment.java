package dev.kxxcn.app_squad.ui.main.team.notification.content;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import dev.kxxcn.app_squad.R;
import dev.kxxcn.app_squad.data.model.User;

/**
 * Created by kxxcn on 2018-06-22.
 */

public class IntroduceFragment extends Fragment {

	@BindView(R.id.tv_enemy)
	TextView tv_enemy;

	private static final String USER = "object";

	public static IntroduceFragment newInstance(User user) {
		IntroduceFragment fragment = new IntroduceFragment();

		Bundle args = new Bundle();
		args.putParcelable(USER, user);

		fragment.setArguments(args);
		return fragment;
	}

	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_introduce, container, false);
		ButterKnife.bind(this, view);
		initUI();
		return view;
	}

	private void initUI() {
		User user = getArguments().getParcelable(USER);
		tv_enemy.setText(user.getTeam());
	}

}
