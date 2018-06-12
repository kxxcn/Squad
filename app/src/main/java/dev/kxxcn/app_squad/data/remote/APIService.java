package dev.kxxcn.app_squad.data.remote;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import dev.kxxcn.app_squad.data.model.message.Send;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

import static dev.kxxcn.app_squad.data.remote.APIPersistence.DATE_FORMAT;
import static dev.kxxcn.app_squad.data.remote.APIPersistence.FCM_SERVER_KEY;
import static dev.kxxcn.app_squad.data.remote.APIPersistence.FCM_SERVER_URL;
import static dev.kxxcn.app_squad.data.remote.APIPersistence.SEND_MESSAGE;

/**
 * Created by kxxcn on 2018-05-01.
 */

public interface APIService {

	class Factory {
		static APIService create() {
			Gson gson = new GsonBuilder()
					.setDateFormat(DATE_FORMAT)
					.create();
			GsonConverterFactory gsonConverterFactory = GsonConverterFactory.create(gson);

			HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
			loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

			OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
			httpClient.addInterceptor(loggingInterceptor);

			Retrofit retrofit = new Retrofit.Builder()
					.baseUrl(FCM_SERVER_URL)
					.addConverterFactory(gsonConverterFactory)
					.client(httpClient.build())
					.build();
			return retrofit.create(APIService.class);
		}
	}

	@Headers({
			"Authorization: key=" + FCM_SERVER_KEY,
			"Accept: application/json",
			"Content-type: application/json"
	})
	@POST(SEND_MESSAGE)
	Call<Void> sendMessage(@Body Send send);

}
