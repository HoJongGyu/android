package com.hjk.music_3.Service;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.youtube.player.YouTubePlayer;

public class YouTubeServiceImpl extends AppCompatActivity {
    private static ServiceConnection serviceConnection;
    private static YouTubeService youTubeService;


    public YouTubeServiceImpl(Context context){



        serviceConnection=new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName componentName, IBinder service) {
                youTubeService=((YouTubeService.YouTubeServiceBinder)service).getService();

            }

            @Override
            public void onServiceDisconnected(ComponentName componentName) {
                serviceConnection=null;
                youTubeService=null;
            }
        };
        context.bindService(new Intent(context,YouTubeService.class).setPackage(context.getPackageName()),serviceConnection,Context.BIND_AUTO_CREATE);
    }

    public void setPlayer(YouTubePlayer youTubePlayer){
        youTubeService.setPlayer(youTubePlayer);
    }

    public void play(){
        youTubeService.play();
    }

    public void pause(){
        youTubeService.pause();
    }
}
