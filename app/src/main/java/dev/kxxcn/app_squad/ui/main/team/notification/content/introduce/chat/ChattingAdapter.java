package dev.kxxcn.app_squad.ui.main.team.notification.content.introduce.chat;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import dev.kxxcn.app_squad.R;
import dev.kxxcn.app_squad.data.model.Chatting;

/**
 * Created by kxxcn on 2018-07-10.
 */

public class ChattingAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

	private static final int VIEW_SEND = 0;
	private static final int VIEW_RECEIVE = 1;

	private List<Chatting> mChattingList;
	private String mUid;

	public ChattingAdapter(List<Chatting> chattingList, String uid) {
		this.mChattingList = chattingList;
		this.mUid = uid;
	}

	@Override
	public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		if (viewType == VIEW_SEND) {
			View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_send, parent, false);
			return new SendHolder(v);
		} else if (viewType == VIEW_RECEIVE) {
			View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_receive, parent, false);
			return new ReceiveHolder(v);
		}
		return null;
	}

	@Override
	public int getItemViewType(int position) {
		if (mChattingList.get(position).getUid().equals(mUid)) {
			return VIEW_SEND;
		} else {
			return VIEW_RECEIVE;
		}
	}

	@Override
	public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
		if (holder instanceof SendHolder) {
			((SendHolder) holder).tv_message.setText(mChattingList.get(position).getMessage());
			((SendHolder) holder).tv_time.setText(mChattingList.get(position).getTime());
		} else if (holder instanceof ReceiveHolder) {
			((ReceiveHolder) holder).tv_message.setText(mChattingList.get(position).getMessage());
			((ReceiveHolder) holder).tv_time.setText(mChattingList.get(position).getTime());
			((ReceiveHolder) holder).tv_team.setText(mChattingList.get(position).getFrom());
		}
	}

	@Override
	public int getItemCount() {
		return mChattingList.size();
	}

	public class SendHolder extends RecyclerView.ViewHolder {
		@BindView(R.id.tv_message)
		TextView tv_message;
		@BindView(R.id.tv_time)
		TextView tv_time;

		public SendHolder(View view) {
			super(view);
			ButterKnife.bind(this, view);
		}
	}

	public class ReceiveHolder extends RecyclerView.ViewHolder {
		@BindView(R.id.tv_message)
		TextView tv_message;
		@BindView(R.id.tv_time)
		TextView tv_time;
		@BindView(R.id.tv_team)
		TextView tv_team;

		public ReceiveHolder(View view) {
			super(view);
			ButterKnife.bind(this, view);
		}
	}

}
