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
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rapidfood.Adapters.SubscriptionAdapter;
import com.example.rapidfood.Fragments.VendorMenuDetails;
import com.example.rapidfood.R;
import com.example.rapidfood.Utils.FirebaseInstances;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    Toolbar mToolbar;
    NavigationView mNavigationView;
    private static final String TAG = "MainActivity";
    DrawerLayout mDrawerLayout;
    private FirebaseInstances mFirebaseInstances;
    private FirebaseUser mFirebaseUser;
    private FirebaseAuth mFirebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mToolbar = findViewById(R.id.toolbar_vendor);
        mNavigationView = findViewById(R.id.nav_menu_user);
        Toast.makeText(this, ""+TAG, Toast.LENGTH_SHORT).show();
        mDrawerLayout = findViewById(R.id.drawerLayout);
        setSupportActionBar(mToolbar);
        ActionBar vActionBar = getSupportActionBar();
        if (vActionBar != null) {
            vActionBar.setDisplayShowHomeEnabled(true);
            vActionBar.setDisplayHomeAsUpEnabled(true);
            vActionBar.setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp);
        }
        TextView user=findViewById(R.id.userId);
        mFirebaseInstances = new FirebaseInstances();
        mFirebaseAuth=mFirebaseInstances.getFirebaseAuth();
        mNavigationView.setNavigationItemSelectedListener(this);
        mNavigationView.getMenu().getItem(0).setChecked(true);
        onNavigationItemSelected(mNavigationView.getMenu().getItem(0));
        user.setText(mFirebaseInstances.getFirebaseAuth().getCurrentUser().getUid());
    }

    @Override
    protected void onStart() {
        super.onStart();
        mFirebaseUser=mFirebaseAuth.getCurrentUser();
        mNavigationView.getMenu().getItem(0).setChecked(true);
        onNavigationItemSelected(mNavigationView.getMenu().getItem(0));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem pMenuItem) {
        pMenuItem.setChecked(true);

        switch (pMenuItem.getItemId()) {
            case R.id.my_profile:
                startActivity(new Intent(MainActivity.this, ProfileActivity.class));
                break;
            case  R.id.subscriptions:
                startActivity(new Intent(MainActivity.this, SubscriptionActivity.class));
                break;
            case R.id.logout:
                mFirebaseAuth.signOut();
                startActivity(new Intent(MainActivity.this,Authentication.class));
                finish();
                break;
        }
        mDrawerLayout.closeDrawers();
        return true;
    }
}
