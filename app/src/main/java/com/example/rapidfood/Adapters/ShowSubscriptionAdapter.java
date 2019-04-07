package com.example.rapidfood.Adapters;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.rapidfood.GooglePay.CheckoutActivity;
import com.example.rapidfood.Models.SubscriptionModel;
import com.example.rapidfood.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.squareup.picasso.Picasso;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ShowSubscriptionAdapter extends FirestoreRecyclerAdapter<SubscriptionModel, ShowSubscriptionAdapter.SubscriptionViewHolder> {

    private RecyclerView mRecyclerview;
    private Context mContext;

    public ShowSubscriptionAdapter(@NonNull FirestoreRecyclerOptions<SubscriptionModel> options,
                               RecyclerView pRecylerView,Context pContext) {
        super(options);
        mContext=pContext;
        mRecyclerview=pRecylerView;

    }


    @Override
    protected void onBindViewHolder(@NonNull final SubscriptionViewHolder pSubscriptionViewHolder,
                                    int pI, @NonNull final SubscriptionModel pSubscriptionModel) {
        Picasso.get()
                .load(pSubscriptionModel.getImagesub())
                .into(pSubscriptionViewHolder.mSubImage);
        pSubscriptionViewHolder.mSubImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(mContext, CheckoutActivity.class);
                Bundle data=new Bundle();
                data.putString("sub_detail",pSubscriptionModel.getDetails());
                data.putString("sub_price",pSubscriptionModel.getPrice());
                data.putString("sub_name",pSubscriptionModel.getType());
                data.putString("sub_image_link",pSubscriptionModel.getImagesub());
                data.putString("sub_duration",pSubscriptionModel.getDuration());
                i.putExtras(data);
               mContext.startActivity(i);
            }
        });
    }

    @Override
    public SubscriptionViewHolder onCreateViewHolder(ViewGroup group, int i) {
        View view = LayoutInflater.from(group.getContext())
                .inflate(R.layout.subscription_image, group, false);
        int width = mRecyclerview.getWidth();
        ViewGroup.LayoutParams params = view.getLayoutParams();
        params.width = (int) (width * 0.8);
        view.setLayoutParams(params);
        return new SubscriptionViewHolder(view);
    }

    class SubscriptionViewHolder extends RecyclerView.ViewHolder {
        private ImageView mSubImage;
        SubscriptionViewHolder(View itemView) {
            super(itemView);
            mSubImage= itemView.findViewById(R.id.sub_image);

        }
//        void showBottomSheetDialog() {
//            View view = LayoutInflater.from(mContext).inflate(R.layout.subscription_payment_bottom_sheet,null);
//            BottomSheetDialog dialog = new BottomSheetDialog(mContext);
//            dialog.setContentView(view);
//            dialog.show();
//        }
    }

}

