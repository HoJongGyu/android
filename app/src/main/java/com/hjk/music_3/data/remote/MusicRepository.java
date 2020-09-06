package com.hjk.music_3.data.remote;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.hjk.music_3.data.local.MusicDatabases;
import com.hjk.music_3.data.local.dao.MusicDao;
import com.hjk.music_3.data.local.model.Music;
import com.hjk.music_3.data.remote.api.MusicService;
import com.hjk.music_3.data.remote.api.RetrofitService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MusicRepository {

    private MusicService musicService;
    private static MusicRepository musicRepository;

    private static MusicDao musicDao;
    private static LiveData<List<Music>> AllMusic;

    public static MusicRepository getInstance(){
        if(musicRepository==null)
            musicRepository=new MusicRepository();
        return musicRepository;
    }

    private MusicRepository(){
        musicService= RetrofitService.getRetro().create(MusicService.class);
    }

    public MusicRepository(Application application){
        MusicDatabases db=MusicDatabases.getInstance(application);
        musicDao=db.musicDao();
        AllMusic=musicDao.getAllMusic();
    }

    public LiveData<List<Music>> getAllMusic(){return AllMusic;}

    public static void insert(Music music){
        MusicDatabases.databaseWriteExecutor.execute(()->{
            musicDao.insertMusic(music);
        });
    }

    public static void deleteMusic(String title){
        MusicDatabases.databaseWriteExecutor.execute(()->{
            musicDao.deleteMusic(title);
        });
    }

    public MutableLiveData<List<Music>> getMusic(){
        MutableLiveData<List<Music>> music=new MutableLiveData<>();

        musicService.getMusic().enqueue(new Callback<List<Music>>(){
            @Override
            public void onResponse(Call<List<Music>> call, Response<List<Music>> response){
                if(response.isSuccessful()){
                    music.setValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<List<Music>> call, Throwable e){
                music.setValue(null);
            }
        });
        return music;
    }

    public MutableLiveData<Music> selectMusic(int bno){
        MutableLiveData<Music> current_music=new MutableLiveData<>();

        musicService.selectMusic(bno).enqueue(new Callback<Music>(){
            @Override
            public void onResponse(Call<Music> call, Response<Music> response){
                if(response.isSuccessful()){
                    current_music.setValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<Music> call, Throwable e){
                current_music.setValue(null);
            }
        });
        return current_music;
    }
}
