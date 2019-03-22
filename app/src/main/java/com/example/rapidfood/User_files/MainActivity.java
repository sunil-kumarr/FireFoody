package com.example.rapidfood.User_files;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.rapidfood.Activites.Authentication;
import com.example.rapidfood.Fragments.HomeFragment;
import com.example.rapidfood.R;
import com.example.rapidfood.Utils.FirebaseInstances;
import com.example.rapidfood.Vendor_files.DashboardActivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class MainActivity extends AppCompatActivity {

    private FragmentManager mFragmentManager;
    private FragmentTransaction mFragmentTransaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        mFragmentManager = getSupportFragmentManager();
        mFragmentTransaction = mFragmentManager.beginTransaction();
        Fragment vFragment = new HomeFragment();
        mFragmentTransaction.replace(R.id.frame_user_holder, vFragment, "frag_user_home");
        mFragmentTransaction.commit();
        findViewById(R.id.qrcode_scanner).setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                FirebaseInstances vFirebaseInstances=new FirebaseInstances();
                vFirebaseInstances.getFirebaseAuth().signOut();
                startActivity(new Intent(MainActivity.this, Authentication.class));
                finish();
            }
        });
    }
}