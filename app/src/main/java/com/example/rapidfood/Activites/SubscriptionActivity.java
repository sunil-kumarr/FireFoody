package com.example.rapidfood.Activites;

import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;


import com.example.rapidfood.Adapters.SubscriptionAdapter;
import com.example.rapidfood.R;
import com.example.rapidfood.Utils.CenterZoomLinearLayoutManager;
import com.example.rapidfood.Utils.FirebaseInstances;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.squareup.picasso.Picasso;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class SubscriptionActivity extends AppCompatActivity {

    private FirebaseStorage mFirebaseStorage;
    private FirebaseInstances mFirebaseInstances;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseFirestore mFirebaseFirestore;
    private FirebaseUser mFirebaseUser;
    private RecyclerView mRecyclerView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.subscription_main);
        ImageView vImageView = findViewById(R.id.imageView);
        Toolbar mToolbar = findViewById(R.id.toolbar_subscription);
        setSupportActionBar(mToolbar);
        ActionBar vActionBar = getSupportActionBar();
        if (vActionBar != null) {
            vActionBar.setDisplayShowTitleEnabled(false);
            vActionBar.setDisplayShowHomeEnabled(true);
            vActionBar.setDisplayHomeAsUpEnabled(true);
            vActionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_white_black_24dp);
        }

        mFirebaseInstances = new FirebaseInstances();
        mFirebaseStorage = mFirebaseInstances.getFirebaseStorage();
        mFirebaseFirestore = mFirebaseInstances.getFirebaseFirestore();
        setImageSub(vImageView);

        mRecyclerView = findViewById(R.id.recyclerView);
        SubscriptionAdapter vSubscriptionAdapter = new SubscriptionAdapter();
        mRecyclerView.setLayoutManager(new CenterZoomLinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(vSubscriptionAdapter);
        mRecyclerView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    Animation anim = AnimationUtils.loadAnimation(SubscriptionActivity.this, R.anim.scale_in);
                    v.startAnimation(anim);
                    ViewCompat.setElevation(v, 20);
                    anim.setFillAfter(true);

                } else {
                    Animation anim = AnimationUtils.loadAnimation(SubscriptionActivity.this, R.anim.scale_out);
                    v.startAnimation(anim);
                    ViewCompat.setElevation(v, 4);
                    anim.setFillAfter(true);
                }
            }
        });
    }

    private void setImageSub(final View pView) {
        mFirebaseFirestore.collection("subscriptions").document("image")
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
