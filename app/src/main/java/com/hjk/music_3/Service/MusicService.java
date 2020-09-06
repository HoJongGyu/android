package com.hjk.music_3.Service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationChannelGroup;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.PowerManager;
import android.view.View;
import android.widget.RemoteViews;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleService;
import androidx.lifecycle.MutableLiveData;

import com.andre_max.youtube_url_extractor.ExtractorException;
import com.andre_max.youtube_url_extractor.YoutubeStreamExtractor;
import com.andre_max.youtube_url_extractor.model.YTMedia;
import com.andre_max.youtube_url_extractor.model.YTSubtitles;
import com.andre_max.youtube_url_extractor.model.YoutubeMeta;
import com.hjk.music_3.R;
import com.hjk.music_3.Receiver.MusicReceiver;
import com.hjk.music_3.data.local.model.Music;
import com.hjk.music_3.ui.activity.MusicActivity;
import com.hjk.music_3.ui.viewmodel.MusicViewModel;
import com.hjk.music_3.ui.viewmodel.UserViewModel;
import com.hjk.music_3.utils.SecondUtils;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Random;

public class MusicService extends LifecycleService  {

    private final IBinder binder=new MusicServiceBinder();
    public static MediaPlayer mediaPlayer;
    MutableLiveData<Integer> rem_time=new MutableLiveData<>();

    boolean isPlaying=true;
    public static String result="";

    static Uri url;
    static MusicViewModel musicViewModel;
    static Music current_music;
    public static Fragment fragment;
    IntentFilter intentFilter;

    MusicReceiver myNoisyAudioStreamReceiver;

    private PlayerCallHelper mPlayerCallHelper;
    private Boolean init=false;

    Thread thread;

    private static final String GROUP_ID = "g1";
    private static final String CHANNEL_ID = "c1";
    public static final String NOTIFY_PREVIOUS = "prev";
    public static final String NOTIFY_CLOSE = "close";
    public static final String NOTIFY_PAUSE = "pause";
    public static final String NOTIFY_PLAY = "play";
    public static final String NOTIFY_NEXT = "next";

    Random random=new Random();





    public class MusicServiceBinder extends Binder {
        MusicService getService() {
            return MusicService.this;

        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        super.onBind(intent);
        return binder;
    }

