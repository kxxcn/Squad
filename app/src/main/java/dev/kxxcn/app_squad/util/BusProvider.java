package dev.kxxcn.app_squad.util;

import android.os.Handler;
import android.os.Looper;

import com.squareup.otto.Bus;


/**
 * Created by kxxcn on 2018-06-20.
 */

public class BusProvider extends Bus {
	private static final Bus BUS = new BusProvider();
	private final Handler mHandler = new Handler(Looper.getMainLooper());

	@Override
	public void post(final Object event) {
		if (Looper.myLooper() == Looper.getMainLooper()) {
			super.post(event);
		} else {
			mHandler.post(new Runnable() {
				@Override
				public void run() {
					BusProvider.super.post(event);
				}
			});
		}
	}

	public static Bus getInstance() {
		return BUS;
	}

	private BusProvider() {

	}
}
