package dev.kxxcn.app_squad.ui.main.team;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import dev.kxxcn.app_squad.R;
import dev.kxxcn.app_squad.data.model.Account;
import dev.kxxcn.app_squad.data.model.Battle;

/**
 * Created by kxxcn on 2018-06-08.
 */

public class TeamAdapter extends RecyclerView.Adapter<TeamAdapter.ViewHolder> {

	private List<Battle> mList;

	public TeamAdapter(List<Battle> list) {
		this.mList = list;
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_team, parent, false);
		return new ViewHolder(view);
	}

	@Override
	public void onBindViewHolder(ViewHolder holder, int position) {
		holder.tv_date.setText(mList.get(holder.getAdapterPosition()).getDate());
		holder.tv_place.setText(mList.get(holder.getAdapterPosition()).getPlace());
		if (mList.get(holder.getAdapterPosition()).isHome()) {
			holder.tv_home.setText(Account.getInstance().getTeam());
			holder.tv_away.setText(mList.get(holder.getAdapterPosition()).getEnemy());
		} else {
			holder.tv_home.setText(mList.get(holder.getAdapterPosition()).getEnemy());
			holder.tv_away.setText(Account.getInstance().getTeam());
		}
	}

	@Override
	public int getItemCount() {
		return mList.size();
	}

	static class ViewHolder extends RecyclerView.ViewHolder {
		@BindView(R.id.tv_date)
		TextView tv_date;
		@BindView(R.id.tv_place)
		TextView tv_place;
		@BindView(R.id.tv_home)
		TextView tv_home;
		@BindView(R.id.tv_away)
		TextView tv_away;

		public ViewHolder(View itemView) {
			super(itemView);
			ButterKnife.bind(this, itemView);
		}
	}

}
