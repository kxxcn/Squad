package dev.kxxcn.app_squad.ui.main.list.matchlist;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dev.kxxcn.app_squad.R;
import dev.kxxcn.app_squad.data.DataRepository;
import dev.kxxcn.app_squad.data.model.Information;
import dev.kxxcn.app_squad.data.model.User;
import dev.kxxcn.app_squad.data.remote.RemoteDataSource;
import dev.kxxcn.app_squad.ui.login.LoginActivity;
import dev.kxxcn.app_squad.ui.main.list.ListContract;
import dev.kxxcn.app_squad.ui.main.list.ListDialog;
import dev.kxxcn.app_squad.ui.main.match.MatchDialog;
import dev.kxxcn.app_squad.util.Constants;
import dev.kxxcn.app_squad.util.DialogUtils;
import dev.kxxcn.app_squad.util.StateButton;
import dev.kxxcn.app_squad.util.threading.UiThread;

import static dev.kxxcn.app_squad.util.Constants.DIALOG_FRAGMENT;
import static dev.kxxcn.app_squad.util.Constants.FORMAT_CHARACTER;
import static dev.kxxcn.app_squad.util.Constants.FORMAT_LENGTH;
import static dev.kxxcn.app_squad.util.Constants.TYPE_SORT;
import static dev.kxxcn.app_squad.util.Constants.USER;

/**
 * Created by kxxcn on 2018-05-09.
 */

public class MatchListFragment extends Fragment implements MatchListContract.View, MatchListContract.ItemClickListener, ListContract.OnDialogDismissed {

	@BindView(R.id.rv_list)
	RecyclerView rv_list;

	@BindView(R.id.btn_region)
	StateButton btn_region;
	@BindView(R.id.btn_date)
	StateButton btn_date;

	private MatchListContract.Presenter mPresenter;

	private List<Information> mList;

	private User mUser;

	private String mRegion;
	private String mDate;

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
		mUser = getArguments().getParcelable(USER);
		mPresenter.onLoadList(mRegion, mDate);
	}

	public static Fragment newInstance(User user) {
		MatchListFragment fragment = new MatchListFragment();

		Bundle args = new Bundle();
		args.putParcelable(USER, user);

		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public void showLoadingIndicator(boolean isShowing) {

	}

	@Override
	public void showMatchList(List<Information> list) {
		mList = new ArrayList<>(0);
		this.mList = list;
		Collections.sort(list, new Compare());
		rv_list.setAdapter(new MatchListAdapter(getContext(), list, this));
	}

	class Compare implements Comparator<Information> {
		@Override
		public int compare(Information o1, Information o2) {
			int ret = 0;
			if (o1.getDate().equals(o2.getDate())) {
				String time1 = o1.getTime().substring(0, o1.getTime().indexOf(":"));
				String time2 = o2.getTime().substring(0, o2.getTime().indexOf(":"));
				if (time1.length() == FORMAT_LENGTH) {
					time1 = FORMAT_CHARACTER + time1;
				}
				if (time2.length() == FORMAT_LENGTH) {
					time2 = FORMAT_CHARACTER + time2;
				}
				ret = time2.compareTo(time1);
			} else {
				ret = o2.getDate().compareTo(o1.getDate());
			}
			return ret;
		}
	}

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
			mPresenter.onLoadList(mRegion, mDate);
		}
	};

	@Override
	public void onClick(int position, int type) {
		if (type == MatchListAdapter.REQUEST) {
			mPresenter.onRequest(mList.get(position).getEmail(), getString(R.string.app_name), String.format(getString(R.string.list_request_match),
					mUser.getTeam()), mUser.getUid(), mList.get(position).getDate().replace("-", ""), Constants.ListsFilterType.MATCH_LIST);
		} else if (type == MatchListAdapter.INFORMATION) {
			MatchDialog dialog = new MatchDialog(getActivity(), getContext(), MatchDialog.MATCH_LIST, mList.get(position));
			dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
			WindowManager.LayoutParams params = new WindowManager.LayoutParams();
			params.copyFrom(dialog.getWindow().getAttributes());
			params.width = WindowManager.LayoutParams.MATCH_PARENT;
			params.height = WindowManager.LayoutParams.MATCH_PARENT;
			dialog.show();
			Window window = dialog.getWindow();
			window.setAttributes(params);
		}
	}

	@Override
	public void onDialogDismissed(String region) {
		mRegion = region;
		btn_region.setText(mRegion);
		if (mRegion.equals(getString(R.string.total))) {
			mRegion = null;
		}
		mPresenter.onLoadList(mRegion, mDate);
	}

	@Override
	public void showSuccessfullyRequested() {
		UiThread.getInstance().post(new Runnable() {
			@Override
			public void run() {
				Toast.makeText(getContext(), getString(R.string.list_successfully_request_match), Toast.LENGTH_SHORT).show();
			}
		});
	}

	@Override
	public void showUnsuccessfullyRequested() {
		UiThread.getInstance().post(new Runnable() {
			@Override
			public void run() {
				Toast.makeText(getContext(), getString(R.string.list_unsuccessfully_request), Toast.LENGTH_SHORT).show();
			}
		});
	}

	@Override
	public void showInvalidAccount() {
		mPresenter.onLogout();
	}

	@Override
	public void showSuccessfullyLogout() {
		startActivity(new Intent(getContext(), LoginActivity.class));
		getActivity().finish();
	}

}
