package dev.kxxcn.app_squad.view.match;

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
 * Created by Jun on 2018-04-26.
 */

public class MatchAdpater extends PagerAdapter {

	@BindView(R.id.cardView)
	CardView cardView;

	private LayoutInflater mLayoutInflater;

	private final Utils.LibraryObject[] LIBRARIES = new Utils.LibraryObject[]{
			new Utils.LibraryObject(
					R.drawable.stadium,
					"매치등록"
			),
			new Utils.LibraryObject(
					R.drawable.player,
					"용병모집"
			),
			new Utils.LibraryObject(
					R.drawable.football,
					"용병등록"
			),
	};

	public MatchAdpater(Context context) {
		mLayoutInflater = LayoutInflater.from(context);
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
		cardView.setOnClickListener(new View.OnClickListener() {
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
