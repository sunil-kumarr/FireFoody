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

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class SelectTodayMenuAdapter extends FirestoreRecyclerAdapter<VendorDishModel, TodayViewHolder> {

    private RecyclerView mRecyclerview;
    private Context mContext;
    private List<VendorDishModel> mSelectedItems;

    public SelectTodayMenuAdapter(@NonNull FirestoreRecyclerOptions<VendorDishModel> options,
                                  RecyclerView pRecylerView, Context pContext) {
        super(options);
        mContext = pContext;
        mRecyclerview = pRecylerView;
        mSelectedItems=new ArrayList<>();
    }

    public List<VendorDishModel> getSelectedItems() {
        return mSelectedItems;
    }

    @NonNull
    @Override
    public VendorDishModel getItem(int position) {
        return super.getItem(position);
    }

    @Override
    public TodayViewHolder onCreateViewHolder(ViewGroup group, int i) {
        View view = LayoutInflater.from(group.getContext())
                .inflate(R.layout.today_menu_item_layout, group, false);
        return new TodayViewHolder(view);
    }

    @Override
    protected void onBindViewHolder(@NonNull final TodayViewHolder pTodayViewHolder,
                                    final int pI, @NonNull VendorDishModel pVendorDishModel) {

        pTodayViewHolder.mCheckBox.setText(pVendorDishModel.getName());
        pTodayViewHolder.mTextView.setText(pVendorDishModel.getMoney());
        String ty=new String();
        if(pVendorDishModel.getItemcategory()==0)ty="( B )";
        else ty="( L )";
        pTodayViewHolder.typeName.setText(ty);
        pTodayViewHolder.setOnClickListener(new TodayViewHolder.ClickListener(){
            @Override
            public void onItemClick(View view, int position) {
                        if(pTodayViewHolder.mCheckBox.isChecked()){
                                mSelectedItems.add(getItem(position));
                        }
                        else{
                            mSelectedItems.remove(getItem(position));
                        }
            }

            @Override
            public void onItemLongClick(View view, int position) {

            }
        });

    }


    @Override
    public int getItemCount() {
        return super.getItemCount();
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }
}

