package dev.kxxcn.app_squad.ui.main.team.notification.content.introduce.chat;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dev.kxxcn.app_squad.R;
import dev.kxxcn.app_squad.data.DataRepository;
import dev.kxxcn.app_squad.data.model.Chatting;
import dev.kxxcn.app_squad.data.model.User;
import dev.kxxcn.app_squad.data.remote.RemoteDataSource;
import dev.kxxcn.app_squad.util.DialogUtils;
import dev.kxxcn.app_squad.util.RecyclerViewDecoration;

/**
 * Created by kxxcn on 2018-07-10.
 */

public class ChattingDialog extends DialogFragment implements ChattingContract.View {

	private static final String USER = "object";
	private static final String FROM = "from";
	private static final String ENEMY_UID = "enemy_uid";
	private static final String UID = "uid";
	private static final String MATCH_DAY = "day";

	public static String ROOM_NAME = "";
	public static String DAY = "";

	@BindView(R.id.tv_receiver)
	TextView tv_receiver;

	@BindView(R.id.et_message)
	EditText et_message;

	@BindView(R.id.rv_chat)
	RecyclerView rv_chat;

	private ChattingContract.Presenter mPresenter;

	private User mEnemy;
	private String mFrom;
	private String roomName;
	private String matchDay;

	public static ChattingDialog newInstance(User user, String from, String enemy_uid, String uid, String matchDay) {
		ChattingDialog dialog = new ChattingDialog();

		Bundle args = new Bundle();
		args.putParcelable(USER, user);
		args.putString(FROM, from);
		args.putString(ENEMY_UID, enemy_uid);
		args.putString(UID, uid);
		args.putString(MATCH_DAY, matchDay);

		dialog.setArguments(args);
		return dialog;
	}

	@Override
	public void setPresenter(ChattingContract.Presenter presenter) {
		this.mPresenter = presenter;
	}

	@Override
	public void showLoadingIndicator(boolean isShowing) {

	}

	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.dialog_chat, container, false);
		ButterKnife.bind(this, view);
		getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

		new ChattingPresenter(this, DataRepository.getInstance(RemoteDataSource.getInstance(
				FirebaseAuth.getInstance(), FirebaseDatabase.getInstance().getReference())));

		initUI();

		matchDay = getArguments().getString(MATCH_DAY);
		String[] roomList = {getArguments().getString(ENEMY_UID), getArguments().getString(UID)};
		Arrays.sort(roomList);
		roomName = roomList[0] + roomList[1];
		mPresenter.onSubscribe(matchDay, roomName);

		rv_chat.addItemDecoration(new RecyclerViewDecoration(30));

		return view;
	}

	@Override
	public void onStart() {
		super.onStart();
		Dialog dialog = getDialog();
		if (dialog != null) {
			int width = ViewGroup.LayoutParams.MATCH_PARENT;
			int height = ViewGroup.LayoutParams.MATCH_PARENT;
			dialog.getWindow().setLayout(width, height);
			ROOM_NAME = roomName;
			DAY = matchDay;
		}
	}

	private void initUI() {
		setCancelable(false);
		mEnemy = getArguments().getParcelable(USER);
		mFrom = getArguments().getString(FROM);
		tv_receiver.setText(mEnemy.getTeam());
	}

	@OnClick(R.id.ib_cancel)
	public void onCancel() {
		ROOM_NAME = "";
		DAY = "";
		dismiss();
	}

	@OnClick(R.id.ib_chat)
	public void onChat() {
		if (!TextUtils.isEmpty(et_message.getText())) {
			mPresenter.onChat(new Chatting(et_message.getText().toString(), mFrom, getArguments().getString(UID),
					DialogUtils.getTime()), getString(R.string.app_name), matchDay, roomName);
		}
	}

	@Override
	public void showSuccessfullySend() {
		et_message.setText(null);
	}

	@Override
	public void showUnsuccessfullySend() {
		Toast.makeText(getContext(), getString(R.string.chatting_failure_send), Toast.LENGTH_SHORT).show();
	}

	@Override
	public void showChattingList(List<Chatting> chattingList) {
		if (!ROOM_NAME.equals("") && !DAY.equals("")) {
			rv_chat.setAdapter(new ChattingAdapter(chattingList, getArguments().getString(UID)));
			rv_chat.smoothScrollToPosition(chattingList.size());
			for (int i = 0; i < chattingList.size(); i++) {
				if (!chattingList.get(i).getUid().equals(getArguments().getString(UID))) {
					chattingList.get(i).setCheck(true);
				}
			}
			mPresenter.onUpdateReadMessages(chattingList, roomName, matchDay);
		}
	}

}
