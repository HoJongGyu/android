package com.hjk.music_3.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.hjk.music_3.R;
import com.hjk.music_3.databinding.FragmentProfileBinding;
import com.hjk.music_3.ui.activity.BackGroundActivity;
import com.hjk.music_3.ui.activity.LoginActivity;
import com.hjk.music_3.ui.activity.MusicAddActivity;
import com.hjk.music_3.ui.activity.MusicRemoveActivity;
import com.hjk.music_3.ui.activity.ProfileActivity;
import com.hjk.music_3.ui.activity.YouTubeAddActivity;

import org.jetbrains.annotations.Nullable;

public class ProfileFragment extends Fragment {

    FragmentProfileBinding binding;

    public static ProfileFragment newInstance(){return new ProfileFragment();}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle saveInstanceState){
        setHasOptionsMenu(true);
        binding= DataBindingUtil.inflate(inflater,R.layout.fragment_profile,container,false);
        View root=binding.getRoot();
        binding.setActivity(this);
        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);

    }

    public void Intent_Profile(){
        Intent intent=new Intent(getActivity(), ProfileActivity.class);
        startActivity(intent);
    }

    public void Intent_Back(){
        Intent intent=new Intent(getActivity(), YouTubeAddActivity.class);
        startActivity(intent);
    }

    public void Intent_Music(){
        Intent intent=new Intent(getActivity(), MusicAddActivity.class);
        startActivity(intent);
    }

    public void Intent_Logout(){
        Intent intent=new Intent(getActivity(), LoginActivity.class);
        startActivity(intent);
    }

    public void Intent_MusicRemove(){
        Intent intent=new Intent(getActivity(), MusicRemoveActivity.class);
        startActivity(intent);
    }

}
