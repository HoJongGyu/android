package com.hjk.music_3.ui.viewmodel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.hjk.music_3.data.local.model.User;
import com.hjk.music_3.data.remote.UserRepository;

import java.util.List;

public class UserViewModel extends AndroidViewModel {

    private static MutableLiveData<User> user=null;
    private static UserRepository userRepository_local;
    private UserRepository userRepository=UserRepository.getInstance();

    private LiveData<User> local_user;
    private static int save_music=0;
    private static int save_back=0;
    private static int save_login=0;

    public void init(){
        if(user!=null)
            return;
        userRepository=UserRepository.getInstance();
    }

    public UserViewModel(Application application){
        super(application);
        userRepository_local=new UserRepository(application);

        local_user=userRepository_local.get_local_user();



        save_music=userRepository_local.load_save_music();

        save_back=userRepository_local.load_save_back();

        save_login=userRepository_local.load_save_login();

    }

    public  LiveData<User> get_local_user(){
        return local_user;
    }

    public static void insert_user(User user){userRepository_local.insert_user(user);}

    public static void save_back(int save_back){userRepository_local.save_back(save_back);}

    public static int load_save_back(){return save_back;}

    public static void save_music(int save_music){userRepository_local.save_music(save_music);}

    public static int load_save_music(){return save_music;}

    public  void save_login(int save_login){
        this.save_login=save_login;
        userRepository_local.save_login(save_login);
    }

    public static int load_save_login(){return save_login;}


    public MutableLiveData<User> getLogin(String id){
        user=userRepository.getLogin(id);
        return user;
    }

    public static MutableLiveData<User> getUser(){
        return user;

    }


}
