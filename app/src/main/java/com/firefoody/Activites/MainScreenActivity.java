package com.firefoody.Activites;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.firefoody.R;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;


public class MainScreenActivity extends AppCompatActivity {
    private static final int RC_SIGN_IN = 452;
    PreferenceManager preferenceManager;
    LinearLayout Layout_bars;
    ImageView[] bottomBars;
    int[] screens;
    Button Skip, Next;
    ViewPager vp;
    MyViewPagerAdapter myvpAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);

        //Get the object of PreferenceManager class
        // To ensure that intro screen is only shown once
        preferenceManager = new PreferenceManager(this);
        // if Intro Screen has been already shown then just move to MainActivity From here only
        if (!preferenceManager.FirstLaunch()) {
            startLoginProcess();
        }
        //Get Array of screens to be shown on the intro swipe screen
        screens = new int[]{
                R.layout.intro_screen_1,
                R.layout.intro_screen_2,
                R.layout.intro_screen_3
        };
        //Get Reference  to Different View Like:
        // ViewPager to hold screens
        vp = (ViewPager) findViewById(R.id.view_pager);
        //LinearLayout
        Layout_bars = (LinearLayout) findViewById(R.id.layoutBars);
        // Button to show skip option
        //Button to show next option
        Next = (Button) findViewById(R.id.next);

        //Get the Object of PagerAdapter Inner Class here
        myvpAdapter = new MyViewPagerAdapter();
        //Set the adapter on the viewpager
        vp.setAdapter(myvpAdapter);

        //Add Listener on the ViewPager
        //to listen for page swipe and change
        vp.addOnPageChangeListener(viewPagerPageChangeListener);
        ColoredBars(0);
    }
    //Used to get Current
    private int getItem(int i) {
        return vp.getCurrentItem() + i;
    }

    //Method to move to next screen
    public void next(View v) {
        int i = getItem(+1);
        if (i < screens.length) {
            // Set Current ViewPager Screen as Position Passed
            vp.setCurrentItem(i);
        } else {
            // Start Option
            startLoginProcess();
        }
    }
    public void startLoginProcess() {
        preferenceManager.setFirstTimeLaunch(false);
        startActivity(new Intent(MainScreenActivity.this,Authentication.class));
        finish();

    }


    private void ColoredBars(int thisScreen) {
        bottomBars = new ImageView[screens.length];
        // LinearLayout to hold Bottom Bars as Dots
        Layout_bars.removeAllViews();
        for (int i = 0; i < bottomBars.length; i++) {
            bottomBars[i] = new ImageView(this);
            bottomBars[i].setImageResource(R.drawable.inactive_dot);
            bottomBars[i].setPadding(5,5,5,5);
            Layout_bars.addView(bottomBars[i]);
        }
        if (bottomBars.length > 0){
            bottomBars[thisScreen].setPadding(5,5,5,5);
            bottomBars[thisScreen].setImageResource(R.drawable.active_dot);}
    }




    //Create PAgeChangeListener Object with settings
    ViewPager.OnPageChangeListener viewPagerPageChangeListener = new ViewPager.OnPageChangeListener() {

        @Override
        public void onPageSelected(int position) {
            // set the Dot Bar Position
            ColoredBars(position);
            // if the Shown Screen is the last one
            // Position =2 and Screen=3
            if (position == screens.length - 1) {
                Next.setText("let's get started");
            } else {
                // For Screens from 1 -2
                Next.setText(getString(R.string.next));
            }
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {

        }

        @Override
        public void onPageScrollStateChanged(int arg0) {

        }
    };

    public class MyViewPagerAdapter extends PagerAdapter {
        private LayoutInflater inflater;
        MyViewPagerAdapter() {
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            //Get the Reference to LayoutInflater Object
            inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            // Inflate the correct Screen visible to user
            View view = inflater.inflate(screens[position], container, false);
            //Add this view to the container ViewGroup
            container.addView(view);
            return view;
        }

        // Get  Total Number Of Screens to be Displayed in viewpager
        @Override
        public int getCount() {
            return screens.length;
        }
        // Remove the non-visible screen from container ViewGroup
        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            View v = (View) object;
            container.removeView(v);
        }

        //
        @Override
        public boolean isViewFromObject(@NonNull View v, @NonNull Object object) {
            return v == object;
        }
    }
}
