package com.example.rapidfood.Adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rapidfood.Activites.GooglePayActivity;
import com.example.rapidfood.Models.SubscriptionModel;
import com.example.rapidfood.Models.SubscriptionTransactionModel;
import com.example.rapidfood.R;
import com.example.rapidfood.Vendor_files.UserSubscriberActivity;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class SubscriberListAdapter extends FirestoreRecyclerAdapter<SubscriptionTransactionModel, SubscriberListAdapter.SubscriberViewHolder> {

    private RecyclerView mRecyclerview;
    private Context mContext;
    private SubscriberListener mSubscriberListener;

    public SubscriberListAdapter(@NonNull FirestoreRecyclerOptions<SubscriptionTransactionModel> options,
                                 RecyclerView pRecylerView, Context pContext) {
        super(options);
        mContext = pContext;
        mRecyclerview = pRecylerView;
        mSubscriberListener=(UserSubscriberActivity) pContext;
    }

    @Override
    protected void onBindViewHolder(@NonNull final SubscriberViewHolder pSubscriptionViewHolder,
                                    int pI, @NonNull final SubscriptionTransactionModel pSubscriptionModel) {

        pSubscriptionViewHolder.googleStatus.setText(pSubscriptionModel.getGooglePay_status());
        pSubscriptionViewHolder.transactiontime.setText(pSubscriptionModel.getTransaction_time().toString());
        pSubscriptionViewHolder.transactionId.setText(pSubscriptionModel.getTransaction_id());
        pSubscriptionViewHolder.userMobile.setText(pSubscriptionModel.getMobile());
        pSubscriptionViewHolder.mSubName.setText(pSubscriptionModel.getSubname());
        pSubscriptionViewHolder.mSubCost.setText(pSubscriptionModel.getSubcost());
        pSubscriptionViewHolder.mSubCoupon.setText(pSubscriptionModel.getSubcoupon());
        pSubscriptionViewHolder.mSubTotalCost.setText(pSubscriptionModel.getTotal_paid());
      //  Toast.makeText(mContext, ""+pSubscriptionModel.getTransaction_time(), Toast.LENGTH_SHORT).show();
        pSubscriptionViewHolder.verifyBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSubscriberListener.onClickVerify(v,pSubscriptionModel);
                pSubscriptionViewHolder.verifyBTN.setEnabled(false);
                pSubscriptionViewHolder.verifyBTN.setText("Verified");
                Drawable img = mContext.getResources().getDrawable(R.drawable.ic_check_white_24dp);
//                Bitmap bitmap = ((BitmapDrawable) dr).getBitmap();
//                Drawable img = new BitmapDrawable(mContext.getResources(), Bitmap.createScaledBitmap(bitmap, 30, 30, true));
                pSubscriptionViewHolder.verifyBTN.setCompoundDrawablesWithIntrinsicBounds( img, null, null, null);
                pSubscriptionViewHolder.verifyBTN.setBackgroundColor(mContext.getResources().getColor(R.color.green_500));
                pSubscriptionViewHolder.failedBTN.setVisibility(View.GONE);
            }
        });
        pSubscriptionViewHolder.failedBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pSubscriptionViewHolder.failedBTN.setEnabled(false);
                mSubscriberListener.onClickVerify(v,pSubscriptionModel);
                Drawable img = mContext.getResources().getDrawable(R.drawable.ic_circle_cross_24dp);
//                Bitmap bitmap = ((BitmapDrawable) dr).getBitmap();
//                Drawable img = new BitmapDrawable(mContext.getResources(), Bitmap.createScaledBitmap(bitmap, 30, 30, true));
                pSubscriptionViewHolder.failedBTN.setCompoundDrawablesWithIntrinsicBounds( img, null, null, null);
                pSubscriptionViewHolder.failedBTN.setBackgroundColor(mContext.getResources().getColor(R.color.red_900));
                pSubscriptionViewHolder.verifyBTN.setVisibility(View.GONE);
            }
        });
    }

    @NonNull
    @Override
    public SubscriptionTransactionModel getItem(int position) {
        return super.getItem(position);
    }

    @Override
    public SubscriberViewHolder onCreateViewHolder(ViewGroup group, int i) {
        View view = LayoutInflater.from(group.getContext())
                .inflate(R.layout.subscribed_user_list_item, group, false);
        return new SubscriberListAdapter.SubscriberViewHolder(view);
    }

    class SubscriberViewHolder extends RecyclerView.ViewHolder {
        private TextView mSubName;
        private TextView mSubCost;
        private TextView mSubCoupon;
        private TextView mSubTotalCost;
        private TextView transactionId;
        private TextView transactiontime;
        private TextView userMobile;
        private TextView googleStatus;
        private Button verifyBTN,failedBTN;

        private SubscriberViewHolder(@NonNull View itemView) {
            super(itemView);
            verifyBTN=itemView.findViewById(R.id.subscriber_btn_verify);
            failedBTN=itemView.findViewById(R.id.subscriber_btn_failed);
            userMobile=itemView.findViewById(R.id.subscriber_mobile);
            mSubName = itemView.findViewById(R.id.subscriber_type);
            mSubCost = itemView.findViewById(R.id.subscriber_cost);
            mSubCoupon = itemView.findViewById(R.id.subscriber_coupon);
            mSubTotalCost = itemView.findViewById(R.id.subscriber_total);
            transactionId = itemView.findViewById(R.id.subscriber_trans_id);
            transactiontime = itemView.findViewById(R.id.subscriber_date);
            googleStatus = itemView.findViewById(R.id.subscriber_google_Status);
        }

    }
   public interface SubscriberListener{
        void onClickVerify(View pView,SubscriptionTransactionModel pSubscriptionTransactionModel);
    }

}

