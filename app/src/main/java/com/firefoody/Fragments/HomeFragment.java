package com.firefoody.Fragments;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.firefoody.Activites.PackageDetailsActivity;
import com.firefoody.Adapters.HomeAdapter;
import com.firefoody.Adapters.HomeViewHolder;
import com.firefoody.Adapters.ShowSubscriptionAdapter;
import com.firefoody.Models.FoodOffered;
import com.firefoody.Models.PackageModel;
import com.firefoody.Models.SubscriptionModel;
import com.firefoody.R;
import com.firefoody.Utils.FirebaseInstances;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import de.hdodenhof.circleimageview.CircleImageView;

public class HomeFragment extends Fragment {
    private Context mContext;
    private RecyclerView mHomeRecycler, mSubscriptionRecycler;
    private FirebaseFirestore mFirebaseFirestore;
    private FirestoreRecyclerOptions<SubscriptionModel> mSubscriptionModelFirestoreRecyclerOptions;
    private FirestoreRecyclerOptions<PackageModel> mPackageModelFirestoreRecyclerOptions;
    private FirestoreRecyclerAdapter mSubAdapter,mHomeAadapter;
    private LinearLayout mLoadingPAge;
    private MaterialCardView OpenCurrenMEnu;
    private TextView mFoodtype,mFoodTimnigs;
    private CircleImageView mFoodIcon;
    private ImageView mFoodImage;
    private static final String TAG = "HomeFragment";
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mContext = context;
        FirebaseInstances vFirebaseInstances = new FirebaseInstances();
        mFirebaseFirestore = vFirebaseInstances.getFirebaseFirestore();
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

//        mLoadingPAge=view.findViewById(R.id.loading_data_page);
//        mHomeRecycler = view.findViewById(R.id.home_recyclerview);
        OpenCurrenMEnu = view.findViewById(R.id.CurrentMenuItem);
        mFoodImage = view.findViewById(R.id.food_image_showcase);
        mFoodIcon=view.findViewById(R.id.food_image);
        mFoodtype=view.findViewById(R.id.food_type);
        mFoodTimnigs=view.findViewById(R.id.food_timings);
        mSubscriptionRecycler = view.findViewById(R.id.subscription_recyclerview);
//        mHomeRecycler.setHasFixedSize(true);
//        LinearLayoutManager llm = new LinearLayoutManager(mContext, RecyclerView.VERTICAL, false);
        LinearLayoutManager sbm = new LinearLayoutManager(mContext, RecyclerView.VERTICAL, false);
//        mHomeRecycler.setLayoutManager(llm);
        mSubscriptionRecycler.setLayoutManager(sbm);
        OpenCurrenMEnu.setOnClickListener( new View.OnClickListener() {
                                               @Override
                                               public void onClick(View v) {
                                                   Intent i=new Intent(mContext, PackageDetailsActivity.class);
                                                   i.putExtra("package_name","Lunch");
                                                   mContext.startActivity(i);
                                               }
                                           });
        getFoodOffered();
        getSubscriptionsData();
    }

    private void getFoodOffered() {
        mFirebaseFirestore.collection("today_menu").document("today_menu")
                .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
              if(documentSnapshot.exists())
              {
                  FoodOffered mFood = documentSnapshot.toObject(FoodOffered.class);
                  PackageModel mPack = mFood.getFoodPack();
                  mFoodTimnigs.setText(mPack.getDescription());
                  mFoodtype.setText(mPack.getName());
                  if(mPack.isBreakfast())
                  {
                      mFoodIcon.setImageResource(R.drawable.ic_coffee);
                  }else{
                      mFoodIcon.setImageResource(R.drawable.ic_lunch);
                  }
                  Picasso.get()
                          .load(mPack.getImage())
                          .networkPolicy(NetworkPolicy.OFFLINE)
                          .fit()
                          .into(mFoodImage, new Callback() {
                              @Override
                              public void onSuccess() {
//                                    mLoadingPAge.setVisibility(View.GONE);
                              }

                              @Override
                              public void onError(Exception e) {
                                  //Try again online if cache failed
                                  Picasso.get()
                                          .load(mPack.getImage())
                                          .fit()
                                          .error(R.drawable.ic_undraw_failure)
                                          .into(mFoodImage, new Callback() {
                                              @Override
                                              public void onSuccess() {
                                              }

                                              @Override
                                              public void onError(Exception e) {
                                                  Log.v("Picasso", "Could not fetch image");
                                              }
                                          });
                              }
                          });
              }
            }
        });
    }

    private void getSubscriptionsData() {
        Query query = mFirebaseFirestore
                .collection("subscriptions");
        mSubscriptionModelFirestoreRecyclerOptions = new FirestoreRecyclerOptions.Builder<SubscriptionModel>()
                .setQuery(query, SubscriptionModel.class).build();
        mSubAdapter = new ShowSubscriptionAdapter(mSubscriptionModelFirestoreRecyclerOptions);
        mSubscriptionRecycler.post(new Runnable() {
            @Override
            public void run() {
                mSubscriptionRecycler.setAdapter(mSubAdapter);
                mSubscriptionRecycler.setItemAnimator(new DefaultItemAnimator());
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        mSubAdapter.startListening();
    }
    @Override
    public void onResume() {
        super.onResume();
    }
    @Override
    public void onStop() {
        super.onStop();
        mSubAdapter.stopListening();
    }
}