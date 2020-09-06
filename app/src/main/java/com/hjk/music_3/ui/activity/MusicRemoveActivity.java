package com.hjk.music_3.ui.activity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

import com.hjk.music_3.R;
import com.hjk.music_3.data.local.model.Music;
import com.hjk.music_3.data.local.model.YouTube;
import com.hjk.music_3.databinding.ActivityMusicremoveBinding;
import com.hjk.music_3.ui.viewmodel.MusicViewModel;
import com.hjk.music_3.ui.viewmodel.YouTubeViewModel;
import com.hjk.music_3.utils.ToastUtils;

public class MusicRemoveActivity extends AppCompatActivity {
    Music music = new Music();
    YouTube youtube = new YouTube();

    ActivityMusicremoveBinding binding;
    MusicViewModel musicViewModel;
    YouTubeViewModel youTubeViewModel;
    @Override
    public void onCreate(Bundle save){
        super.onCreate(save);
        binding= DataBindingUtil.setContentView(this, R.layout.activity_musicremove);
        binding.setActivity(this);
        youTubeViewModel= ViewModelProviders.of(this).get(YouTubeViewModel.class);
        musicViewModel= ViewModelProviders.of(this).get(MusicViewModel.class);

    }

    public void music_delete(){
        String str=binding.mp3.getText().toString();

        if(str.length()==0){
            ToastUtils.set(getApplicationContext(),"음악 링크를 입력해주세요",2);
        }else{
            musicViewModel.deleteMusic(str);
            ToastUtils.set(getApplicationContext(),"음악이 삭제 되었습니다",2);
        }
    }

}