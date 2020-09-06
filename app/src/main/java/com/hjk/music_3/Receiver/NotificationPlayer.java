package com.hjk.music_3.Receiver;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.widget.RemoteViews;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.hjk.music_3.R;
import com.hjk.music_3.Service.MusicService;
import com.hjk.music_3.ui.activity.MusicActivity;
import com.hjk.music_3.ui.viewmodel.MusicViewModel;
import com.squareup.picasso.Picasso;

import java.io.IOException;


public class NotificationPlayer  {
    private final static int NOTIFICATION_PLAYER_ID = 0x342;
    private MusicService mService;
    private NotificationManager mNotificationManager;
    private NotificationManagerBuilder mNotificationManagerBuilder;
    private boolean isForeground;

    public NotificationPlayer(MusicService service) {
        mService = service;
        mNotificationManager = (NotificationManager) service.getSystemService(Context.NOTIFICATION_SERVICE);
    }

    public void updateNotificationPlayer() {
        cancel();
        mNotificationManagerBuilder = new NotificationManagerBuilder();
        mNotificationManagerBuilder.execute();
    }

    public void removeNotificationPlayer() {
        cancel();
        mService.stopForeground(true);
        isForeground = false;
    }

    private void cancel() {
        if (mNotificationManagerBuilder != null) {
            mNotificationManagerBuilder.cancel(true);
            mNotificationManagerBuilder = null;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private class NotificationManagerBuilder extends AsyncTask<Void, Void, Notification> {
        private RemoteViews mRemoteViews;
        private NotificationCompat.Builder mNotificationBuilder;
        private PendingIntent mMainPendingIntent;

        String channelId="Channel";
        String channelName="Channel Name";
        int importance=NotificationManager.IMPORTANCE_LOW;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Intent mainActivity = new Intent(mService, MusicActivity.class);
            mMainPendingIntent = PendingIntent.getActivity(mService, 0, mainActivity, 0);
            mRemoteViews = createRemoteView(R.layout.notify_player);
//            mNotificationBuilder = new NotificationCompat.Builder(mService);

            NotificationChannel notificationChannel= new NotificationChannel(channelId,channelName,importance);

            mNotificationManager.createNotificationChannel(notificationChannel);

            mNotificationBuilder = new NotificationCompat.Builder(mService, channelId);

            mNotificationBuilder.setSmallIcon(R.drawable.batu_gezer_mnhltvr6g9q_unsplash)
                    .setOngoing(true)
                    .setContentIntent(mMainPendingIntent)
                    .setContent(mRemoteViews)
                    .setPriority(NotificationManager.IMPORTANCE_MIN)
                    .setVisibility(NotificationCompat.VISIBILITY_PUBLIC);



            Notification notification = mNotificationBuilder.build();
            notification.priority = Notification.PRIORITY_MAX;
            notification.contentIntent = mMainPendingIntent;
            NotificationManagerCompat.from(mService).notify(NOTIFICATION_PLAYER_ID, notification);
            if (!isForeground) {
                isForeground = true;
                // 서비스를 Foreground 상태로 만든다
                mService.startForeground(NOTIFICATION_PLAYER_ID, notification);
            }
        }

        @Override
        protected Notification doInBackground(Void... params) {
            mNotificationBuilder.setContent(mRemoteViews);
            mNotificationBuilder.setContentIntent(mMainPendingIntent);
            mNotificationBuilder.setPriority(Notification.PRIORITY_MAX);
            Notification notification = mNotificationBuilder.build();
            try {
                updateRemoteView(mRemoteViews, notification);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return notification;

        }

        @Override
        protected void onPostExecute(Notification notification) {
            super.onPostExecute(notification);
            try {
                mNotificationManager.notify(NOTIFICATION_PLAYER_ID, notification);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        private RemoteViews createRemoteView(int layoutId) {
            RemoteViews remoteView = new RemoteViews(mService.getPackageName(), layoutId);
            Intent actionTogglePlay = new Intent(CommandActions.TOGGLE_PLAY);
            Intent actionForward = new Intent(CommandActions.FORWARD);
            Intent actionRewind = new Intent(CommandActions.REWIND);
            Intent actionClose = new Intent(CommandActions.CLOSE);
            PendingIntent togglePlay = PendingIntent.getService(mService, 0, actionTogglePlay, 0);
            PendingIntent forward = PendingIntent.getService(mService, 0, actionForward, 0);
            PendingIntent rewind = PendingIntent.getService(mService, 0, actionRewind, 0);
            PendingIntent close = PendingIntent.getService(mService, 0, actionClose, 0);

            remoteView.setOnClickPendingIntent(R.id.player_pause, togglePlay);
            remoteView.setOnClickPendingIntent(R.id.player_next, forward);
            remoteView.setOnClickPendingIntent(R.id.player_previous, rewind);

            return remoteView;
        }

        private void updateRemoteView(RemoteViews remoteViews, Notification notification) throws IOException {
            if (mService.isPlaying()) {
                remoteViews.setImageViewResource(R.id.player_pause, R.drawable.ic_action_pause);
            } else {
                remoteViews.setImageViewResource(R.id.player_pause, R.drawable.ic_play_arrow_48dp);
            }


            String title =     MusicViewModel.current_music().getValue().getTitle();
            remoteViews.setTextViewText(R.id.player_song_name, title);
            Uri url = Uri.parse(MusicViewModel.current_music().getValue().getImage());
            Picasso.get()
                    .load(url)
                    .into(remoteViews,R.id.image,NOTIFICATION_PLAYER_ID, notification);

        }
    }

    public class CommandActions {
        public final static String REWIND = "REWIND";
        public final static String TOGGLE_PLAY = "TOGGLE_PLAY";
        public final static String FORWARD = "FORWARD";
        public final static String CLOSE = "CLOSE";
    }

}
