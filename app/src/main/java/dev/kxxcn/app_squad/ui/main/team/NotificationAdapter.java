package dev.kxxcn.app_squad.ui.main.team;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import dev.kxxcn.app_squad.R;
import dev.kxxcn.app_squad.data.model.Notification;

/**
 * Created by kxxcn on 2018-06-14.
 */

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder> {

	List<Notification> mList;

	public NotificationAdapter(List<Notification> list) {
		this.mList = list;
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_notification, parent, false);
		return new NotificationAdapter.ViewHolder(view);
	}

	@Override
	public void onBindViewHolder(ViewHolder holder, int position) {
		holder.tv_message.setText(mList.get(holder.getAdapterPosition()).getMessage());
		holder.tv_time.setText(mList.get(holder.getAdapterPosition()).getTimestamp());
	}

	@Override
	public int getItemCount() {
		return mList.size();
	}

	static class ViewHolder extends RecyclerView.ViewHolder {
		@BindView(R.id.tv_message)
		TextView tv_message;
		@BindView(R.id.tv_time)
		TextView tv_time;
		@BindView(R.id.ib_arrow)
		ImageButton ib_arrow;

		public ViewHolder(View itemView) {
			super(itemView);
			ButterKnife.bind(this, itemView);
		}
	}

}
