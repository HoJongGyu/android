package com.hjk.music_3.data.remote;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.hjk.music_3.data.local.MusicDatabases;
import com.hjk.music_3.data.local.YouTubeDatabases;
import com.hjk.music_3.data.local.dao.MusicDao;
import com.hjk.music_3.data.local.dao.YouTubeDao;
import com.hjk.music_3.data.local.model.Music;
import com.hjk.music_3.data.local.model.YouTube;
import com.hjk.music_3.data.remote.api.MusicService;
import com.hjk.music_3.data.remote.api.RetrofitService;
import com.hjk.music_3.data.remote.api.YouTubeService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class YouTubeRepository {

    private YouTubeService youTubeService;
    private static YouTubeRepository youTubeRepository;

    private static YouTubeDao youtubeDao;
    private static LiveData<List<YouTube>> AllYouTube;

    public static YouTubeRepository getInstance(){
        if(youTubeRepository==null)
            youTubeRepository=new YouTubeRepository();
        return youTubeRepository;
    }

    private YouTubeRepository(){
        youTubeService= RetrofitService.getRetro().create(YouTubeService.class);
    }

    public YouTubeRepository(Application application){
        YouTubeDatabases db= YouTubeDatabases.getInstance(application);
        youtubeDao=db.youtubeDao();
        AllYouTube=youtubeDao.getAllYouTube();
    }

    public LiveData<List<YouTube>> getAllYouTube(){return AllYouTube;}

    public static void insert(YouTube youtube){
        YouTubeDatabases.databaseWriteExecutor.execute(()->{
            youtubeDao.insert_youtube(youtube);
        });
    }


    public MutableLiveData<List<YouTube>> getYouTube(){
        MutableLiveData<List<YouTube>> music=new MutableLiveData<>();

        youTubeService.getYouTube().enqueue(new Callback<List<YouTube>>(){
            @Override
            public void onResponse(Call<List<YouTube>> call, Response<List<YouTube>> response){
                if(response.isSuccessful()){
                    music.setValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<List<YouTube>> call, Throwable e){
                music.setValue(null);
            }
        });
        return music;
    }


}
