package com.hjk.music_3.Service;

import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaMetadataRetriever;
import android.media.RemoteControlClient;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;

public class PlayerCallHelper implements AudioManager.OnAudioFocusChangeListener {

    private final PlayerCallHelperListener mPlayerCallHelperListener;
    private PhoneStateListener phoneStateListener;
    private RemoteControlClient remoteControlClient;
    private AudioManager mAudioManager;
    private boolean ignoreAudioFocus;
    private boolean mIsTempPauseByPhone;
    private boolean tempPause;

    public PlayerCallHelper(PlayerCallHelperListener playerCallHelperListener) {
        mPlayerCallHelperListener = playerCallHelperListener;

    }

    public void bindCallListener(Context context) {
        phoneStateListener = new PhoneStateListener() {
            @Override
            public void onCallStateChanged(int state, String incomingNumber) {
                if (state == TelephonyManager.CALL_STATE_IDLE) {
                    if (mIsTempPauseByPhone) {
                        if (mPlayerCallHelperListener != null) {
                            mPlayerCallHelperListener.playAudio();
                        }
                        mIsTempPauseByPhone = false;
                    }
                } else if (state == TelephonyManager.CALL_STATE_RINGING) {
                    if (mPlayerCallHelperListener != null) {
                        if (mPlayerCallHelperListener.isPlaying()) {
                            mPlayerCallHelperListener.pauseAudio();
                            mIsTempPauseByPhone = true;
                        }
                    }

                } else if (state == TelephonyManager.CALL_STATE_OFFHOOK) {

                }
                super.onCallStateChanged(state, incomingNumber);
            }
        };
        TelephonyManager manager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        if (manager != null) {
            manager.listen(phoneStateListener, PhoneStateListener.LISTEN_CALL_STATE);
        }
    }

    public void bindRemoteController(Context context) {
        mAudioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);

        ComponentName remoteComponentName = new ComponentName(context, PlayerCallHelper.class.getName());
        try {
            if (remoteControlClient == null) {
                mAudioManager.registerMediaButtonEventReceiver(remoteComponentName);
                Intent mediaButtonIntent = new Intent(Intent.ACTION_MEDIA_BUTTON);
                mediaButtonIntent.setComponent(remoteComponentName);
                PendingIntent mediaPendingIntent = PendingIntent.getBroadcast(
                        context, 0, mediaButtonIntent, 0);
                remoteControlClient = new RemoteControlClient(mediaPendingIntent);
                mAudioManager.registerRemoteControlClient(remoteControlClient);
            }
            remoteControlClient.setTransportControlFlags(RemoteControlClient.FLAG_KEY_MEDIA_PLAY
                    | RemoteControlClient.FLAG_KEY_MEDIA_PAUSE
                    | RemoteControlClient.FLAG_KEY_MEDIA_PLAY_PAUSE
                    | RemoteControlClient.FLAG_KEY_MEDIA_STOP
                    | RemoteControlClient.FLAG_KEY_MEDIA_PREVIOUS
                    | RemoteControlClient.FLAG_KEY_MEDIA_NEXT);
        } catch (Exception e) {
            Log.e("tmessages", e.toString());
        }
    }

    public void unbindCallListener(Context context) {
        try {
            TelephonyManager mgr = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            if (mgr != null) {
                mgr.listen(phoneStateListener, PhoneStateListener.LISTEN_NONE);
            }
        } catch (Exception e) {
            Log.e("tmessages", e.toString());
        }
    }

    public void unbindRemoteController() {
        if (remoteControlClient != null) {
            RemoteControlClient.MetadataEditor metadataEditor = remoteControlClient.editMetadata(true);
            metadataEditor.clear();
            metadataEditor.apply();
            mAudioManager.unregisterRemoteControlClient(remoteControlClient);
            mAudioManager.abandonAudioFocus(this);
        }
    }

    public void requestAudioFocus(String title, String summary) {
        if (remoteControlClient != null) {
            RemoteControlClient.MetadataEditor metadataEditor = remoteControlClient.editMetadata(true);
            metadataEditor.putString(MediaMetadataRetriever.METADATA_KEY_ARTIST, summary);
            metadataEditor.putString(MediaMetadataRetriever.METADATA_KEY_TITLE, title);
            metadataEditor.apply();
            mAudioManager.requestAudioFocus(this, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);
        }
    }

    public void setIgnoreAudioFocus() {
        ignoreAudioFocus = true;
    }

    @Override
    public void onAudioFocusChange(int focusChange) {
        if (ignoreAudioFocus) {
            ignoreAudioFocus = false;
            return;
        }
        if (focusChange == AudioManager.AUDIOFOCUS_LOSS) {
            if (mPlayerCallHelperListener != null) {
                if (mPlayerCallHelperListener.isPlaying() ) {
                    mPlayerCallHelperListener.pauseAudio();
                    tempPause = true;
                }
            }
        } else if (focusChange == AudioManager.AUDIOFOCUS_GAIN) {
            if (tempPause) {
                if (mPlayerCallHelperListener != null) {
                    mPlayerCallHelperListener.playAudio();
                }
                tempPause = false;
            }
        }
    }

    public interface PlayerCallHelperListener {
        void playAudio();

        boolean isPlaying();


        void pauseAudio();
    }
}