    @Override
    public void onCreate(){
        super.onCreate();


        mediaPlayer=new MediaPlayer();
        mediaPlayer.setWakeMode(getApplicationContext(), PowerManager.PARTIAL_WAKE_LOCK);

        intentFilter = new IntentFilter(AudioManager.ACTION_AUDIO_BECOMING_NOISY);
        myNoisyAudioStreamReceiver = new MusicReceiver();




        if (mPlayerCallHelper == null) {
            mPlayerCallHelper = new PlayerCallHelper(new PlayerCallHelper.PlayerCallHelperListener() {
                @Override
                public void playAudio() {
                    MusicApplication.getInstance().getServiceInterface().start();
                }

                @Override
                public boolean isPlaying() {
                    return MusicApplication.getInstance().getServiceInterface().isPlaying();
                }

                @Override
                public void pauseAudio() {
                    MusicApplication.getInstance().getServiceInterface().pause();
                }
            });
        }
        mPlayerCallHelper.bindCallListener(getApplicationContext());
        mPlayerCallHelper.bindRemoteController(getApplicationContext());


        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {

            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {


            }
        });

        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                setNext();
                try {

                    musicViewModel.set_current_music(musicViewModel.getPos());

                    init();


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        mediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mediaPlayer, int i, int i1) {


                return false;
            }
        });

        mediaPlayer.setOnSeekCompleteListener(new MediaPlayer.OnSeekCompleteListener() {
            @Override
            public void onSeekComplete(MediaPlayer mediaPlayer) {


            }
        });


    }

    public void init(){
        String str=musicViewModel.current_music().getValue().getMp3();
        if (str.contains("youtube")||str.contains("youtu.be")) {
            if (str.contains("youtu.be/"))
                str = str.replaceAll("youtu.be/", "youtube.com/watch?v=");
            youtube_extract(str);
        }else{
            setData(str);
        }

    }

    public void setData(String result) {
        init=true;
        final int pos_=musicViewModel.getPos();
        UserViewModel.save_music(pos_); //노래저장


        url = Uri.parse(result);
        musicViewModel.setIsPlaying(true);
        play();

    }


    @Override
    public void onDestroy(){
        super.onDestroy();
        if(mediaPlayer!=null){
            mPlayerCallHelper.unbindCallListener(getApplicationContext());
            mPlayerCallHelper.unbindRemoteController();

            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer=null;
        }
    }

    public void setViewModel(MusicViewModel musicViewModel){
         this.musicViewModel=musicViewModel;
    }

    public void getFragment(Fragment fragment){
        this.fragment=fragment;
    }

    public void play()  {

        if(musicViewModel.getIsPlaying().getValue()){
            mediaPlayer.stop();
            mediaPlayer.reset();
            musicViewModel.setIsPlaying(false);
        }

        try{

            mediaPlayer.setDataSource(this,url);
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.prepare();

          }catch(Exception e){
            e.printStackTrace();
        }

            if(url!=null) {

                mediaPlayer.start();
                musicViewModel.setIsPlaying(true);
                createNotification();
                mPlayerCallHelper.requestAudioFocus("Music","App");
                registerReceiver(myNoisyAudioStreamReceiver, intentFilter);
                musicViewModel.setSeek(getDuration());
                thread_start();
            }



    }

    public void setNext(){
        if(musicViewModel.getLoop()) {
            musicViewModel.setPos(musicViewModel.getPos());
            return;
        }

        if(musicViewModel.getRandom()){
            int size=musicViewModel.getSize();
            int p=random.nextInt(size);
            musicViewModel.setPos(p);
            return;
        }

        else if(musicViewModel.getSize()-1<musicViewModel.getPos()+1){
            musicViewModel.setPos(0);
            return;
        }else {
            musicViewModel.setPos(musicViewModel.getPos() + 1);
            return;
        }
    }

    public void setPrev(){
        if(musicViewModel.getLoop()) {
            musicViewModel.setPos(musicViewModel.getPos());
            return;
        }

        if(musicViewModel.getRandom()){
            int size=musicViewModel.getSize();
            int p=random.nextInt(size);
            musicViewModel.setPos(p);
            return;
        }

        if(musicViewModel.getPos()-1<0){
            musicViewModel.setPos(musicViewModel.getSize()-1);
        }else {
            musicViewModel.setPos(musicViewModel.getPos() - 1);
        }
    }

    public void thread_start(){

        thread=new MusicService.Thread();
        thread.start();
    }

    public void next()  {
        setNext();
        musicViewModel.set_current_music(musicViewModel.getPos());
        init();

    }

    public void prev(){
        setPrev();
        musicViewModel.set_current_music(musicViewModel.getPos());
        init();
    }


    public void start(){
        musicViewModel.setIsPlaying(true);

        mPlayerCallHelper.requestAudioFocus("Music","App");
        mediaPlayer.start();
        createNotification();
        thread_start();
    }

    public void pause(){

        musicViewModel.setIsPlaying(false);

        mediaPlayer.pause();
        createNotification();
    }

    public boolean isPlaying(){
        return mediaPlayer.isPlaying();
    }

    public int getDuration(){
        return mediaPlayer.getDuration();
    }

    public int current_position(){
        return mediaPlayer.getCurrentPosition();
    }

    public void set_seek(int time){
        mediaPlayer.seekTo(time);

    }

    public Boolean getInit(){
        return this.init;
    }





    private void createNotification() {
        try {


            RemoteViews expandedView;
            expandedView = new RemoteViews(
                    getApplicationContext().getPackageName(), R.layout.notify_player3);

            Intent intent = new Intent(getApplicationContext(), MusicActivity.class);
            intent.setAction("showPlayer");
            PendingIntent contentIntent = PendingIntent.getActivity(
                    this, 0, intent, 0);

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                NotificationManager notificationManager = (NotificationManager)
                        getSystemService(Context.NOTIFICATION_SERVICE);

                NotificationChannelGroup playGroup = new NotificationChannelGroup(GROUP_ID, getString(R.string.play));
                notificationManager.createNotificationChannelGroup(playGroup);

                NotificationChannel playChannel = new NotificationChannel(CHANNEL_ID,
                        getString(R.string.notify_of_play), NotificationManager.IMPORTANCE_DEFAULT);
                playChannel.setGroup(GROUP_ID);
                notificationManager.createNotificationChannel(playChannel);
            }

            Notification notification = new NotificationCompat.Builder(
                    getApplicationContext(), CHANNEL_ID)
                    .setSmallIcon(R.drawable.ic_player)
                    .setContentIntent(contentIntent)
                    .setOnlyAlertOnce(true)
                    .setContentTitle(musicViewModel.current_music().getValue().getTitle()).build();


            notification.bigContentView = expandedView;


            setListeners(expandedView);


            notification.bigContentView.setViewVisibility(R.id.player_next, View.VISIBLE);
            notification.bigContentView.setViewVisibility(R.id.player_previous, View.VISIBLE);

            boolean isPaused = MusicApplication.getInstance().getServiceInterface().isPlaying();



            notification.bigContentView.setViewVisibility(R.id.player_pause, isPaused ? View.VISIBLE : View.GONE);
            notification.bigContentView.setViewVisibility(R.id.player_play, isPaused ? View.GONE : View.VISIBLE);


            notification.bigContentView.setTextViewText(R.id.player_song_name, musicViewModel.current_music().getValue().getTitle());

            notification.flags |= Notification.FLAG_ONGOING_EVENT;


            Picasso.get()
                    .load(musicViewModel.current_music().getValue().getImage())
                    .into(notification.bigContentView,R.id.player_album_art,1, notification);







        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void setListeners(RemoteViews view) {
        try {
            PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(),
                    0, new Intent(NOTIFY_PREVIOUS).setPackage(getPackageName()),
                    PendingIntent.FLAG_UPDATE_CURRENT);
            view.setOnClickPendingIntent(R.id.player_previous, pendingIntent);

            pendingIntent = PendingIntent.getBroadcast(getApplicationContext(),
                    0, new Intent(NOTIFY_CLOSE).setPackage(getPackageName()),
                    PendingIntent.FLAG_UPDATE_CURRENT);
            view.setOnClickPendingIntent(R.id.player_close, pendingIntent);

            pendingIntent = PendingIntent.getBroadcast(getApplicationContext(),
                    0, new Intent(NOTIFY_PAUSE).setPackage(getPackageName()),
                    PendingIntent.FLAG_UPDATE_CURRENT);
            view.setOnClickPendingIntent(R.id.player_pause, pendingIntent);

            pendingIntent = PendingIntent.getBroadcast(getApplicationContext(),
                    0, new Intent(NOTIFY_NEXT).setPackage(getPackageName()),
                    PendingIntent.FLAG_UPDATE_CURRENT);
            view.setOnClickPendingIntent(R.id.player_next, pendingIntent);

            pendingIntent = PendingIntent.getBroadcast(getApplicationContext(),
                    0, new Intent(NOTIFY_PLAY).setPackage(getPackageName()),
                    PendingIntent.FLAG_UPDATE_CURRENT);
            view.setOnClickPendingIntent(R.id.player_play, pendingIntent);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    final Handler handler=new Handler(Looper.getMainLooper()){
        @Override
        public void handleMessage(Message msg){
            switch(msg.what){
                case 0:

                    String music_time=Integer.toString(msg.arg1);
                    String all_music_time=Integer.toString(msg.arg2);

                    if(musicViewModel.getTimer()) {
                        musicViewModel.setRem_Time(musicViewModel.getRem_time().getValue() - 1);
                        System.out.println("남은시간"+musicViewModel.getRem_time().getValue());
                        if(musicViewModel.getRem_time().getValue()==0) {
                            pause();
                        }
                    }
                    if(music_time.length()==3) {
                        music_time = music_time.substring(0, 1) + ":" + music_time.substring(1);
                    }else  if(music_time.length()==4) {
                    music_time = music_time.substring(0, 2) + ":" + music_time.substring(2);
                    }else  if(music_time.length()==5) {
                        music_time = music_time.substring(0, 1) + ":"+ music_time.substring(1, 3) + ":"  + music_time.substring(3);
                    }
                    else{
                        music_time="0:"+music_time.substring(0);
                    }

                    if(all_music_time.length()==3) {
                        all_music_time = all_music_time.substring(0, 1) + ":" + all_music_time.substring(1);
                    }else if(all_music_time.length()==4) {
                    all_music_time = all_music_time.substring(0, 2) + ":" + all_music_time.substring(2);
                    }else if(all_music_time.length()==5) {
                        all_music_time = all_music_time.substring(0, 1) + ":" +all_music_time.substring(1, 3) + ":"  + all_music_time.substring(3);
                    }
                    else{
                        all_music_time="0:"+all_music_time.substring(0);
                    }
                    String progress=music_time+"/"+all_music_time;
                    musicViewModel.setStart_progress(music_time);
                    musicViewModel.setEnd_progress(all_music_time);
                    musicViewModel.setProgress(progress);
                    break;
            }
        }
    };

    class Thread extends java.lang.Thread{
        @Override
        public void run(){
            super.run();


            while(musicViewModel.getIsPlaying().getValue()){

                int duration=getDuration();
                int All_Time = Integer.parseInt(SecondUtils.formateMilliSeccond(duration));

                int current_pos=current_position();
                int current_progress=Integer.parseInt(SecondUtils.formateMilliSeccond(current_pos));


                Message message=handler.obtainMessage();



                String msg=new String(":");

                message.obj=msg;


                message.arg1=current_progress;
                message.arg2=All_Time;

                message.what=0;






                try{
                    sleep(1000);
                }catch(Exception e){
                    e.printStackTrace();
                }
                handler.sendMessage(message);
            }

        }
    }

    public void youtube_extract(String url){

        new YoutubeStreamExtractor(new YoutubeStreamExtractor.ExtractorListner(){
            @Override
            public void onExtractionDone(List<YTMedia> adativeStream, final List<YTMedia> muxedStream, List<YTSubtitles> subtitles, YoutubeMeta meta) {
                //url to get subtitle
                for (YTMedia media:adativeStream) {
                    result=media.getUrl();


                }

                setData(result);

            }
            @Override
            public void onExtractionGoesWrong(final ExtractorException e) {
                Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
            }

        }).useDefaultLogin().Extract(url);
    }



    // 1. 멈춤하고 다음 재생을 하면 기존곡으로 설정됨
    // 2. 시간표시에 관한 문제
    // 3.
}
