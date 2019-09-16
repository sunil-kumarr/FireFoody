package com.firefoody.VendorActivities;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.firefoody.Adapters.SelectTodayMenuAdapter;
import com.firefoody.Models.FoodOffered;
import com.firefoody.Models.PackageModel;
import com.firefoody.Models.VendorDishModel;
import com.firefoody.R;
import com.firefoody.Utils.FirebaseInstances;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class VendorTodayMenuActivity extends AppCompatActivity {

    private static final String TAG = "VendorTodayMenuActivity";

    private FirebaseInstances mFirebaseInstances;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseFirestore mFirebaseFirestore;
    private RecyclerView mTodayRecycler;
    private FirestoreRecyclerOptions<VendorDishModel> options;
    private FirestoreRecyclerAdapter TodayAdapter;
    private Button mPublishMenu;
    ProgressDialog vProgressDialog;
    private RadioGroup mFoodOffered;
    private AlertDialog publishDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vendor_today_menu);
        Toolbar vToolbar = findViewById(R.id.toolbar_today);
        setSupportActionBar(vToolbar);
        ActionBar vActionBar = getSupportActionBar();
        if (vActionBar != null) {
            vActionBar.setDisplayHomeAsUpEnabled(true);
            vActionBar.setDisplayShowHomeEnabled(true);
            vActionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_black_24dp);
        }
        mFirebaseInstances = new FirebaseInstances();
        mFirebaseFirestore = mFirebaseInstances.getFirebaseFirestore();
        mTodayRecycler = findViewById(R.id.today_recycler);
        mPublishMenu = findViewById(R.id.publish_menu);
        mFoodOffered = findViewById(R.id.currentFoodType);
        getAllDataFireStore();

        mPublishMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                publishDialog = new AlertDialog.Builder(VendorTodayMenuActivity.this)
                        .setTitle("Publish Today Menu!!")
                        .setCancelable(false)
                        .setMessage("Are you sure to publish this menu \n after you click confirm this will replace any previous menu and users will see this menu ?")
                        .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                uploadTodayMenu();
                                finish();
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(VendorTodayMenuActivity.this, "Publish cancelled!", Toast.LENGTH_SHORT).show();
                            }
                        }).show();
            }
        });


    }

    private void uploadTodayMenu() {
        vProgressDialog = new ProgressDialog(this);
        vProgressDialog.setCancelable(false);
        vProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        vProgressDialog.setMessage("Publishing menu....");
        vProgressDialog.show();
        List<VendorDishModel> vModels = ((SelectTodayMenuAdapter) TodayAdapter).getSelectedItems();
        FoodOffered mFood = new FoodOffered();
        mFood.setmCurrentMenuList(vModels);
        final int foodtypeId = mFoodOffered.getCheckedRadioButtonId();
        getPackList(new MyDataCallBack() {
            @Override
            public void onCallback(List<PackageModel> pPackageModels) {
                RadioButton rb = findViewById(foodtypeId);
                String type = (String) rb.getText();
                CollectionReference vToday_menu = mFirebaseFirestore.collection("today_menu");
                for(PackageModel x:pPackageModels)
                {
                    if(x.getName().equals(type)){
                        mFood.setFoodPack(x);
                        break;
                    }
                }
                vToday_menu.document("today_menu").set(mFood)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void pVoid) {
                                Toast.makeText(VendorTodayMenuActivity.this, "Menu Updated", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });
    }


    // all dish in today menu section
    private void getAllDataFireStore() {
        Log.d(TAG, "MAKING ");
        //    Toast.makeText(this, "Recycler init adknlsdna", Toast.LENGTH_SHORT).show();

        mTodayRecycler.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        mTodayRecycler.setLayoutManager(llm);

        Query query = mFirebaseFirestore
                .collection("dishes_main");

        options = new FirestoreRecyclerOptions.Builder<VendorDishModel>()
                .setQuery(query, VendorDishModel.class).build();
        TodayAdapter = new SelectTodayMenuAdapter(options, mTodayRecycler, this);
        mTodayRecycler.post(new Runnable() {
            @Override
            public void run() {
                mTodayRecycler.setAdapter(TodayAdapter);
                mTodayRecycler.setItemAnimator(new DefaultItemAnimator());
            }
        });
    }


    private void getPackList(final MyDataCallBack pMyDataCallBack) {
        mFirebaseFirestore.collection("packages").orderBy("name").get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot pQueryDocumentSnapshots) {
                        final List<PackageModel> pack = new ArrayList<>();
                        for (QueryDocumentSnapshot doc : pQueryDocumentSnapshots) {
                            pack.add(doc.toObject(PackageModel.class));
                        }
                        pMyDataCallBack.onCallback(pack);
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
//        vProgressDialog.dismiss();
    }

    private interface MyDataCallBack {
        void onCallback(List<PackageModel> pPackageModels);
    }


}
