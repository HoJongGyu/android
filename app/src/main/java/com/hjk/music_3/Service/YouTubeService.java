package com.hjk.music_3.Service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import androidx.annotation.Nullable;
import androidx.lifecycle.LifecycleService;

import com.google.android.youtube.player.YouTubePlayer;
import com.hjk.music_3.data.local.model.YouTube;

public class YouTubeService extends LifecycleService {

    private YouTubePlayer player;

    private final IBinder binder=new YouTubeServiceBinder();

    public class YouTubeServiceBinder extends Binder {
        YouTubeService getService(){
            return YouTubeService.this;
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();

    }

    @Nullable

    @Override
    public IBinder onBind(Intent intent) {
        super.onBind(intent);
        return binder;
    }

    public void setPlayer(YouTubePlayer p){
        player=p;
    }

    public void play(){
        player.play();
    }

    public void pause(){
        player.pause();

    }
}
