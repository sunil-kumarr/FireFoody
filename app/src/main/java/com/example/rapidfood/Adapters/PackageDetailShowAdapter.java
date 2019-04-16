package com.example.rapidfood.Adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rapidfood.Models.VendorDishModel;
import com.example.rapidfood.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class PackageDetailShowAdapter extends RecyclerView.Adapter<PackageDetailShowAdapter.PackageViewHolder> {

    private List<VendorDishModel> dishlist;
    private Context mContext;
    private List<String> selectedItems;
    private String packName;
    private int itemCount;

    public PackageDetailShowAdapter(List<VendorDishModel> pDishlist, Context pContext, int itemCount, String pPackName) {
        dishlist = pDishlist;
        mContext = pContext;
        this.itemCount = itemCount;
        packName = pPackName;
        selectedItems = new ArrayList<>();
    }

    @NonNull
    @Override
    public PackageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layout = LayoutInflater.from(parent.getContext()).inflate(R.layout.vendor_dishitem_layout, parent, false);
        return new PackageViewHolder(layout);
    }

    public List<String> getSelectedItems() {
        return selectedItems;
    }

    public String getPackName() {
        return packName;
    }

    @Override
    public void onBindViewHolder(@NonNull PackageViewHolder holder, int position) {
        VendorDishModel vModel = dishlist.get(position);
        holder.dishName.setText(vModel.getName());
        holder.dishDetail.setText(vModel.getDescription());
        Picasso.get()
                .load(vModel.getImage())
                .networkPolicy(NetworkPolicy.OFFLINE)
                .into(holder.dishImage, new Callback() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onError(Exception e) {
                        //Try again online if cache failed
                        Picasso.get()
                                .load(vModel.getImage())
                                .error(R.drawable.ic_undraw_failure)
                                .into(holder.dishImage, new Callback() {
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
        holder.dishAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Button btn = (Button) v;

                if (btn.getText().equals("Add")) {
                    if (itemCount > selectedItems.size()) {

                        selectedItems.add(dishlist.get(position).getName());
                       // Toast.makeText(mContext, itemCount+""+selectedItems.size(), Toast.LENGTH_SHORT).show();
                        btn.setBackgroundColor(mContext.getResources().getColor(R.color.green_500));
                        Drawable img = mContext.getResources().getDrawable(R.drawable.ic_check_white_24dp);
                        btn.setText("Added");
                        btn.setCompoundDrawablesWithIntrinsicBounds(img, null, null, null);
                    }
                    else{
                        Toast.makeText(mContext, "Cart is full!!", Toast.LENGTH_SHORT).show();
                    }
                } else {

                    selectedItems.remove(dishlist.get(position).getName());
                    Drawable back = mContext.getResources().getDrawable(R.drawable.button_red_background);
                    btn.setBackground(back);
                    btn.setText("Add");
                    Drawable img = mContext.getResources().getDrawable(R.drawable.ic_add_white_24dp);
                    btn.setCompoundDrawablesWithIntrinsicBounds(null, null, img, null);
                }
            }
        });
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
