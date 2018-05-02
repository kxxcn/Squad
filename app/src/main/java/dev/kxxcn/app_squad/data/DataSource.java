package dev.kxxcn.app_squad.data;

import java.util.List;

/**
 * Created by kxxcn on 2018-05-01.
 */

public abstract class DataSource {

	public interface GetPhotosCallback {
		void onSuccess(List<String> photos);

		void onFailure(Throwable throwable);

		void onNetworkFailure();

	}

	public abstract void getPhotos(GetPhotosCallback callback);

}
