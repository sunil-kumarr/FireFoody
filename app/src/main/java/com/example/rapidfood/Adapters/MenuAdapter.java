package com.example.rapidfood.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.rapidfood.Models.VendorMenuItem;
import com.example.rapidfood.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MenuAdapter extends MultiSelectableAdapter<MenuAdapter.MenuViewHolder> {

    private ArrayList<VendorMenuItem> mItems;
    private Context mContext;

    public MenuAdapter(ArrayList<VendorMenuItem> pItems) {
        mItems = pItems;
        Log.d("Adapterr","p:"+pItems.size());
    }

    @NonNull
    @Override
    public MenuViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mContext=parent.getContext();
        View vView= LayoutInflater.from(parent.getContext()).inflate(R.layout.vendor_menu_item,parent,false);
        return new MenuViewHolder(vView);
    }

    @Override
    public void onBindViewHolder(@NonNull MenuViewHolder holder, int position) {
        VendorMenuItem vItem=mItems.get(position);
        holder.name.setText(vItem.getItemname());
        holder.desc.setText(vItem.getItemdescription());
        holder.quant.setText(vItem.getS1());
        holder.showImage(vItem.getItemImageid());
        holder.category.setText("( "+vItem.getItemCategory()+" )");

    }

    @Override
    public int getItemCount() {
        return mItems!=null?mItems.size():0;
    }

    class MenuViewHolder extends RecyclerView.ViewHolder{
        TextView name,desc,quant,category;
        ImageView mView;
        public MenuViewHolder(@NonNull View itemView) {
            super(itemView);
            name=itemView.findViewById(R.id.item_Name);
            desc=itemView.findViewById(R.id.item_Desc);
            quant=itemView.findViewById(R.id.item_Quant);
            mView=itemView.findViewById(R.id.item_Image);
            category=itemView.findViewById(R.id.item_Category);
        }
        public void showImage(String url) {
            if (url != null && !url.isEmpty()) {
                Log.d("URL",url.toString());
                Picasso.get()
                        .load(url)
                        .centerCrop()
                        .resize(80,150)
                        .placeholder(R.drawable.foodplaceholder)
                        .into(mView);
                // Glide.with(this).load(url).into(mImageView);
            }
        }
    }
}
