package com.example.rapidfood.User_files;


import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;
import com.aurelhubert.ahbottomnavigation.notification.AHNotification;
import com.example.rapidfood.Activites.Authentication;
import com.example.rapidfood.Fragments.HomeFragment;
import com.example.rapidfood.R;
import com.example.rapidfood.Utils.FirebaseInstances;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class MainActivity extends AppCompatActivity {

    private FragmentManager mFragmentManager;
    private FragmentTransaction mFragmentTransaction;
    private AHBottomNavigation bottomNavigation;
    private FirebaseInstances mFirebaseInstances;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        mFirebaseInstances=new FirebaseInstances();
        mFragmentManager = getSupportFragmentManager();
        mFragmentTransaction = mFragmentManager.beginTransaction();
        Fragment vFragment = new HomeFragment();
        mFragmentTransaction.replace(R.id.frame_user_holder, vFragment, "frag_user_home");
        mFragmentTransaction.commit();
        bottomNavigation = findViewById(R.id.bottom_navigation);
        initBottomBar(bottomNavigation);
    }

    private void initBottomBar(AHBottomNavigation pBottomNavigation) {
        // Create items
        AHBottomNavigationItem item1 = new AHBottomNavigationItem("Home", R.drawable.ic_home_black_24dp, R.color.red_500);
        AHBottomNavigationItem item2 = new AHBottomNavigationItem("QR Scanner", R.drawable.ic_qrcode, R.color.green_500);
        AHBottomNavigationItem item3 = new AHBottomNavigationItem("History", R.drawable.ic_access_time_black_24dp, R.color.blue_500);
        AHBottomNavigationItem item4 = new AHBottomNavigationItem("Profile", R.drawable.ic_person_black_24dp, R.color.blue_500);

        // Add items
        pBottomNavigation.addItem(item1);
        pBottomNavigation.addItem(item2);
        pBottomNavigation.addItem(item3);
        pBottomNavigation.addItem(item4);

        // Set background color
        pBottomNavigation.setDefaultBackgroundColor(Color.parseColor("#ffffff"));

        // Disable the translation inside the CoordinatorLayout
        pBottomNavigation.setBehaviorTranslationEnabled(true);

        // Change colors
        pBottomNavigation.setAccentColor(Color.parseColor("#F63D2B"));
        pBottomNavigation.setInactiveColor(Color.parseColor("#747474"));

        // Force to tint the drawable (useful for font with icon for example)
        pBottomNavigation.setForceTint(true);



        // Manage titles
        pBottomNavigation.setTitleState(AHBottomNavigation.TitleState.ALWAYS_SHOW);


        // Use colored navigation with circle reveal effect
       // pBottomNavigation.setColored(true);

        // Set current item programmatically
        pBottomNavigation.setCurrentItem(1);

        // Customize notification (title, background, typeface)
        pBottomNavigation.setNotificationBackgroundColor(Color.parseColor("#F63D2B"));

        // Add or remove notification for each item
        pBottomNavigation.setNotification("1", 2);
        // OR
        AHNotification notification = new AHNotification.Builder()
                .setText("1")
                .setBackgroundColor(ContextCompat.getColor(MainActivity.this, R.color.black))
                .setTextColor(ContextCompat.getColor(MainActivity.this, R.color.white))
                .build();
        pBottomNavigation.setNotification(notification, 1);


        // Set listeners
        pBottomNavigation.setOnTabSelectedListener(new AHBottomNavigation.OnTabSelectedListener() {
            @Override
            public boolean onTabSelected(int position, boolean wasSelected) {
                // Do something cool here...
                switch (position)
                {
                    case 3:if(mFirebaseInstances.getFirebaseAuth().getCurrentUser()!=null){
                        mFirebaseInstances.getFirebaseAuth().signOut();
                        startActivity(new Intent(MainActivity.this, Authentication.class));
                        finish();
                    }
                }
                return true;
            }
        });
        pBottomNavigation.setOnNavigationPositionListener(new AHBottomNavigation.OnNavigationPositionListener() {
            @Override
            public void onPositionChange(int y) {
                // Manage the new y position
            }
        });
    }
}