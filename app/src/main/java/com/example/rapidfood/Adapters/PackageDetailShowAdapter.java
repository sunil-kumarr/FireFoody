package com.example.rapidfood.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rapidfood.Activites.PackageDetailsActivity;
import com.example.rapidfood.Models.VendorDishModel;
import com.example.rapidfood.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class PackageDetailShowAdapter extends RecyclerView.Adapter<PackageDetailShowAdapter.PackageViewHolder> {

    private List<VendorDishModel> dishlist;
    private Context mContext;
    private List<String> selectedItems;
    private String packName;
    private int itemCount;
    private ShowButtonListener buttonListener;
    private boolean isSubscribed;

    public PackageDetailShowAdapter(List<VendorDishModel> pDishlist, Context pContext,
                                    int itemCount, String pPackName,boolean isSubscribed) {
        dishlist = pDishlist;
        mContext = pContext;
        this.itemCount = itemCount;
        packName = pPackName;
        buttonListener = (PackageDetailsActivity) mContext;
        selectedItems = new ArrayList<>();
        this.isSubscribed=isSubscribed;
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
        holder.dishDesc.setText(vModel.getDescription());
        Picasso.get()
                .load(vModel.getImage())
                .networkPolicy(NetworkPolicy.OFFLINE)
                .fit()
                .into(holder.dishImage, new Callback() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onError(Exception e) {
                        //Try again online if cache failed
                        Picasso.get()
                                .load(vModel.getImage())
                                .fit()
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
                if (itemCount > selectedItems.size()) {
                    selectedItems.add(dishlist.get(position).getName());
                    holder.dishAddedButton.setVisibility(View.VISIBLE);
                    holder.dishAddButton.setVisibility(View.GONE);
                } else {
                    Toast.makeText(mContext, "Cart is full!!", Toast.LENGTH_SHORT).show();
                }
                if(isSubscribed)
                if (selectedItems.size() == 1) {
                    buttonListener.onClickBtn(selectedItems.size());
                }
            }
        });
        holder.dishAddedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedItems.remove(dishlist.get(position).getName());
                holder.dishAddButton.setVisibility(View.VISIBLE);
                holder.dishAddedButton.setVisibility(View.GONE);
                if(isSubscribed)
                if (selectedItems.size() == 0) {
                    buttonListener.onClickBtn(selectedItems.size());
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
        private TextView dishDesc;
        private Button dishAddButton;
        private Button dishAddedButton;

        PackageViewHolder(@NonNull View itemView) {
            super(itemView);
            dishImage = itemView.findViewById(R.id.item_Image);
            dishName = itemView.findViewById(R.id.item_Name);
            dishDesc= itemView.findViewById(R.id.item_Desc);
            dishAddedButton = itemView.findViewById(R.id.dis_added_button);
            dishAddButton = itemView.findViewById(R.id.dis_add_button);
        }
    }

    public interface ShowButtonListener {
        void onClickBtn(int size);
    }
}
