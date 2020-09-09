package com.hjk.music_3.ui.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;


import com.andre_max.youtube_url_extractor.ExtractorException;
import com.andre_max.youtube_url_extractor.YoutubeStreamExtractor;
import com.andre_max.youtube_url_extractor.model.YTMedia;
import com.andre_max.youtube_url_extractor.model.YTSubtitles;
import com.andre_max.youtube_url_extractor.model.YoutubeMeta;
import com.google.gson.Gson;
import com.hjk.music_3.R;

import com.hjk.music_3.data.local.model.Music;
import com.hjk.music_3.data.local.model.YouTube;
import com.hjk.music_3.data.remote.api.MusicService;
import com.hjk.music_3.data.remote.api.RetrofitService;
import com.hjk.music_3.data.remote.api.YouTubeService;
import com.hjk.music_3.databinding.ActivityMusicaddBinding;
import com.hjk.music_3.ui.viewmodel.MusicViewModel;
import com.hjk.music_3.ui.viewmodel.YouTubeViewModel;
import com.hjk.music_3.utils.ToastUtils;

import java.util.List;

import at.huber.youtubeExtractor.VideoMeta;
import at.huber.youtubeExtractor.YouTubeExtractor;
import at.huber.youtubeExtractor.YtFile;


import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;



public class MusicAddActivity extends AppCompatActivity {
    Music music=new Music();
    YouTube youtube=new YouTube();

    ActivityMusicaddBinding binding;
    Gson gson=new Gson();

    MusicService musicService;
    YouTubeService youtubeService;

    private static final String TAG = MusicAddActivity.class.getSimpleName();
    MusicViewModel musicViewModel;
    YouTubeViewModel youTubeViewModel;
    String image;
    public static String result="";

    @Override
    public void onCreate(Bundle save){
        super.onCreate(save);
        binding= DataBindingUtil.setContentView(this, R.layout.activity_musicadd);
        binding.setActivity(this);
        youTubeViewModel= ViewModelProviders.of(this).get(YouTubeViewModel.class);
        musicViewModel=ViewModelProviders.of(this).get(MusicViewModel.class);

    }

    public void music_insert(){
        String str=binding.mp3.getText().toString();

        if(str.length()==0){
            ToastUtils.set(getApplicationContext(),"음악 링크를 입력해주세요",2);
        }else if(binding.title.getText().toString().length()==0){
            ToastUtils.set(getApplicationContext(),"타이틀을 입력해주세요",2);
        }
        else {
            if (str.contains("youtube")||str.contains("youtu.be")) {
                if(str.contains("youtu.be/"))
                str=str.replaceAll("youtu.be/","youtube.com/watch?v=");
//                youtube_extract(str);
                youtube(str);
            } else {
                if(binding.image.getText().toString().length()==0){
                    image="https://images.macrumors.com/t/vMbr05RQ60tz7V_zS5UEO9SbGR0=/1600x900/smart/article-new/2018/05/apple-music-note.jpg";
                }
                else{
                    image=binding.image.getText().toString();
                }
                music.setMp3(str);
                music.setTitle(binding.title.getText().toString());
                music.setImage(image);
                MusicViewModel.insert(music);

                String objJson = gson.toJson(music);
                musicService = RetrofitService.getRetro().create(MusicService.class);
                Call<ResponseBody> music = musicService.music_insert(objJson);


                music.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        try {
                            String result = response.body().string();
                            System.out.println(result);
                            if (result.equals("1")) {
                                Toast.makeText(getApplicationContext(), "음악 추가 완료", Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Toast.makeText(getApplicationContext(), "음악 추가 실패", Toast.LENGTH_SHORT).show();

                    }
                });
            }
        }
    }



    public void youtube_extract(String url){

        new YoutubeStreamExtractor(new YoutubeStreamExtractor.ExtractorListner(){
            @Override
            public void onExtractionDone(List<YTMedia> adativeStream, final List<YTMedia> muxedStream,List<YTSubtitles> subtitles, YoutubeMeta meta) {
                //url to get subtitle
                for (YTMedia media:adativeStream) {
                    result=media.getUrl();


                }

                youtube(result);

            }
            @Override
            public void onExtractionGoesWrong(final ExtractorException e) {
                Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
            }

        }).useDefaultLogin().Extract(url);
        System.out.println("rwe90432432"+result);
    }


    public void youtube(String result){
        if(binding.image.getText().toString().length()==0){
            image="https://images.macrumors.com/t/vMbr05RQ60tz7V_zS5UEO9SbGR0=/1600x900/smart/article-new/2018/05/apple-music-note.jpg";
        }
        else{
            image=binding.image.getText().toString();
        }

        music.setMp3(result);
        music.setTitle(binding.title.getText().toString());
        music.setImage(image);
        musicViewModel.insert(music);

        String objJson = gson.toJson(youtube);

        Intent intent=new Intent(this,MusicActivity.class);
        startActivity(intent);
        ToastUtils.set(getApplicationContext(),"유튜브 뮤직 추가 완료",2);
    }
}
