package com.hjk.music_3.ui.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.hjk.music_3.data.local.model.BackGround;
import com.hjk.music_3.data.remote.BackRepository;

import java.util.List;

public class BackViewModel extends ViewModel {

    public static MutableLiveData<List<BackGround>> back;
    public static BackGround current_back;
    public static int pos=2;
    public static boolean change=true;
    private static BackRepository backRepository= BackRepository.getInstance();

    public void init(){
        if(back!=null){
            return;
        }
        back=backRepository.getBack();

    }


    public void setPos(int pos){
        this.pos=pos;
    }

    public int getPos(){
        return this.pos;
    }

    public static LiveData<List<BackGround>> getBack(){
        return back;
    }

    public static BackGround current_back(){
         return current_back;
   }

   public static void set_current_back(BackGround backgroud){
        current_back=backgroud;
   }




}



//여기서는 getValue로 값을 받을 수 없는 듯 하다
