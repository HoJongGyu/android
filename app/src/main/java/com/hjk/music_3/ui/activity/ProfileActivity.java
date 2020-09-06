package com.hjk.music_3.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Window;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

import com.hjk.music_3.R;
import com.hjk.music_3.databinding.ActivityMyinfoBinding;
import com.hjk.music_3.ui.viewmodel.UserViewModel;

public class ProfileActivity extends AppCompatActivity {
    UserViewModel userViewModel;
    ActivityMyinfoBinding binding;
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        binding= DataBindingUtil.setContentView(this,R.layout.activity_myinfo);
        userViewModel= ViewModelProviders.of(this).get(UserViewModel.class);
        userViewModel.get_local_user().observe(this,m->{
            binding.name.setText(m.getName());
            binding.id.setText(m.getId());

        });

    }
}
