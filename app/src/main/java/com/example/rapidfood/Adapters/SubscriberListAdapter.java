package com.example.rapidfood.Adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.rapidfood.Models.SubscriptionTransactionModel;
import com.example.rapidfood.R;
import com.example.rapidfood.VendorActivities.UserSubscriberActivity;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import static android.view.View.GONE;

public class SubscriberListAdapter extends FirestoreRecyclerAdapter<SubscriptionTransactionModel, SubscriberListAdapter.SubscriberViewHolder> {

    private RecyclerView mRecyclerview;
    private Context mContext;
    private SubscriberListener mSubscriberListener;

    public SubscriberListAdapter(@NonNull FirestoreRecyclerOptions<SubscriptionTransactionModel> options,
                                 RecyclerView pRecylerView, Context pContext) {
        super(options);
        mContext = pContext;
        mRecyclerview = pRecylerView;
        mSubscriberListener = (UserSubscriberActivity) pContext;
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
        pSubscriptionViewHolder.verificationStatus.setText(pSubscriptionModel.getVerification_status());
        if(!pSubscriptionModel.getVerification_status().equals("pending")){
            pSubscriptionViewHolder.verifyBTN.setVisibility(GONE);
        }
        else {
            pSubscriptionViewHolder.verifyBTN.setVisibility(View.VISIBLE);
            pSubscriptionViewHolder.verifyBTN.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog vDialog = new AlertDialog.Builder(mContext)
                            .setTitle("CANCEL SUBSCRIPTION")
                            .setIcon(R.drawable.ic_cancel_red_24dp)
                            .setMessage("Are you sure you have verified the payment is failed for this subscription??")
                            .setPositiveButton("CONFIRM", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    pSubscriptionViewHolder.verifyBTN.setVisibility(GONE);
                                    mSubscriberListener.onClickVerify(v, pSubscriptionModel);
                                }
                            })
                            .setNegativeButton("FAILED", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    pSubscriptionViewHolder.verifyBTN.setVisibility(GONE);
                                    mSubscriberListener.onClickFailed(v, pSubscriptionModel);
                                }
                            })
                            .create();
                    vDialog.show();
                }
            });
        }
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
        private TextView verificationStatus;
        private Button verifyBTN;

        private SubscriberViewHolder(@NonNull View itemView) {
            super(itemView);
            verifyBTN = itemView.findViewById(R.id.subscriber_btn_verify);
            userMobile = itemView.findViewById(R.id.subscriber_mobile);
            mSubName = itemView.findViewById(R.id.subscriber_type);
            mSubCost = itemView.findViewById(R.id.subscriber_cost);
            mSubCoupon = itemView.findViewById(R.id.subscriber_coupon);
            mSubTotalCost = itemView.findViewById(R.id.subscriber_total);
            transactionId = itemView.findViewById(R.id.subscriber_trans_id);
            transactiontime = itemView.findViewById(R.id.subscriber_date);
            googleStatus = itemView.findViewById(R.id.subscriber_google_Status);
            verificationStatus = itemView.findViewById(R.id.subscriber_verification_Status);
        }

    }

    public interface SubscriberListener {
        void onClickVerify(View pView, SubscriptionTransactionModel pSubscriptionTransactionModel);

        void onClickFailed(View pView, SubscriptionTransactionModel pSubscriptionTransactionModel);
    }

}

