package com.firefoody.Activites;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.firefoody.R;

public class StatusActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status);
        Handler handler = new Handler();

        handler.postDelayed(new Runnable() {
            public void run() {
                startActivity(new Intent(StatusActivity.this,MainActivity.class));
                finish();
            }
        }, 3000);
    }
}
