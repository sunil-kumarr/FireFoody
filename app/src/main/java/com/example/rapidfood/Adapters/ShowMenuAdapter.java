package com.example.rapidfood.Adapters;

import android.content.Context;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.rapidfood.Models.VendorMenuItem;
import com.example.rapidfood.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ShowMenuAdapter extends FirestoreRecyclerAdapter<VendorMenuItem, ShowMenuAdapter.MenuViewHolder> {

    private RecyclerView mRecyclerView;
    public ShowMenuAdapter(@NonNull FirestoreRecyclerOptions<VendorMenuItem> options,RecyclerView pRecyclerView) {
        super(options);
        mRecyclerView=pRecyclerView;
    }
    @NonNull
    @Override
    public MenuViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView= LayoutInflater.from(parent.getContext()).inflate(R.layout.vendor_menu_item,parent,false);
        int width = mRecyclerView.getWidth();
        ViewGroup.LayoutParams params = itemView.getLayoutParams();
        params.width = (int) (width * 0.8);
        itemView.setLayoutParams(params);
          return new MenuViewHolder(itemView);
    }



    @Override
    protected void onBindViewHolder(@NonNull MenuViewHolder holder, int pI, @NonNull VendorMenuItem vItem) {
        holder.name.setText(vItem.getItemname());
        holder.desc.setText(vItem.getItemdescription());
        holder.quant.setText(vItem.getS1());
        holder.showImage(vItem.getItemImageid());
    }


    class MenuViewHolder extends RecyclerView.ViewHolder{
        TextView name,desc,quant,category;
        ImageView mView;
        MenuViewHolder(@NonNull View itemView) {
            super(itemView);
            name=itemView.findViewById(R.id.item_Name);
            desc=itemView.findViewById(R.id.item_Desc);
            quant=itemView.findViewById(R.id.item_Quant);
            mView=itemView.findViewById(R.id.item_Image);
            category=itemView.findViewById(R.id.item_Category);
        }
        void showImage(String url) {
            if (url != null && !url.isEmpty()) {
                Picasso.get()
                        .load(url)
                        .centerCrop()
                        .resize(80,150)

                        .into(mView);
            }
        }
    }
}
