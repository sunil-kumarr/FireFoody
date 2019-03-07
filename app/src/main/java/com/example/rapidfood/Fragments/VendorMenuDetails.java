package com.example.rapidfood.Fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.rapidfood.Activites.VendorAddMenu;
import com.example.rapidfood.Adapters.MenuAdapter;
import com.example.rapidfood.Models.VendorMenuItem;
import com.example.rapidfood.R;
import com.example.rapidfood.Utils.FirebaseInstances;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class VendorMenuDetails extends Fragment {
    FloatingActionButton mAddMenuBtn;
    RecyclerView mRecyclerView;
    private ArrayList<VendorMenuItem> mItems;
    private FirebaseFirestore mFirebaseFirestore;
    Context mContext;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mContext=context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        FirebaseInstances vFirebaseInstances=new FirebaseInstances();
        mFirebaseFirestore=vFirebaseInstances.getFirebaseFirestore();
        return inflater.inflate(R.layout.vendor_menu_details,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mAddMenuBtn=view.findViewById(R.id.addMenuButton);
        mAddMenuBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), VendorAddMenu.class));
            }
        });
        mRecyclerView=view.findViewById(R.id.recyclerMenu);
        getAllMenu();

    }
    private  void getAllMenu(){
        mFirebaseFirestore.collection("menus")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value,
                                        @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            Log.d("DownloadData", "Listen failed.", e);
                            return;
                        }

                         mItems= new ArrayList<>();
                        Toast.makeText(mContext, ""+value.size(), Toast.LENGTH_SHORT).show();
                        for (QueryDocumentSnapshot doc : value) {
                           // Toast.makeText(mContext, ""+doc.getString("itemname"), Toast.LENGTH_SHORT).show();
                                VendorMenuItem vItem=new VendorMenuItem();
                                vItem.setItemname(doc.getString("itemname"));
                                vItem.setS1("Quantity: "+doc.getString("s1"));
                                vItem.setItemdescription(doc.getString("itemdescription"));
                                vItem.setItemImageid(doc.getString("itemImageid"));
                                vItem.setItemCategory(doc.getString("itemCategory"));
                                mItems.add(vItem);
                        }
                        mRecyclerView.setAdapter(new MenuAdapter(mItems));
                        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
                        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext,RecyclerView.VERTICAL,false));
                    }
                });
    }
}
