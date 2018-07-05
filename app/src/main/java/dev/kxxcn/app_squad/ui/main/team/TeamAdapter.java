package dev.kxxcn.app_squad.ui.main.team;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import dev.kxxcn.app_squad.R;
import dev.kxxcn.app_squad.data.model.Battle;

/**
 * Created by kxxcn on 2018-06-08.
 */

public class TeamAdapter extends RecyclerView.Adapter<TeamAdapter.ViewHolder> {

	private Context mContext;
	private List<Battle> mList;

	private String mTeam;

	private TeamContract.ItemClickListener mItemClickListener;

	public TeamAdapter(Context context, List<Battle> list, String team, TeamContract.ItemClickListener itemClickListener) {
		this.mContext = context;
		this.mList = list;
		this.mTeam = team;
		this.mItemClickListener = itemClickListener;
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_team, parent, false);
		return new ViewHolder(view, mItemClickListener);
	}

	@Override
	public void onBindViewHolder(ViewHolder holder, int position) {
		Glide.with(mContext).load(R.drawable.team_list).diskCacheStrategy(DiskCacheStrategy.NONE).into(holder.iv_background);
		holder.tv_date.setText(getFormattedDate(mList.get(holder.getAdapterPosition()).getDate()));
		holder.tv_place.setText(mList.get(holder.getAdapterPosition()).getPlace());
		if (mList.get(holder.getAdapterPosition()).isHome()) {
			holder.tv_home.setText(mTeam);
			holder.tv_away.setText(mList.get(holder.getAdapterPosition()).getEnemy());
		} else {
			holder.tv_home.setText(mList.get(holder.getAdapterPosition()).getEnemy());
			holder.tv_away.setText(mTeam);
		}
	}

	@Override
	public int getItemCount() {
		return mList.size();
	}

	static class ViewHolder extends RecyclerView.ViewHolder {
		@BindView(R.id.cv_match)
		CardView cv_match;

		@BindView(R.id.iv_background)
		ImageView iv_background;

		@BindView(R.id.tv_date)
		TextView tv_date;
		@BindView(R.id.tv_place)
		TextView tv_place;
		@BindView(R.id.tv_home)
		TextView tv_home;
		@BindView(R.id.tv_away)
		TextView tv_away;

		public ViewHolder(View itemView, final TeamContract.ItemClickListener itemClickListener) {
			super(itemView);
			ButterKnife.bind(this, itemView);
			cv_match.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					itemClickListener.onClick(getAdapterPosition());
				}
			});
		}
	}

	private String getFormattedDate(String date) {
		String year = date.substring(0, 4);
		String month = date.substring(4, 6);
		String day = date.substring(6, 8);

		return String.format(mContext.getString(R.string.team_date), year, month, day);
	}

}
