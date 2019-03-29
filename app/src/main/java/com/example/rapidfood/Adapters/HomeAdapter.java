package com.example.rapidfood.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.rapidfood.Models.VendorDishModel;
import com.example.rapidfood.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.squareup.picasso.Picasso;

import androidx.annotation.NonNull;

public class HomeAdapter extends FirestoreRecyclerAdapter<VendorDishModel, HomeViewHolder> {

    public HomeAdapter(@NonNull FirestoreRecyclerOptions<VendorDishModel> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull HomeViewHolder pHomeViewHolder, int pI, @NonNull VendorDishModel pVendorDishModel) {
        Picasso.get().load(pVendorDishModel.getImage()).fit().into(pHomeViewHolder.mImageView);
        pHomeViewHolder.mName.setText(pVendorDishModel.getName());
        pHomeViewHolder.mDesc.setText(pVendorDishModel.getDescription());
        pHomeViewHolder.mPrice.setText(pVendorDishModel.getMoney());
    }

    @NonNull
    @Override
    public HomeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View lay=LayoutInflater.from(parent.getContext()).inflate(R.layout.vendor_dishitem_layout,parent,false);
        return new HomeViewHolder(lay);
    }
}
