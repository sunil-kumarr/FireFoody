package com.example.rapidfood.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.rapidfood.Models.VendorBreakFastItem;
import com.example.rapidfood.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.squareup.picasso.Picasso;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ShowMenuBreakfastAdapter extends FirestoreRecyclerAdapter<VendorBreakFastItem, ShowMenuBreakfastAdapter.MenuViewHolder> {

    private RecyclerView mRecyclerView;
    public ShowMenuBreakfastAdapter(@NonNull FirestoreRecyclerOptions<VendorBreakFastItem> options, RecyclerView pRecyclerView) {
        super(options);
        mRecyclerView=pRecyclerView;

    }

    @NonNull
    @Override
    public MenuViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView= LayoutInflater.from(parent.getContext()).inflate(R.layout.vendor_dishitem_layout,parent,false);
        int width = mRecyclerView.getWidth();
        ViewGroup.LayoutParams params = itemView.getLayoutParams();
        params.width = (int) (width * 0.8);
        itemView.setLayoutParams(params);
          return new MenuViewHolder(itemView);
    }



    @Override
    protected void onBindViewHolder(@NonNull MenuViewHolder holder, int pI, @NonNull VendorBreakFastItem vItem) {
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
            quant=itemView.findViewById(R.id.dish_quantity);
            mView=itemView.findViewById(R.id.item_Image);
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
