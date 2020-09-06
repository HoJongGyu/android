package com.hjk.music_3.data.local.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;


import com.hjk.music_3.data.local.model.YouTube;

import java.util.List;

@Dao
public interface YouTubeDao {
    @Insert(onConflict= OnConflictStrategy.IGNORE)
    void insert_youtube(YouTube youtube);

    @Query("SELECT * FROM YouTube")
    LiveData<List<YouTube>> getAllYouTube();

}
