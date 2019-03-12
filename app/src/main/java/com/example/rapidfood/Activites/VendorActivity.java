package com.example.rapidfood.Activites;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rapidfood.Fragments.VendorMenuDetails;
import com.example.rapidfood.Models.VendorModel;
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

public class VendorActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private Toolbar mToolbar;
    private TextView userTextName, userTextMobile, userTextEmail;
    private CircleImageView userImage;
    private FragmentTransaction vFragmentTransaction;
    private NavigationView mNavigationView;
    private static final String TAG = "VendorActivity";
    private DrawerLayout mDrawerLayout;
    private FirebaseInstances mFirebaseInstances;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseFirestore mFirebaseFirestore;
    private FirebaseUser mFirebaseUser;
    private FragmentManager mFragmentManager;
    private VendorModel mVendorModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vendor);
        mToolbar = findViewById(R.id.toolbar_vendor);
        Toast.makeText(this, TAG, Toast.LENGTH_SHORT).show();
        mNavigationView = findViewById(R.id.nav_menu_vendor);
        mDrawerLayout = findViewById(R.id.drawerLayout);
        setSupportActionBar(mToolbar);
        ActionBar vActionBar = getSupportActionBar();
        if (vActionBar != null) {
            vActionBar.setDisplayShowHomeEnabled(true);
            vActionBar.setDisplayHomeAsUpEnabled(true);
            vActionBar.setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp);
        }
        mFragmentManager = getSupportFragmentManager();
        mFirebaseInstances = new FirebaseInstances();
        mFirebaseAuth = mFirebaseInstances.getFirebaseAuth();
        mFirebaseFirestore = mFirebaseInstances.getFirebaseFirestore();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        mVendorModel = new VendorModel();

        View pUser = mNavigationView.getHeaderView(0);
        userTextName = pUser.findViewById(R.id.name);
        userTextMobile = pUser.findViewById(R.id.mobile);
        userImage = pUser.findViewById(R.id.profile_image);
        userTextEmail = pUser.findViewById(R.id.email_id);
        getUserDetails();
        Toast.makeText(this, "" + mVendorModel.getMobile(), Toast.LENGTH_SHORT).show();

        mNavigationView.setNavigationItemSelectedListener(this);
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
    protected void onStart() {
        super.onStart();
       mFirebaseUser = mFirebaseAuth.getCurrentUser();
        mNavigationView.getMenu().getItem(0).setChecked(true);
        onNavigationItemSelected(mNavigationView.getMenu().getItem(0));
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem pMenuItem) {
        pMenuItem.setChecked(true);
        vFragmentTransaction = mFragmentManager.beginTransaction();
        Fragment vFragment = new VendorMenuDetails();
        Toast.makeText(VendorActivity.this, "" + pMenuItem.getItemId(), Toast.LENGTH_SHORT).show();
        switch (pMenuItem.getItemId()) {

            case R.id.menu_details:
                vFragment = new VendorMenuDetails();
                break;
            case R.id.subscriptions:
                startActivity(new Intent(VendorActivity.this,VendorAddSubscription.class));
                break;
            case R.id.packages:
                startActivity(new Intent(VendorActivity.this,VendorCreatePackage.class));
                break;
            case R.id.package_item:
                startActivity(new Intent(VendorActivity.this,VendorCreateMenuItem.class));
                break;
            case R.id.logout:
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(VendorActivity.this, Authentication.class));
                finish();
                break;

        }
        vFragmentTransaction.replace(R.id.frame_layout_vendor, vFragment, "frag_replace");
        vFragmentTransaction.commit();
        mDrawerLayout.closeDrawers();
        return true;
    }

    private void getUserDetails() {

        final String userName = mFirebaseUser.getUid();

        mFirebaseFirestore.collection("vendors").document(userName)
                .addSnapshotListener(VendorActivity.this,new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot pDocumentSnapshot, @Nullable FirebaseFirestoreException pE) {

                        userTextName.setText(pDocumentSnapshot.getString("name"));
                        userTextMobile.setText(pDocumentSnapshot.getString("mobile"));
                        userTextEmail.setText(pDocumentSnapshot.getString("email"));
                        Picasso.get()
                                .load(pDocumentSnapshot.getString("userimage"))
                                .resize(80, 80)
                                .centerCrop()
                                .placeholder(R.drawable.man)
                                .into(userImage);
                    }
                });
    }
}
