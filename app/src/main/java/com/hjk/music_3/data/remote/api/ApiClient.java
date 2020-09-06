package com.hjk.music_3.data.remote.api;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {
    private static final String BASE_URL="http://these9909.cafe24.com/";

    private static final OkHttpClient client;

    private static MusicService sInstance;

    private static UserService sInstance_user;

    private static final Object sLock=new Object();

    static{
        HttpLoggingInterceptor logging=new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BASIC);

        client=new OkHttpClient.Builder()
                .addInterceptor(logging)
//                .addInterceptor(new AuthInterceptor())
                .build();
    }

    public static MusicService getInstance(){
        synchronized (sLock){
            if(sInstance==null){
                sInstance = getRetrofitInstance().create(MusicService.class);
            }
            return sInstance;
        }
    }

    public static UserService getInstance_User(){
        synchronized (sLock){
            if(sInstance_user==null){
                sInstance_user=getRetrofitInstance().create(UserService.class);
            }
            return sInstance_user;
        }
    }

    private static Retrofit getRetrofitInstance(){
        return new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();
    }
}
