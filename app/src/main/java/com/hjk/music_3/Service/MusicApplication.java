package com.hjk.music_3.Service;

import android.app.Application;

public class MusicApplication extends Application {
    private static MusicApplication musicApplication;
    private static MusicServiceImpl musicService;

    private static  MusicApplication youTubeApplication;
    private static YouTubeServiceImpl youTubeService;


    @Override
    public void onCreate(){
        super.onCreate();

        musicApplication=this;
        musicService=new MusicServiceImpl(getApplicationContext());


        youTubeService=new YouTubeServiceImpl(getApplicationContext());
    }

    public static MusicApplication getInstance(){
        return musicApplication;
    }

    public static MusicServiceImpl getServiceInterface() { return musicService; }

    public static YouTubeServiceImpl y_getServiceInterface() { return youTubeService; }





}
