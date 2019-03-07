package com.example.rapidfood.Activites;



import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;


import com.example.rapidfood.R;
import com.google.firebase.auth.FirebaseAuth;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class ProfileActivity extends AppCompatActivity {

    FirebaseAuth mAuth;
    Toolbar mToolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        requestWindowFeature(1);
//        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.LOLLIPOP){
//            getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
//            getWindow().setStatusBarColor(Color.TRANSPARENT);
//        }
        setContentView(R.layout.activity_profile);
        mToolbar=findViewById(R.id.toolbar_profile);
        setSupportActionBar(mToolbar);
        ActionBar vActionBar=getSupportActionBar();
        if(vActionBar!=null){
            vActionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_white_black_24dp);
            vActionBar.setDisplayHomeAsUpEnabled(true);
            vActionBar.setDisplayShowHomeEnabled(true);
        }


    }
}
