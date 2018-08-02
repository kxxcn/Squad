package dev.kxxcn.app_squad.ui.main.team;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import dev.kxxcn.app_squad.R;
import dev.kxxcn.app_squad.data.model.Battle;
import dev.kxxcn.app_squad.data.remote.RemoteDataSource;
import dev.kxxcn.app_squad.util.threading.UiThread;

import static dev.kxxcn.app_squad.util.Constants.LOADING;

/**
 * Created by kxxcn on 2018-06-08.
 */

public class TeamAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

	private static final int VIEW_MATCH = 0;
	private static final int VIEW_RECRUITMENT = 1;
	private static final int VIEW_PLAYER = 2;

	private Context mContext;
	private List<Battle> mList;

	private String mTeam;

	public static boolean isClickedItem;

	private TeamContract.ItemClickListener mItemClickListener;

	ArrayList<LinearLayout> layoutList = new ArrayList<>(0);

	public TeamAdapter(Context context, List<Battle> list, String team, TeamContract.ItemClickListener itemClickListener) {
		this.mContext = context;
		this.mList = list;
		this.mTeam = team;
		this.mItemClickListener = itemClickListener;
		isClickedItem = false;
	}

	@Override
	public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		View view = null;
		if (viewType == VIEW_MATCH) {
			view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_team, parent, false);
			return new MatchHolder(view, mItemClickListener);
		} else if (viewType == VIEW_RECRUITMENT) {
			view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_team1, parent, false);
			return new RecruitmentHolder(view, mItemClickListener);
		} else if (viewType == VIEW_PLAYER) {
			view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_team2, parent, false);
			return new PlayerHolder(view, mItemClickListener);
		}
		return null;
	}

	@Override
	public int getItemViewType(int position) {
		if (mList.get(position).getFlag().equals(String.valueOf(RemoteDataSource.FLAG_MATCH_LIST))) {
			return VIEW_MATCH;
		} else if (mList.get(position).getFlag().equals(String.valueOf(RemoteDataSource.FLAG_RECRUITMENT_LIST))) {
			return VIEW_RECRUITMENT;
		} else {
			return VIEW_PLAYER;
		}
	}

	@Override
	public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
		if (holder instanceof MatchHolder) {
			Glide.with(mContext).load(R.drawable.team_list).diskCacheStrategy(DiskCacheStrategy.NONE).into(((MatchHolder) holder).iv_background);
			((MatchHolder) holder).tv_date.setText(getFormattedDate(mList.get(holder.getAdapterPosition()).getDate()));
			((MatchHolder) holder).tv_place.setText(mList.get(holder.getAdapterPosition()).getPlace());
			if (mList.get(holder.getAdapterPosition()).isHome()) {
				((MatchHolder) holder).tv_home.setText(mTeam);
				((MatchHolder) holder).tv_away.setText(mList.get(holder.getAdapterPosition()).getEnemy());
			} else {
				((MatchHolder) holder).tv_home.setText(mList.get(holder.getAdapterPosition()).getEnemy());
				((MatchHolder) holder).tv_away.setText(mTeam);
			}
			layoutList.add(((MatchHolder) holder).ll_receive);
		} else if (holder instanceof RecruitmentHolder) {
			Glide.with(mContext).load(R.drawable.team1_list).diskCacheStrategy(DiskCacheStrategy.NONE).into(((RecruitmentHolder) holder).iv_background);
			((RecruitmentHolder) holder).tv_date.setText(getFormattedDate(mList.get(holder.getAdapterPosition()).getDate()));
			((RecruitmentHolder) holder).tv_place.setText(mList.get(holder.getAdapterPosition()).getPlace());
			if (mList.get(holder.getAdapterPosition()).isHome()) {
				((RecruitmentHolder) holder).tv_comment.setText(String.format(mContext.getString(R.string.team_recruitment_home), mList.get(holder.getAdapterPosition()).getEnemy()));
			} else {
				((RecruitmentHolder) holder).tv_comment.setText(String.format(mContext.getString(R.string.team_recruitment_away), mList.get(holder.getAdapterPosition()).getEnemy()));
			}
			layoutList.add(((RecruitmentHolder) holder).ll_receive);
		} else if (holder instanceof PlayerHolder) {
			Glide.with(mContext).load(R.drawable.team2_list).diskCacheStrategy(DiskCacheStrategy.NONE).into(((PlayerHolder) holder).iv_background);
			((PlayerHolder) holder).tv_date.setText(getFormattedDate(mList.get(holder.getAdapterPosition()).getDate()));
			((PlayerHolder) holder).tv_place.setText(mList.get(holder.getAdapterPosition()).getPlace());
			if (mList.get(holder.getAdapterPosition()).isHome()) {
				((PlayerHolder) holder).tv_comment.setText(String.format(mContext.getString(R.string.team_player_home), mList.get(holder.getAdapterPosition()).getEnemy()));
			} else {
				((PlayerHolder) holder).tv_comment.setText(String.format(mContext.getString(R.string.team_player_away), mList.get(holder.getAdapterPosition()).getEnemy()));
			}
			layoutList.add(((PlayerHolder) holder).ll_receive);
		}
	}

	@Override
	public int getItemCount() {
		return mList.size();
	}

	static class MatchHolder extends RecyclerView.ViewHolder {
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

		@BindView(R.id.ll_receive)
		LinearLayout ll_receive;

		public MatchHolder(View itemView, final TeamContract.ItemClickListener itemClickListener) {
			super(itemView);
			ButterKnife.bind(this, itemView);
			cv_match.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					if (!isClickedItem) {
						itemClickListener.onClick(getAdapterPosition(), TeamFragment.BATTLE);
						isClickedItem = true;
					}
				}
			});
		}
	}

	static class RecruitmentHolder extends RecyclerView.ViewHolder {
		@BindView(R.id.cv_match)
		CardView cv_match;

		@BindView(R.id.iv_background)
		ImageView iv_background;

		@BindView(R.id.tv_date)
		TextView tv_date;
		@BindView(R.id.tv_place)
		TextView tv_place;
		@BindView(R.id.tv_comment)
		TextView tv_comment;

		@BindView(R.id.ll_receive)
		LinearLayout ll_receive;

		public RecruitmentHolder(View itemView, final TeamContract.ItemClickListener itemClickListener) {
			super(itemView);
			ButterKnife.bind(this, itemView);
			cv_match.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					if (!isClickedItem) {
						itemClickListener.onClick(getAdapterPosition(), TeamFragment.BATTLE);
						isClickedItem = true;
					}
				}
			});
		}
	}

	static class PlayerHolder extends RecyclerView.ViewHolder {
		@BindView(R.id.cv_match)
		CardView cv_match;

		@BindView(R.id.iv_background)
		ImageView iv_background;

		@BindView(R.id.tv_date)
		TextView tv_date;
		@BindView(R.id.tv_place)
		TextView tv_place;
		@BindView(R.id.tv_comment)
		TextView tv_comment;

		@BindView(R.id.ll_receive)
		LinearLayout ll_receive;

		public PlayerHolder(View itemView, final TeamContract.ItemClickListener itemClickListener) {
			super(itemView);
			ButterKnife.bind(this, itemView);
			cv_match.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					if (!isClickedItem) {
						itemClickListener.onClick(getAdapterPosition(), TeamFragment.BATTLE);
						isClickedItem = true;
					}
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

	public void receivedMessage(final int position) {
		try {
			layoutList.get(position).setVisibility(View.VISIBLE);
		} catch (RuntimeException e) {
			UiThread.getInstance().postDelayed(new Runnable() {
				@Override
				public void run() {
					receivedMessage(position);
				}
			}, LOADING);
			e.printStackTrace();
		}
	}

	public void readMessage(final int position) {
		try {
			layoutList.get(position).setVisibility(View.GONE);
		} catch (RuntimeException e) {
			UiThread.getInstance().postDelayed(new Runnable() {
				@Override
				public void run() {
					readMessage(position);
				}
			}, LOADING);
			e.printStackTrace();
		}
	}

	public void completedClick() {
		isClickedItem = false;
	}

}
