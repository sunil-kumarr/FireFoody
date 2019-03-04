package com.example.rapidfood.Activites;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;


import com.example.rapidfood.Fragments.VendorMenuDetails;
import com.example.rapidfood.R;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class VendorActivity extends AppCompatActivity {

    Toolbar mToolbar;

    NavigationView mNavigationView;
    DrawerLayout mDrawerLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vendor);
        mToolbar=findViewById(R.id.toolbar_vendor);
        mNavigationView=findViewById(R.id.nav_menu);
        mDrawerLayout=findViewById(R.id.drawerLayout);
        setSupportActionBar(mToolbar);
        ActionBar vActionBar=getSupportActionBar();
        if(vActionBar!=null){
           vActionBar.setDisplayShowHomeEnabled(true);
           vActionBar.setDisplayHomeAsUpEnabled(true);
           vActionBar.setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp);
        }
        FragmentTransaction vFragmentTransaction=getSupportFragmentManager().beginTransaction();
        vFragmentTransaction.add(R.id.frame_layout,new VendorMenuDetails(),"frag");
        vFragmentTransaction.commit();
        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem pMenuItem) {
               pMenuItem.setChecked(true);
                FragmentTransaction vFragmentTransaction=getSupportFragmentManager().beginTransaction();
                Fragment vFragment=new VendorMenuDetails();
                switch (pMenuItem.getItemId()){
                    case R.id.menu_details:
                        vFragment=new VendorMenuDetails();
                        break;
                    case  R.id.logout:
                        FirebaseAuth.getInstance().signOut();
                        startActivity(new Intent(VendorActivity.this,Authentication.class));
                        finish();
                        break;

                }
                vFragmentTransaction.replace(R.id.frame_layout,vFragment,"frag");
                mDrawerLayout.closeDrawers();
                return true;
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
