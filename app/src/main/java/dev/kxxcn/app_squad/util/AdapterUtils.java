package dev.kxxcn.app_squad.util;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import dev.kxxcn.app_squad.R;

/**
 * Created by kxxcn on 2018-04-26.
 */
public class AdapterUtils {

	public static void setupItem(final View view, final LibraryObject libraryObject, int color) {
		final TextView txt = view.findViewById(R.id.txt_item);
		txt.setText(libraryObject.getTitle());
		txt.setTextColor(color);

		final ImageView img = view.findViewById(R.id.img_item);
		Glide.with(view.getContext()).load(libraryObject.getRes()).override(250, 250).into(img);
	}

	public static class LibraryObject {

		private String mTitle;
		private int mRes;

		public LibraryObject(final int res, final String title) {
			mRes = res;
			mTitle = title;
		}

		public String getTitle() {
			return mTitle;
		}

		public void setTitle(final String title) {
			mTitle = title;
		}

		public int getRes() {
			return mRes;
		}

		public void setRes(final int res) {
			mRes = res;
		}
	}

}
