package com.example.rapidfood.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.rapidfood.Models.VendorDishModel;
import com.example.rapidfood.R;
import com.squareup.picasso.Picasso;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class PackageDetailShowAdapter extends RecyclerView.Adapter<PackageDetailShowAdapter.PackageViewHolder> {

    private List<VendorDishModel> dishlist;
    private Context mContext;

    public PackageDetailShowAdapter(List<VendorDishModel> pDishlist, Context pContext) {
        dishlist = pDishlist;
        mContext = pContext;
    }

    @NonNull
    @Override
    public PackageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layout = LayoutInflater.from(parent.getContext()).inflate(R.layout.vendor_dishitem_layout, parent, false);
        return new PackageViewHolder(layout);
    }

    @Override
    public void onBindViewHolder(@NonNull PackageViewHolder holder, int position) {
        VendorDishModel vModel = dishlist.get(position);
        holder.dishName.setText(vModel.getName());
//        holder.dishPrice.setText(vModel.getMoney());
        holder.dishDetail.setText(vModel.getDescription());
        Picasso.get().load(vModel.getImage()).fit().into(holder.dishImage);
    }

    @Override
    public int getItemCount() {
        return dishlist.size() > 0 ? dishlist.size() : 0;
    }

    class PackageViewHolder extends RecyclerView.ViewHolder {
        private ImageView dishImage;
        private TextView dishName;
        private TextView dishDetail;
        private TextView dishPrice;
        private Button dishAddButton;

        PackageViewHolder(@NonNull View itemView) {
            super(itemView);
            dishImage = itemView.findViewById(R.id.item_Image);
            dishDetail = itemView.findViewById(R.id.item_Desc);
//            dishPrice = itemView.findViewById(R.id.item_Price);
            dishName = itemView.findViewById(R.id.item_Name);
            dishAddButton = itemView.findViewById(R.id.dis_add_button);
        }
    }
}
