package com.shenjing.dimension.dimension.base.request;


import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by plan on 17/9/3.
 */

public class HttpClient {

    private static HttpClient instance;

    private Retrofit retrofit;

/*    private HttpClient() {
        retrofit = new Retrofit.Builder()
                .client(getOkHttpClient())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl(BuildConfig.baseUrl)
                .build();
    }*/

    public <T> T createService(Class<T> serviceClass) {
        return retrofit.create(serviceClass);
    }

    public static HttpClient getInstance() {
        if (instance == null) {
            synchronized (HttpClient.class) {
                if (instance == null) {
                    instance = new HttpClient();
                }
            }
        }
        return instance;
    }

    private static OkHttpClient getOkHttpClient() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        return builder.retryOnConnectionFailure(true)
                .connectTimeout(20, TimeUnit.SECONDS)
                .readTimeout(20, TimeUnit.SECONDS)
               /* .addInterceptor(new BasicParamsInterceptor())
                .addInterceptor(new TokenInvalidInterceptor())*/
                .build();
    }
}
