package com.example.rapidfood.Vendor_files;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.rapidfood.Adapters.SelectTodayMenuAdapter;
import com.example.rapidfood.Models.SubscriptionModel;
import com.example.rapidfood.Models.VendorBreakFastItem;
import com.example.rapidfood.Models.VendorDishModel;
import com.example.rapidfood.R;
import com.example.rapidfood.Utils.FirebaseInstances;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.firebase.ui.firestore.SnapshotParser;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class VendorTodayMenuActivity extends AppCompatActivity {

    private static final String TAG = "VendorTodayMenuActivity";

    private FirebaseInstances mFirebaseInstances;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseFirestore mFirebaseFirestore;
    private RecyclerView mTodayRecycler;
    private FirestoreRecyclerOptions<VendorDishModel> options;
    private FirestoreRecyclerAdapter TodayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vendor_today_menu);
        Toolbar vToolbar=findViewById(R.id.toolbar_today);
        setSupportActionBar(vToolbar);
        ActionBar vActionBar=getSupportActionBar();
        if(vActionBar!=null){
            vActionBar.setDisplayHomeAsUpEnabled(true);
            vActionBar.setDisplayShowHomeEnabled(true);
            vActionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_white_black_24dp);
        }

        mFirebaseInstances=new FirebaseInstances();
        mFirebaseFirestore=mFirebaseInstances.getFirebaseFirestore();
        mTodayRecycler=findViewById(R.id.today_recycler);
        getAllDataFireStore();
    }
    
    private void getAllDataFireStore()
    {
        Log.d(TAG,"MAKING ");
        Toast.makeText(this, "Recycler init", Toast.LENGTH_SHORT).show();

        mTodayRecycler.setHasFixedSize(true);
       LinearLayoutManager llm = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
       mTodayRecycler.setLayoutManager(llm);

        Query query = mFirebaseFirestore
                .collection("dishes_main");

        options = new FirestoreRecyclerOptions.Builder<VendorDishModel>()
                .setQuery(query, new SnapshotParser<VendorDishModel>() {
                    @NonNull
                    @Override
                    public VendorDishModel parseSnapshot(@NonNull DocumentSnapshot snapshot) {
                        VendorDishModel vModel=new VendorDishModel();
                        vModel.setName(snapshot.getString("name"));
                        Toast.makeText(VendorTodayMenuActivity.this, "CALLEd:  "+snapshot.getString("name"), Toast.LENGTH_SHORT).show();
                        return vModel;
                    }
                }).build();
        TodayAdapter = new SelectTodayMenuAdapter(options,mTodayRecycler,this);
        mTodayRecycler.post(new Runnable() {
            @Override
            public void run() {
                mTodayRecycler.setAdapter(TodayAdapter);
                mTodayRecycler.setItemAnimator(new DefaultItemAnimator());
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        TodayAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        TodayAdapter.stopListening();
    }
}
