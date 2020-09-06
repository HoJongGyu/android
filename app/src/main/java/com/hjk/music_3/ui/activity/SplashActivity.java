package com.hjk.music_3.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import com.hjk.music_3.R;
import com.hjk.music_3.Service.MusicApplication;
import com.hjk.music_3.data.local.model.Music;
import com.hjk.music_3.data.local.model.User;
import com.hjk.music_3.ui.viewmodel.BackViewModel;
import com.hjk.music_3.ui.viewmodel.MusicViewModel;
import com.hjk.music_3.ui.viewmodel.UserViewModel;

import com.hjk.music_3.ui.viewmodel.YouTubeViewModel;

public class SplashActivity extends AppCompatActivity {

    UserViewModel userViewModel;
    BackViewModel backViewModel;
    MusicViewModel musicViewModel;
    YouTubeViewModel youTubeModel;


    Intent intent=getIntent();
    User user=new User();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        userViewModel = ViewModelProviders.of(this).get(UserViewModel.class);
        userViewModel.init();

   userViewModel.get_local_user().observe(this,m->{

   });

        musicViewModel=ViewModelProviders.of(this).get(MusicViewModel.class);

        musicViewModel.getAllMusic().observe(this,m->{
            int pos_;
            if(UserViewModel.load_save_music()!=0){
                pos_=UserViewModel.load_save_music();
            }
            else{
                pos_=0;
            }

            if(m.size()==0) {
                musicViewModel.set_current_music(9999);
              }
            else{
                musicViewModel.set_current_music(pos_);
            }
        });

        youTubeModel=ViewModelProviders.of(this).get(YouTubeViewModel.class);

        backViewModel=ViewModelProviders.of(this).get(BackViewModel.class);
        backViewModel.init();

        backViewModel.getBack().observe(this,b->{
            int pos_;
            if(UserViewModel.load_save_back()!=0){
                pos_=UserViewModel.load_save_back();
            }
            else{
                pos_=0;
            }
            backViewModel.set_current_back(b.get(pos_));
        });

        Handler handler=new Handler();
        handler.postDelayed(new splash(),3000);

    }

    private class splash implements Runnable{
        public void run(){

            if(userViewModel.load_save_login()==0) {
                startActivity(new Intent(getApplication(), LoginActivity.class));

            }else {
                startActivity(new Intent(getApplication(), MusicActivity.class));
            }
            finish();
        }
    }
}
