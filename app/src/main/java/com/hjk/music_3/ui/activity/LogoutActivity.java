package com.hjk.music_3.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;

import com.hjk.music_3.R;
import com.hjk.music_3.databinding.ActivityLogoutBinding;

public class LogoutActivity extends Activity {

    ActivityLogoutBinding binding;
    Intent intent=getIntent();
    @Override
    public void onCreate(Bundle saveInstanceState){
        super.onCreate(saveInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_logout);
    }

    public void logout(){
        intent=new Intent(LogoutActivity.this,LoginActivity.class);
        startActivity(intent);
        ActivityCompat.finishAffinity(this);
    }


}
