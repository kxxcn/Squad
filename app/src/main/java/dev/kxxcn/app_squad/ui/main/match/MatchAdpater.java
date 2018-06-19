package dev.kxxcn.app_squad.ui.main.match;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import butterknife.BindView;
import butterknife.ButterKnife;
import dev.kxxcn.app_squad.R;
import dev.kxxcn.app_squad.util.AdapterUtils;

import static dev.kxxcn.app_squad.util.AdapterUtils.setupItem;


/**
 * Created by kxxcn on 2018-04-26.
 */
public class MatchAdpater extends PagerAdapter {

	private static final int MATCH = 0;
	private static final int RECRUITMENT = 1;
	private static final int PLAYER = 2;

	@BindView(R.id.rl_background)
	RelativeLayout mBackground;

	@BindView(R.id.cardView)
	CardView mCardView;

	@BindView(R.id.iv_match_card)
	ImageView iv_match_card;

	private Activity mActivity;
	private Context mContext;

	private LayoutInflater mLayoutInflater;

	private final AdapterUtils.LibraryObject[] LIBRARIES;

	private String[] mBackgroundColors;
	private String[] mHighlightColors;
	private int txtColor;
	private int[] imgs = {R.drawable.card_match, R.drawable.card_recruit, R.drawable.card_player};

	public MatchAdpater(Activity activity, Context context) {
		mActivity = activity;
		mContext = context;
		mLayoutInflater = LayoutInflater.from(context);

		LIBRARIES = new AdapterUtils.LibraryObject[]{
				new AdapterUtils.LibraryObject(
						R.drawable.stadium,
						context.getString(R.string.match_title_match)
				),
				new AdapterUtils.LibraryObject(
						R.drawable.player,
						context.getString(R.string.match_title_recruitment)
				),
				new AdapterUtils.LibraryObject(
						R.drawable.football,
						context.getString(R.string.match_title_player)
				),
		};

		mBackgroundColors = context.getResources().getStringArray(R.array.match_card_background);
		mHighlightColors = context.getResources().getStringArray(R.array.match_card_highlight);
		txtColor = context.getResources().getColor(R.color.match_font);
	}

	@Override
	public int getCount() {
		return LIBRARIES.length;
	}

	@Override
	public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
		return view.equals(object);
	}

	@Override
	public int getItemPosition(@NonNull Object object) {
		return POSITION_NONE;
	}

	@Override
	@NonNull
	public Object instantiateItem(@NonNull ViewGroup container, final int position) {
		final View view = mLayoutInflater.inflate(R.layout.item_match, container, false);
		ButterKnife.bind(this, view);
		initUI(position);
		setupItem(view, LIBRARIES[position], txtColor);
		mCardView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				showRegistrationPage(position);
			}
		});
		container.addView(view);
		return view;
	}

	@Override
	public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
		container.removeView((View) object);
	}

	private void initUI(int position) {
		Glide.with(mContext).load(imgs[position]).diskCacheStrategy(DiskCacheStrategy.NONE).into(iv_match_card);
	}

	private void showRegistrationPage(int position) {
		MatchDialog dialog = new MatchDialog(mActivity, mContext, position, null);
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
