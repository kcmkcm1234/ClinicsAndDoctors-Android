package com.clinicsanddoctors.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;

import com.clinicsanddoctors.R;
import com.clinicsanddoctors.data.entity.UserClient;
import com.clinicsanddoctors.data.local.AppPreference;
import com.clinicsanddoctors.ui.main.MainActivity;
import com.clinicsanddoctors.ui.start.StartActivity;


public class SplashActivity extends BaseClinicActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

//                startActivity(new Intent(SplashActivity.this, MainActivity.class));

                UserClient userClient = AppPreference.getUser(SplashActivity.this);
                if (userClient != null) {
                    startActivity(new Intent(SplashActivity.this, MainActivity.class));
                } else
                    startActivity(new Intent(SplashActivity.this, StartActivity.class));

                finish();
            }
        }, 3000);
    }
}
