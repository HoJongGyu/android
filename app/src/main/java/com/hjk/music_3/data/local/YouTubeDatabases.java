package com.hjk.music_3.data.local;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.hjk.music_3.data.local.dao.MusicDao;
import com.hjk.music_3.data.local.dao.YouTubeDao;
import com.hjk.music_3.data.local.model.YouTube;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(
        entities = {YouTube.class},
        version = 1,
        exportSchema = false)
public abstract class YouTubeDatabases extends RoomDatabase {

    public static final String DATABASE_NAME = "YouTube.db";

    private static YouTubeDatabases INSTANCE;

    private static final Object sLock = new Object();

    private static final int NUMBER_OF_THREADS=4;

    public static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public abstract YouTubeDao youtubeDao();


    public static YouTubeDatabases getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (YouTubeDatabases.class) {

                if(INSTANCE==null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            YouTubeDatabases.class,"youtube_databases")
                            .addCallback(sRoomDatabaseCallback)
                            .allowMainThreadQueries()
                            .build();
                }
            }
        }
        return INSTANCE;
    }



    private static RoomDatabase.Callback sRoomDatabaseCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);


            databaseWriteExecutor.execute(() -> {

            });
        }
    };
}
