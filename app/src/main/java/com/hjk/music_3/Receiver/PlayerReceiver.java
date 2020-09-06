package com.hjk.music_3.Receiver;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.view.KeyEvent;


import com.hjk.music_3.Service.MusicApplication;
import com.hjk.music_3.Service.MusicService;

import java.util.Objects;

public class PlayerReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        if (Objects.equals(intent.getAction(), Intent.ACTION_MEDIA_BUTTON)) {
            if (intent.getExtras() == null) {
                return;
            }
            KeyEvent keyEvent = (KeyEvent) intent.getExtras().get(Intent.EXTRA_KEY_EVENT);
            if (keyEvent == null) {
                return;
            }
            if (keyEvent.getAction() != KeyEvent.ACTION_DOWN) {
                return;
            }

            switch (keyEvent.getKeyCode()) {
                case KeyEvent.KEYCODE_HEADSETHOOK:
                case KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE:
                    MusicApplication.getInstance().getServiceInterface().start();
                    break;
                case KeyEvent.KEYCODE_MEDIA_PLAY:

                    MusicApplication.getInstance().getServiceInterface().start();
                    break;
                case KeyEvent.KEYCODE_MEDIA_PAUSE:

                    MusicApplication.getInstance().getServiceInterface().pause();
                    break;
                case KeyEvent.KEYCODE_MEDIA_STOP:

                    break;
                case KeyEvent.KEYCODE_MEDIA_NEXT:

                    MusicApplication.getInstance().getServiceInterface().next();
                    break;
                case KeyEvent.KEYCODE_MEDIA_PREVIOUS:

                    MusicApplication.getInstance().getServiceInterface().prev();
                    break;
                default:
            }

        } else {

            if (Objects.requireNonNull(intent.getAction()).equals(MusicService.NOTIFY_PLAY)) {
                if(MusicApplication.getInstance().getServiceInterface().isPlaying()) {
                    MusicApplication.getInstance().getServiceInterface().pause();
                }else {
                    MusicApplication.getInstance().getServiceInterface().start();
                }
            } else if (intent.getAction().equals(MusicService.NOTIFY_PAUSE)
                    || intent.getAction().equals(android.media.AudioManager.ACTION_AUDIO_BECOMING_NOISY)) {
                if(MusicApplication.getInstance().getServiceInterface().isPlaying()) {
                    MusicApplication.getInstance().getServiceInterface().pause();
                }else {
                    MusicApplication.getInstance().getServiceInterface().start();
                }
            } else if (intent.getAction().equals(MusicService.NOTIFY_NEXT)) {
                MusicApplication.getInstance().getServiceInterface().next();
            } else if (intent.getAction().equals(MusicService.NOTIFY_CLOSE)) {

            } else if (intent.getAction().equals(MusicService.NOTIFY_PREVIOUS)) {
                MusicApplication.getInstance().getServiceInterface().prev();

            }
        }
    }
}
