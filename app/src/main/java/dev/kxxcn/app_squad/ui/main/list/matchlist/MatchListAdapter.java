package dev.kxxcn.app_squad.ui.main.list.matchlist;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

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
	private MatchListContract.ItemClickListener mItemClickListener;

	public MatchListAdapter(Context context, List<Information> list, MatchListContract.ItemClickListener itemClickListener) {
		this.mContext = context;
		this.mList = list;
		this.mItemClickListener = itemClickListener;
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list, parent, false);
		return new ViewHolder(view, mItemClickListener);
	}

	@Override
	public void onBindViewHolder(final ViewHolder holder, final int position) {
		holder.tv_team.setText(mList.get(holder.getAdapterPosition()).getTeam());
		holder.tv_region.setText(mList.get(holder.getAdapterPosition()).getRegion());
		holder.tv_schedule.setText(String.format(mContext.getString(R.string.list_schedule),
				mList.get(holder.getAdapterPosition()).getDate(), mList.get(holder.getAdapterPosition()).getTime().replace(" ", "")));
		holder.tv_place.setText(mList.get(holder.getAdapterPosition()).getPlace());
		if (FirebaseAuth.getInstance().getCurrentUser().getEmail().equals(mList.get(holder.getAdapterPosition()).getEmail())) {
			holder.ll_mine.setVisibility(View.GONE);
			holder.tv_team.setTextColor(mContext.getResources().getColor(R.color.list_my_team));
		} else {
			holder.ll_mine.setVisibility(View.VISIBLE);
			holder.tv_team.setTextColor(mContext.getResources().getColor(R.color.list_team));
			if (mList.get(holder.getAdapterPosition()).isConnect()) {
				holder.btn_request.setEnabled(false);
				holder.btn_request.setRound(false);
				holder.btn_request.setNormalStrokeColor(mContext.getResources().getColor(R.color.list_linked));
				holder.btn_request.setNormalTextColor(mContext.getResources().getColor(R.color.list_linked));
			} else {
				holder.btn_request.setEnabled(true);
				holder.btn_request.setRound(true);
				holder.btn_request.setNormalStrokeColor(mContext.getResources().getColor(R.color.list_unlinked));
				holder.btn_request.setNormalTextColor(mContext.getResources().getColor(R.color.list_unlinked));
			}
		}
	}

	@Override
	public int getItemCount() {
		return mList.size();
	}

	static class ViewHolder extends RecyclerView.ViewHolder {
		@BindView(R.id.tv_team)
		TextView tv_team;
		@BindView(R.id.tv_region)
		TextView tv_region;
		@BindView(R.id.tv_schedule)
		TextView tv_schedule;
		@BindView(R.id.tv_place)
		TextView tv_place;

		@BindView(R.id.ll_mine)
		LinearLayout ll_mine;

		@BindView(R.id.btn_request)
		StateButton btn_request;

		public ViewHolder(View itemView, final MatchListContract.ItemClickListener itemClickListener) {
			super(itemView);
			ButterKnife.bind(this, itemView);
			btn_request.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					itemClickListener.onClick(getAdapterPosition());
				}
			});
		}
	}

}


