package dev.kxxcn.app_squad.ui.main.list.playerlist;

import android.content.Context;
import android.support.v7.widget.CardView;
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
 * Created by kxxcn on 2018-07-18.
 */

public class PlayerListAdapter extends RecyclerView.Adapter<PlayerListAdapter.ViewHolder> {

	public static final int REQUEST = 0;
	public static final int INFORMATION = 1;

	private Context mContext;
	private List<Information> mList;
	private PlayerListContract.ItemClickListener mItemClickListener;

	public PlayerListAdapter(Context context, List<Information> list, PlayerListContract.ItemClickListener itemClickListener) {
		this.mContext = context;
		this.mList = list;
		this.mItemClickListener = itemClickListener;
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_playerlist, parent, false);
		return new PlayerListAdapter.ViewHolder(view, mItemClickListener);
	}

	@Override
	public void onBindViewHolder(ViewHolder holder, int position) {
		holder.tv_region.setText(mList.get(holder.getAdapterPosition()).getRegion());
		holder.tv_schedule.setText(String.format(mContext.getString(R.string.list_schedule),
				mList.get(holder.getAdapterPosition()).getDate(), mList.get(holder.getAdapterPosition()).getTime().replace(" ", "")));
		holder.tv_information.setText(String.format(mContext.getString(R.string.list_information),
				mList.get(holder.getAdapterPosition()).getAge(), mList.get(holder.getAdapterPosition()).getPeople()));
		if (FirebaseAuth.getInstance().getCurrentUser().getEmail().equals(mList.get(holder.getAdapterPosition()).getEmail())) {
			holder.ll_mine.setVisibility(View.GONE);
			holder.tv_region.setTextColor(mContext.getResources().getColor(R.color.list_my_team));
		} else {
			holder.ll_mine.setVisibility(View.VISIBLE);
			holder.tv_region.setTextColor(mContext.getResources().getColor(R.color.list_team));
			holder.btn_request.setEnabled(true);
			holder.btn_request.setRound(true);
			holder.btn_request.setNormalStrokeColor(mContext.getResources().getColor(R.color.list_unlinked));
			holder.btn_request.setNormalTextColor(mContext.getResources().getColor(R.color.list_unlinked));
			for (int i = 0; i < mList.get(holder.getAdapterPosition()).getJoin().size(); i++) {
				if (mList.get(holder.getAdapterPosition()).getJoin().get(i).equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
					holder.btn_request.setText(mContext.getString(R.string.list_checking));
					holder.btn_request.setOnClickListener(null);
					holder.btn_request.setRound(false);
					holder.btn_request.setNormalStrokeColor(mContext.getResources().getColor(R.color.list_linked));
					holder.btn_request.setNormalTextColor(mContext.getResources().getColor(R.color.list_linked));
					holder.btn_request.setPressedStrokeColor(mContext.getResources().getColor(R.color.list_linked));
					holder.btn_request.setPressedTextColor(mContext.getResources().getColor(R.color.list_linked));
					break;
				}
			}
		}
	}

	@Override
	public int getItemCount() {
		return mList.size();
	}

	static class ViewHolder extends RecyclerView.ViewHolder {
		@BindView(R.id.cv_list)
		CardView cv_list;

		@BindView(R.id.tv_region)
		TextView tv_region;
		@BindView(R.id.tv_schedule)
		TextView tv_schedule;
		@BindView(R.id.tv_information)
		TextView tv_information;

		@BindView(R.id.ll_mine)
		LinearLayout ll_mine;

		@BindView(R.id.btn_request)
		StateButton btn_request;

		public ViewHolder(View itemView, final PlayerListContract.ItemClickListener itemClickListener) {
			super(itemView);
			ButterKnife.bind(this, itemView);
			btn_request.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					itemClickListener.onClick(getAdapterPosition(), REQUEST);
				}
			});
			cv_list.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					itemClickListener.onClick(getAdapterPosition(), INFORMATION);
				}
			});
		}
	}

}
