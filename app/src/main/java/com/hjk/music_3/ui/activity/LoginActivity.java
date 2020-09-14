package com.hjk.music_3.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.widget.CompoundButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

import com.hjk.music_3.R;


import com.hjk.music_3.databinding.ActivityLoginBinding;
import com.hjk.music_3.ui.viewmodel.MusicViewModel;
import com.hjk.music_3.ui.viewmodel.UserViewModel;
import com.hjk.music_3.ui.viewmodel.YouTubeViewModel;
import com.hjk.music_3.utils.ToastUtils;


public class LoginActivity extends AppCompatActivity {

    UserViewModel userViewModel;

    ActivityLoginBinding binding;
    Intent intent = getIntent();

    int save;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        userViewModel = ViewModelProviders.of(this).get(UserViewModel.class);
        userViewModel.init();

        if(userViewModel.load_save_login()==1)
            binding.switch1.setChecked(true);
        binding.switch1.setOnCheckedChangeListener(new saveSwitchListener());
        binding.setActivity(this);


    }


    public void Intent_Sign() {


            intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);

    }

    public void login() {
        String Email = binding.Email.getText().toString();
        String Pwd = binding.Password.getText().toString();

        userViewModel.getLogin(Email).observe(this, u -> {
            if (u != null) {
                if (u.getId().equals(Email) && u.getPwd().equals(Pwd)) {
                    userViewModel.save_login(save);
                    Toast.makeText(this, "로그인 완료", Toast.LENGTH_SHORT).show();
                    intent = new Intent(LoginActivity.this, MusicActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(this, "비밀번호가 틀립니다.", Toast.LENGTH_SHORT).show();
                }
            }
            else{
                Toast.makeText(this, "아이디가 틀립니다.", Toast.LENGTH_SHORT).show();

            }
        });
    }


    class saveSwitchListener implements CompoundButton.OnCheckedChangeListener{
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (isChecked){
                save = 1;
                ToastUtils.set(getApplicationContext(),"자동 로그인 설정",2);
            }
            else
                save=0;
                ToastUtils.set(getApplicationContext(),"자동 로그인 해제",2);
            }

    }
}
