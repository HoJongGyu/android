package com.hjk.music_3.data.remote.api;

import com.hjk.music_3.data.local.model.Music;
import com.hjk.music_3.data.local.model.YouTube;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface YouTubeService {

    @GET("youtube_list")
    Call<List<YouTube>> getYouTube();


    @FormUrlEncoded
    @POST("youtube_insert")
    Call<ResponseBody> youtube_insert(@Field("objJson") String objJson);

}
