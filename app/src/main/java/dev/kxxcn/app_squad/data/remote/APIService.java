package dev.kxxcn.app_squad.data.remote;

import retrofit2.Retrofit;

/**
 * Created by kxxcn on 2018-05-01.
 */

public interface APIService {

	class Factory {
		public static APIService create() {
			Retrofit retrofit = new Retrofit.Builder()
					.baseUrl("")
					.build();
			return retrofit.create(APIService.class);
		}
	}

}
