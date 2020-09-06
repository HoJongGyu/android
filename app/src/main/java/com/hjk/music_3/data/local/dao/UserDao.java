package com.hjk.music_3.data.local.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;

import com.hjk.music_3.data.local.model.User;

@Dao
public interface UserDao {
    @Insert(onConflict= OnConflictStrategy.IGNORE)
    void insert_user(User user);

    @Query("update user set save_music=:save_music")
    void save_music(int save_music);

    @Query("update user set save_back=:save_back")
    void save_back(int save_back);

    @Query("update user set save_login=:save_login")
    void save_login(int save_login);

    @Query("select save_back from user")
    int load_save_back();

    @Query("select save_music from user")
    int load_save_music();

    @Query("select save_login from user")
    int load_save_login();

    @Query("SELECT * FROM user")
    LiveData<User> getUser();


}
