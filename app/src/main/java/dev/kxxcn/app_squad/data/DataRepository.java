package dev.kxxcn.app_squad.data;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kxxcn on 2018-05-01.
 */

public class DataRepository {

	private DataSource dataSource;

	private static DataRepository dataRepository;

	List<String> repo = new ArrayList<>(0);

	public DataRepository(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	public static synchronized DataRepository getInstance(DataSource dataSource) {
		if (dataRepository == null) {
			dataRepository = new DataRepository(dataSource);
		}
		return dataRepository;
	}

	public void getPhotos(final DataSource.GetPhotosCallback callback) {
		dataSource.getPhotos(new DataSource.GetPhotosCallback() {
			@Override
			public void onSuccess(List<String> photos) {
				callback.onSuccess(repo);
			}

			@Override
			public void onFailure(Throwable throwable) {
				callback.onFailure(throwable);
			}

			@Override
			public void onNetworkFailure() {
				callback.onNetworkFailure();
			}
		});
	}

}
