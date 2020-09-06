package com.hjk.music_3.Service;

import android.app.Application;

public class MusicApplication extends Application {
    private static MusicApplication musicApplication;
    private static MusicServiceImpl musicService;

    private static  MusicApplication youTubeApplication;



    @Override
    public void onCreate(){
        super.onCreate();

        musicApplication=this;
        musicService=new MusicServiceImpl(getApplicationContext());



    }

    public static MusicApplication getInstance(){
        return musicApplication;
    }

    public static MusicServiceImpl getServiceInterface() { return musicService; }




}
