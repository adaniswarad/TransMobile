package com.kreatidea.transmobile.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Dennis on 10/19/2018.
 */
public class Client {

    private static Retrofit getClient() {
        String BASE_URL = "http://192.168.43.195/transvision/api/";
        Gson gson = new GsonBuilder().setLenient().create();
        final OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .readTimeout(60, TimeUnit.SECONDS)
                .connectTimeout(60, TimeUnit.SECONDS)
                .build();
        return new Retrofit.Builder().baseUrl(BASE_URL).client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
    }

    public static TransvisionApi getInstanceRetrofit() {
        return getClient().create(TransvisionApi.class);
    }
}
