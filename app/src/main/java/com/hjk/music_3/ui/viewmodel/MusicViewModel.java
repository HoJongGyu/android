package com.hjk.music_3.ui.viewmodel;

import android.app.Application;

import androidx.databinding.ObservableField;
import androidx.databinding.ObservableInt;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.hjk.music_3.data.local.model.Music;
import com.hjk.music_3.data.remote.MusicRepository;

import java.util.List;

public class MusicViewModel extends AndroidViewModel {

    private LiveData<List<Music>> AllMusic; //로컬
    private static MutableLiveData<List<Music>> music; //레트로핏
    private static MutableLiveData<Music> current_music=new MutableLiveData<Music>();
    private static MusicRepository musicRepository=MusicRepository.getInstance();
    private static MusicRepository musicRepository_local;
    public static MutableLiveData<Boolean> isPlaying=new MutableLiveData<Boolean>();
    public static int time=0;
    public static MutableLiveData<Integer> rem_time=new MutableLiveData<Integer>();
    public static MutableLiveData<Integer> Seek=new MutableLiveData<Integer>();
    public static int pos=1;
    public static int pos_=1;
    public static boolean timer=false;
    public static boolean change=true;
    public static boolean loop=false;
    public static boolean random=false;

    public static MutableLiveData<String> Progress=new MutableLiveData<String>();

    public static MutableLiveData<String> start_progress=new MutableLiveData<String>();

    public static MutableLiveData<String> end_progress=new MutableLiveData<String>();





    public MusicViewModel(Application application){
        super(application);
        musicRepository_local=new MusicRepository(application);
        AllMusic=musicRepository_local.getAllMusic();

    }


//    public void init(){
////        if(change==true){
////            pos_=UserViewModel.load_save_music();
////            change=false;
////            isPlaying.setValue(false);
////        }
//
//        if(music!=null)
//            return;
////        music=musicRepository.getMusic();
//
//    }
//레트로핏 미사용으로 주석처리

    //Room
    public LiveData<List<Music>> getAllMusic() {
        return AllMusic;
    }

    public static void insert(Music music){musicRepository_local.insert(music);}

    public static void deleteMusic(String title){
        musicRepository_local.deleteMusic(title);
    }

    public int getSize(){ return AllMusic.getValue().size();}


    //Retrofit
    public MutableLiveData<List<Music>> getMusic(){
        return music;
    }


    //method
    public static MutableLiveData<Music> current_music(){
        return current_music;
    }

    public void set_current_music(int pos){
        if(pos==9999){
            Music music=new Music();
            music.setTitle("음악을 추가해주세요");
            music.setImage("https://i.pinimg.com/originals/f7/3a/5b/f73a5b4b7262440684a2b5c39e684304.jpg");
            current_music.setValue(music);
            return;
        }
        current_music.setValue(getAllMusic().getValue().get(pos));
    }

    public int getPos(){
        return this.pos;
    }

    public void setPos(int pos){
        this.pos= pos;
        UserViewModel.save_music(pos); //사용자 가장 마지막 재생한 음악
    }

    public void setIsPlaying(boolean isPlaying){this.isPlaying.setValue(isPlaying);}

    public static MutableLiveData<Boolean> getIsPlaying(){return isPlaying;}

    public void setProgress(String progress){
        this.Progress.setValue(progress);
    }

    public MutableLiveData<String> getProgress(){
        return this.Progress;
    }

    public void setStart_progress(String start_progress){
        this.start_progress.setValue(start_progress);
    }

    public MutableLiveData<String> getStart_progress(){
        return this.start_progress;
    }

    public void setEnd_progress(String end_progress){
        this.end_progress.setValue(end_progress);
    }

    public MutableLiveData<String> getEnd_progress(){
        return this.end_progress;
    }

    public void setChange(boolean change){this.change=change;}

    public boolean getChange(){return this.change;}

    public void setTime(int time){
        this.time=time;
    }

    public int getTime(){
        return time;
    }

    public void setRem_Time(int rem_time){
        this.rem_time.setValue(rem_time);
    }

    public MutableLiveData<Integer> getRem_time(){
        return this.rem_time;
    }

    public void setSeek(int seek){
        this.Seek.setValue(seek);
    }

    public MutableLiveData<Integer> getSeek(){
        return this.Seek;
    }

    public void setLoop(Boolean loop){
        this.loop=loop;
    }

    public Boolean getLoop(){
        return loop;
    }

    public void setRandom(Boolean random){
        this.random=random;
    }

    public Boolean getRandom(){
        return random;
    }

    public void setTimer(Boolean timer){
        this.timer=timer;
    }

    public Boolean getTimer(){
        return timer;
    }


}
