package com.example.rapidfood.Activites;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import de.hdodenhof.circleimageview.CircleImageView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import com.example.rapidfood.R;
import com.example.rapidfood.Utils.FirebaseInstances;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.squareup.picasso.Picasso;

import javax.annotation.Nullable;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private Toolbar mToolbar;
    private TextView userTextName, userTextMobile, userTextEmail;
    private CircleImageView userImage;
    private NavigationView mNavigationView;
    private static final String TAG = "MainActivity";
    private DrawerLayout mDrawerLayout;
    private FirebaseInstances mFirebaseInstances;
    private FirebaseUser mFirebaseUser;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseFirestore mFirebaseFirestore;
    private FragmentManager mFragmentManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mToolbar = findViewById(R.id.toolbar_vendor);
        mNavigationView = findViewById(R.id.nav_menu_user);
        Toast.makeText(this, "" + TAG, Toast.LENGTH_SHORT).show();
        mDrawerLayout = findViewById(R.id.drawerLayout);
        setSupportActionBar(mToolbar);
        ActionBar vActionBar = getSupportActionBar();
        if (vActionBar != null) {
            vActionBar.setDisplayShowHomeEnabled(true);
            vActionBar.setDisplayHomeAsUpEnabled(true);
            vActionBar.setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp);
        }
        mFirebaseInstances = new FirebaseInstances();
        mFirebaseAuth = mFirebaseInstances.getFirebaseAuth();
        mFirebaseFirestore = mFirebaseInstances.getFirebaseFirestore();

        mFragmentManager=getSupportFragmentManager();

        View pUser = mNavigationView.getHeaderView(0);
        userTextName = pUser.findViewById(R.id.name);
        userTextMobile = pUser.findViewById(R.id.mobile);
        userImage = pUser.findViewById(R.id.profile_image);
        userTextEmail = pUser.findViewById(R.id.email_id);
        getUserDetails();


        mNavigationView.setNavigationItemSelectedListener(this);
        mNavigationView.getMenu().getItem(0).setChecked(true);
        onNavigationItemSelected(mNavigationView.getMenu().getItem(0));
    }

    @Override
    protected void onStart() {
        super.onStart();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();

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
        FragmentTransaction vFragmentTransaction=mFragmentManager.beginTransaction();
        switch (pMenuItem.getItemId()) {
            case R.id.my_home:

                    break;
            case R.id.my_profile:
                startActivity(new Intent(MainActivity.this, ProfileActivity.class));
                break;
            case R.id.subscriptions:
                startActivity(new Intent(MainActivity.this, SubscriptionActivity.class));
                break;
            case R.id.logout:
                mFirebaseAuth.signOut();
                startActivity(new Intent(MainActivity.this, Authentication.class));
                finish();
                break;
        }
        mDrawerLayout.closeDrawers();
        return true;
    }

    private void getUserDetails() {
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        assert mFirebaseUser != null;
        String userName = mFirebaseUser.getUid();
        mFirebaseFirestore.collection("users").document(userName)
                .addSnapshotListener(MainActivity.this, new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot pDocumentSnapshot, @Nullable FirebaseFirestoreException pE) {
                        userTextName.setText(pDocumentSnapshot.getString("username"));
                        userTextMobile.setText(pDocumentSnapshot.getString("mobile"));
                        userTextEmail.setText(pDocumentSnapshot.getString("emailAddress"));
                        Picasso.get()
                                .load(pDocumentSnapshot.getString("profileimage"))
                                .resize(80, 80)
                                .centerCrop()
                                .placeholder(R.drawable.man)
                                .into(userImage);
                    }
                });
    }
}
