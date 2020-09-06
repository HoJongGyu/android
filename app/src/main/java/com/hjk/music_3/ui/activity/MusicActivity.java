package com.hjk.music_3.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;


import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.hjk.music_3.R;
import com.hjk.music_3.Service.MusicApplication;
import com.hjk.music_3.data.local.model.Music;
import com.hjk.music_3.databinding.ActivityMainBinding;
import com.hjk.music_3.ui.fragment.MusicFragment;
import com.hjk.music_3.ui.fragment.ProfileFragment;
import com.hjk.music_3.ui.fragment.YouTubeFragment;

import com.hjk.music_3.ui.viewmodel.MusicViewModel;
import com.hjk.music_3.ui.viewmodel.UserViewModel;
import com.hjk.music_3.ui.viewmodel.YouTubeViewModel;
import com.hjk.music_3.utils.ActivityUtils;
import com.hjk.music_3.utils.Binding;

public class MusicActivity extends AppCompatActivity {

    MusicViewModel musicViewModel;
    UserViewModel userViewModel;
    YouTubeViewModel youtubeViewModel;
    ActivityMainBinding binding;

    Thread thread;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding= DataBindingUtil.setContentView(this,R.layout.activity_main);
        binding.setMusic(musicViewModel);

        binding.setActivity(this);

        setupViewFragment();
        setupBottomNavigation();

    }

    @Override
    public void onResume(){
        super.onResume();

        if(!MusicApplication.getInstance().getServiceInterface().getInit()){
            binding.mini.setVisibility(View.GONE);
        }
        else{
            binding.mini.setVisibility(View.VISIBLE);
        }


        musicViewModel=ViewModelProviders.of(this).get(MusicViewModel.class);

        musicViewModel.getIsPlaying().observe(this,new Observer<Boolean>(){
           @Override
           public void onChanged(Boolean aBoolean) {
               if(aBoolean){
                   binding.playBtn.setImageResource(R.drawable.ic_pause_48dp);
               }else{
                   binding.playBtn.setImageResource(R.drawable.ic_play_arrow_48dp);
               }
           }
        });

        musicViewModel.getProgress().observe(this,new Observer<String>(){
           @Override
            public void onChanged(String string){
               binding.musicTime.setText(string);
           }
        });

        musicViewModel.current_music().observe(this,new Observer<Music>(){
            @Override
            public void onChanged(Music music){
                binding.musicTitle.setText(music.getTitle());
                Binding.PicassoImage(binding.musicImage, music.getImage());

            }
        });


        //뮤직 선택후 다시 UI업데이트를 위해
    }


    public void Intent_Current_Music(){
        Intent intent=new Intent(MusicActivity.this, PlayerActivity.class);
        intent.putExtra("bno",musicViewModel.getPos());
        startActivity(intent);
    }




    public void start_pause(){
        if(musicViewModel.getIsPlaying().getValue()){
            MusicApplication.getInstance().getServiceInterface().pause();

        }
        else{
            MusicApplication.getInstance().getServiceInterface().start();


        }
    }

    public void next()   {
        MusicApplication.getInstance().getServiceInterface().next();

    }

    public void prev(){
        MusicApplication.getInstance().getServiceInterface().prev();

    }

    private void setupViewFragment(){
        MusicFragment musicFragment=MusicFragment.newInstance();
        ActivityUtils.replaceFragmentInActivity(
                getSupportFragmentManager(),musicFragment,R.id.fragment_container);
    }


    private void setupBottomNavigation(){
        BottomNavigationView bottomNav=findViewById(R.id.bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch(item.getItemId()) {
                    case R.id.action1:
                        ActivityUtils.replaceFragmentInActivity(
                                getSupportFragmentManager(), MusicFragment.newInstance(),
                                R.id.fragment_container);
                        return true;

                    case R.id.action2:
                        ActivityUtils.replaceFragmentInActivity(
                                getSupportFragmentManager(), YouTubeFragment.newInstance(),
                                R.id.fragment_container);
                        return true;


                    case R.id.action3:
                        ActivityUtils.replaceFragmentInActivity(
                                getSupportFragmentManager(), ProfileFragment.newInstance(),
                                R.id.fragment_container);
                        return true;
                }

                return false;
            }
        });
    }



}

//2020 08 25
//첫번째 프래그먼트와 3번째 프래그먼트 안에 액티비티 값을 일치시켰다
// java.lang.NullPointerException: Attempt to invoke virtual method 'java.lang.String 미해결
//2020 08 27 유튜브 자동재생, 카드뷰 크게 나오는 문제
//브로드 캐스트 작업, 유튜브링크랑 일반링크랑 구분
//자동재생 해결, 유튜브 백그라운드 재생 작업 고민 <- 어려워보임, 삭제기능 개발하기
// 노래재생중에 내 어플에 재생시 다른 어플 음악 중지하기 개발하기
// 노래시간표시 서비스로 옮김, 구현 완료
// 노티피케이션 플레이어바 버튼 안 바뀜
// 노티피케이션에도 시간 표시 하려했지만 ViewmodelsProviders를 사용할수 없었따
// 추후 해결 검토