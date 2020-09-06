package com.hjk.music_3.Service;

import android.app.Application;

public class YouTubeApplication extends Application {
    private static  YouTubeApplication youTubeApplication;
    private static YouTubeServiceImpl youTubeService;

    @Override
    public void onCreate(){
        super.onCreate();

        youTubeApplication=this;
        youTubeService=new YouTubeServiceImpl(getApplicationContext());
    }

    public static YouTubeApplication getInstance(){
        return youTubeApplication;
    }

    public static YouTubeServiceImpl getServiceInterface() { return youTubeService; }



}
