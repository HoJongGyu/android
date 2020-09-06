package com.hjk.music_3.data.remote.api;

import androidx.lifecycle.LiveData;

import com.hjk.music_3.data.local.model.Music;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface MusicService {

    @GET("music_list")
    Call<List<Music>> getMusic();

    @GET("music_list/{bno}")
    Call<Music> selectMusic(@Path("bno")int bno);

    @FormUrlEncoded
    @POST("music_insert")
    Call<ResponseBody> music_insert(@Field("objJson") String objJson);



}
