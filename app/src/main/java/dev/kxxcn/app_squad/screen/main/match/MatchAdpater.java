package dev.kxxcn.app_squad.screen.main.match;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.BindView;
import butterknife.ButterKnife;
import dev.kxxcn.app_squad.R;
import dev.kxxcn.app_squad.util.Utils;

import static dev.kxxcn.app_squad.util.Utils.setupItem;


/**
 * Created by kxxcn on 2018-04-26.
 */
public class MatchAdpater extends PagerAdapter {

	@BindView(R.id.cardView)
	CardView mCardView;

	private LayoutInflater mLayoutInflater;

	private final Utils.LibraryObject[] LIBRARIES;

	public MatchAdpater(Context context) {
		mLayoutInflater = LayoutInflater.from(context);

		LIBRARIES = new Utils.LibraryObject[]{
				new Utils.LibraryObject(
						R.drawable.stadium,
						context.getString(R.string.match_title_game_registration)
				),
				new Utils.LibraryObject(
						R.drawable.player,
						context.getString(R.string.match_title_recruitment)
				),
				new Utils.LibraryObject(
						R.drawable.football,
						context.getString(R.string.match_title_player_registration)
				),
		};
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
		final View view = mLayoutInflater.inflate(R.layout.item, container, false);
		ButterKnife.bind(this, view);
		setupItem(view, LIBRARIES[position]);
		mCardView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

			}
		});
		container.addView(view);
		return view;
	}

	@Override
	public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
		container.removeView((View) object);
	}

}
