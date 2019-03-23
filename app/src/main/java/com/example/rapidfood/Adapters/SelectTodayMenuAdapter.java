package com.example.rapidfood.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.Toast;

import com.example.rapidfood.Models.VendorBreakFastItem;
import com.example.rapidfood.Models.VendorDishModel;
import com.example.rapidfood.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class SelectTodayMenuAdapter extends FirestoreRecyclerAdapter<VendorDishModel, SelectTodayMenuAdapter.TodayViewHolder> {

    private RecyclerView mRecyclerview;
    private Context mContext;

    public SelectTodayMenuAdapter(@NonNull FirestoreRecyclerOptions<VendorDishModel> options,
                                   RecyclerView pRecylerView,Context pContext) {
        super(options);
        mContext=pContext;
        mRecyclerview=pRecylerView;
        Toast.makeText(pContext, ""+options.getSnapshots().size(), Toast.LENGTH_SHORT).show();

    }


    @Override
    protected void onBindViewHolder(@NonNull TodayViewHolder pTodayViewHolder,
                                    int pI, @NonNull VendorDishModel pVendorBreakFastItem) {
        Toast.makeText(mContext, ""+pVendorBreakFastItem.getName(), Toast.LENGTH_SHORT).show();
       pTodayViewHolder.mCheckBox.setText(pVendorBreakFastItem.getName());
       //pTodayViewHolder.mCheckBox.getResources().getColor(R.color.black);
    }

    @Override
    public TodayViewHolder onCreateViewHolder(ViewGroup group, int i) {
        View view = LayoutInflater.from(group.getContext())
                .inflate(R.layout.today_menu_item_layout, group, false);
        return new TodayViewHolder(view);
    }

    @Override
    public int getItemCount() {
        return super.getItemCount();
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    class TodayViewHolder extends RecyclerView.ViewHolder {
        CheckBox mCheckBox;

        TodayViewHolder(@NonNull View itemView) {
            super(itemView);
            mCheckBox = itemView.findViewById(R.id.pack_item_type);
        }
    }


}

