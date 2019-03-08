package com.example.rapidfood.Activites;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
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

public class VendorAddSubscription extends AppCompatActivity {


    private FirebaseStorage mFirebaseStorage;
    private FirebaseInstances mFirebaseInstances;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseFirestore mFirebaseFirestore;
    private FirebaseUser mFirebaseUser;
    private RecyclerView mRecyclerView;

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
