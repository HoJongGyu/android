package com.hjk.music_3.ui.viewmodel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.hjk.music_3.data.local.model.YouTube;
import com.hjk.music_3.data.remote.YouTubeRepository;

import java.util.List;

public class YouTubeViewModel extends AndroidViewModel {

    private static MutableLiveData<List<YouTube>> youtube;
    private static YouTube current_youtube;
    private static YouTubeRepository youTubeRepository=YouTubeRepository.getInstance();
    private static YouTubeRepository youtubeRepository_local;
    private LiveData<List<YouTube>> AllYouTube;

    public YouTubeViewModel(Application application){
        super(application);
        youtubeRepository_local=new YouTubeRepository(application);
        AllYouTube=youtubeRepository_local.getAllYouTube();

    }


    public void init(){
        if(youtube!=null)
            return;
        youtube=youTubeRepository.getYouTube();
    }

    public LiveData<List<YouTube>> getAllYouTube() {
        return AllYouTube;
    }

    public static void insert(YouTube youtube){youtubeRepository_local.insert(youtube);}


    public static MutableLiveData<List<YouTube>> getYoutube(){return youtube;}

    public static YouTube current_youtube(){return current_youtube;}

    public static void set_current_youtube(YouTube youtube){
        current_youtube=youtube;
    }
}
