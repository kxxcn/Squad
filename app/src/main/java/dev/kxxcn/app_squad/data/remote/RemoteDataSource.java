package dev.kxxcn.app_squad.data.remote;

import dev.kxxcn.app_squad.data.DataSource;

/**
 * Created by kxxcn on 2018-05-01.
 */

public class RemoteDataSource extends DataSource {

	private static RemoteDataSource remoteDataSource;

	public static synchronized RemoteDataSource getInstance() {
		if (remoteDataSource == null) {
			remoteDataSource = new RemoteDataSource();
		}
		return remoteDataSource;
	}

	@Override
	public void getPhotos(GetPhotosCallback callback) {

	}

}
