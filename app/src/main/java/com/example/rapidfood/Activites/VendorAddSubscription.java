package com.example.rapidfood.Activites;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.rapidfood.Adapters.CreateSubscriptionAdapter;
import com.example.rapidfood.R;
import com.example.rapidfood.Utils.CenterZoomLinearLayoutManager;
import com.example.rapidfood.Utils.FirebaseInstances;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.squareup.picasso.Picasso;

public class VendorAddSubscription extends AppCompatActivity {


    private FirebaseStorage mFirebaseStorage;
    private FirebaseInstances mFirebaseInstances;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseFirestore mFirebaseFirestore;
    private FirebaseUser mFirebaseUser;
    private RecyclerView mRecyclerView;
    private FloatingActionButton mButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vendor_add_subscription);
        ImageView vImageView = findViewById(R.id.imageView);
        Toolbar mToolbar = findViewById(R.id.toolbar_subscription);
        setSupportActionBar(mToolbar);
        ActionBar vActionBar = getSupportActionBar();
        if (vActionBar != null) {
            vActionBar.setDisplayShowTitleEnabled(false);
            vActionBar.setDisplayShowHomeEnabled(true);
            vActionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_white_black_24dp);
        }
        mFirebaseInstances = new FirebaseInstances();
        mFirebaseStorage = mFirebaseInstances.getFirebaseStorage();
        mFirebaseFirestore = mFirebaseInstances.getFirebaseFirestore();
        setImageSub(vImageView);
       mButton= findViewById(R.id.btn_add_subscription);
       mButton.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               startActivity(new Intent(VendorAddSubscription.this,VendorCreateSubscription.class));
           }
       });
        mRecyclerView = findViewById(R.id.recyclerView);
        CreateSubscriptionAdapter vCreateSubscriptionAdapter = new CreateSubscriptionAdapter();
        mRecyclerView.setLayoutManager(new CenterZoomLinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(vCreateSubscriptionAdapter);
    }

    private void setImageSub(final View pView) {
        mFirebaseFirestore.collection("poster_images").document("image")
                .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot pDocumentSnapshot) {
                if (pDocumentSnapshot.exists()) {
                    Picasso.get()
                            .load(pDocumentSnapshot.getString("headImage"))
                            .fit()
                            .into((ImageView) pView);
                }
            }
        });
    }
}
