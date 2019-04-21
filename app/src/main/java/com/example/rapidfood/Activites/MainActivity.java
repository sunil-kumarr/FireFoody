package com.example.rapidfood.Activites;


import android.graphics.Color;
import android.os.Bundle;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;
import com.example.rapidfood.Fragments.HomeFragment;
import com.example.rapidfood.Fragments.NotificationFragment;
import com.example.rapidfood.Fragments.ProfileFragment;
import com.example.rapidfood.Fragments.QRFragment;
import com.example.rapidfood.Fragments.TimingFragment;
import com.example.rapidfood.R;
import com.example.rapidfood.Utils.FirebaseInstances;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class MainActivity extends AppCompatActivity {

    private FragmentManager mFragmentManager;
    private FragmentTransaction mFragmentTransaction;
    private AHBottomNavigation bottomNavigation;
    private FirebaseInstances mFirebaseInstances;
    private FirebaseFirestore mFirebaseFirestore;
    private FirebaseAuth mFirebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        mFirebaseInstances = new FirebaseInstances();
        mFirebaseFirestore = mFirebaseInstances.getFirebaseFirestore();
        mFirebaseAuth = mFirebaseInstances.getFirebaseAuth();

        mFragmentManager = getSupportFragmentManager();
        mFragmentTransaction = mFragmentManager.beginTransaction();
        Fragment vFragment = new HomeFragment();
        mFragmentTransaction.add(R.id.frame_user_holder, vFragment, "frag_user_home");
        mFragmentTransaction.commit();
        bottomNavigation = findViewById(R.id.bottom_navigation);
        initBottomBar(bottomNavigation);
    }

    private void initBottomBar(AHBottomNavigation pBottomNavigation) {
        // Create items
        AHBottomNavigationItem item1 = new AHBottomNavigationItem("Home", R.drawable.ic_home_black_24dp, R.color.red_500);
        AHBottomNavigationItem item3 = new AHBottomNavigationItem("QR Scanner", R.drawable.ic_qrcode, R.color.green_500);
        AHBottomNavigationItem item2 = new AHBottomNavigationItem("Delivery", R.drawable.ic_today_white_24dp, R.color.blue_500);
        AHBottomNavigationItem item4 = new AHBottomNavigationItem("Notification", R.drawable.ic_notifications_black_24dp, R.color.blue_500);
        AHBottomNavigationItem item5 = new AHBottomNavigationItem("Profile", R.drawable.ic_person_black_24dp, R.color.blue_500);

        // Add items
        pBottomNavigation.addItem(item1);
        pBottomNavigation.addItem(item2);
        pBottomNavigation.addItem(item3);
        pBottomNavigation.addItem(item4);
        pBottomNavigation.addItem(item5);

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
        pBottomNavigation.setCurrentItem(0);

        // Customize notification (title, background, typeface)
//        pBottomNavigation.setNotificationBackgroundColor(Color.parseColor("#F63D2B"));
//
//        // Add or remove notification for each item
//        pBottomNavigation.setNotification("1", 2);
        // OR
//        AHNotification notification = new AHNotification.Builder()
//                .setText("1")
//                .setBackgroundColor(ContextCompat.getColor(MainActivity.this, R.color.black))
//                .setTextColor(ContextCompat.getColor(MainActivity.this, R.color.white))
//                .build();
//        pBottomNavigation.setNotification(notification, 1);


        // Set listeners
        pBottomNavigation.setOnTabSelectedListener(new AHBottomNavigation.OnTabSelectedListener() {
            @Override
            public boolean onTabSelected(int position, boolean wasSelected) {
                // Do something cool here...
                mFragmentTransaction = mFragmentManager.beginTransaction();
                Fragment vFragment = null;

                switch (position) {
                    case 0:
                        vFragment = new HomeFragment();
                        break;
                    case 1:
                        vFragment = new TimingFragment();

                        break;

                    case 2:
                        vFragment = new QRFragment();
                        break;
                    case 3:
                        vFragment = new NotificationFragment();
                        break;
                    case 4:
                        vFragment = new ProfileFragment();

                        break;
                    default:
                        vFragment = new HomeFragment();
                }
                mFragmentTransaction.replace(R.id.frame_user_holder, vFragment, "frag");
                mFragmentTransaction.addToBackStack(null);
                mFragmentTransaction.commit();
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

    @Override
    protected void onResume() {
        super.onResume();
        Map<String, Object> mp = new HashMap<>();
        mp.put("timestamp", FieldValue.serverTimestamp());
        mFirebaseFirestore.collection("company_data").document("timestamp").update(mp);
    }
}