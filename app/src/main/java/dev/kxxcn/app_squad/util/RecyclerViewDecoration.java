package dev.kxxcn.app_squad.util;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;

/**
 * Created by kxxcn on 2018-07-10.
 */

public class RecyclerViewDecoration extends RecyclerView.ItemDecoration {

	private final int height;

	public RecyclerViewDecoration(int height) {
		this.height = height;
	}

	@Override
	public void getItemOffsets(Rect outRect, int itemPosition, RecyclerView parent) {
		super.getItemOffsets(outRect, itemPosition, parent);
		outRect.top = height;
	}

}
