package dev.kxxcn.app_squad.ui.main.list.matchlist;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import dev.kxxcn.app_squad.R;
import dev.kxxcn.app_squad.data.model.Information;
import dev.kxxcn.app_squad.util.StateButton;

/**
 * Created by kxxcn on 2018-05-18.
 */

public class MatchListAdapter extends RecyclerView.Adapter<MatchListAdapter.ViewHolder> {

	private Context mContext;
	private List<Information> mList;

	public MatchListAdapter(Context context, List<Information> list) {
		this.mContext = context;
		this.mList = list;
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list, parent, false);
		return new ViewHolder(view);
	}

	@Override
	public void onBindViewHolder(ViewHolder holder, int position) {
		holder.tv_team.setText(mList.get(holder.getAdapterPosition()).getTeam());
		holder.tv_region.setText(mList.get(holder.getAdapterPosition()).getRegion());
		holder.tv_schedule.setText(String.format(mContext.getString(R.string.list_schedule),
				mList.get(holder.getAdapterPosition()).getDate(), mList.get(holder.getAdapterPosition()).getTime().replace(" ", "")));
		holder.tv_place.setText(mList.get(holder.getAdapterPosition()).getPlace());
	}

	@Override
	public int getItemCount() {
		return mList.size();
	}

	class ViewHolder extends RecyclerView.ViewHolder {
		@BindView(R.id.tv_team)
		TextView tv_team;
		@BindView(R.id.tv_region)
		TextView tv_region;
		@BindView(R.id.tv_schedule)
		TextView tv_schedule;
		@BindView(R.id.tv_place)
		TextView tv_place;

		@BindView(R.id.btn_match)
		StateButton btn_match;

		public ViewHolder(View itemView) {
			super(itemView);
			ButterKnife.bind(this, itemView);
		}
	}

}
