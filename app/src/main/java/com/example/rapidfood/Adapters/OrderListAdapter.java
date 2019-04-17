package com.example.rapidfood.Adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.rapidfood.Models.CheckoutPlaceOrderModel;
import com.example.rapidfood.R;
import com.example.rapidfood.Vendor_files.UserSubscriberActivity;
import com.example.rapidfood.Vendor_files.VendorShowOrderActivity;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class OrderListAdapter extends FirestoreRecyclerAdapter<CheckoutPlaceOrderModel, OrderListAdapter.SubscriberViewHolder> {

    private RecyclerView mRecyclerview;
    private Context mContext;
    private OrderListener mOrderListener;
    private static final String TAG = "OrderListAdapter";

    public OrderListAdapter(@NonNull FirestoreRecyclerOptions<CheckoutPlaceOrderModel> options,
                            RecyclerView pRecylerView, Context pContext) {
        super(options);
        Log.d(TAG, "OrderListAdapter: ");
        mContext = pContext;
        mRecyclerview = pRecylerView;
        mOrderListener = (VendorShowOrderActivity) pContext;
    }

    @Override
    protected void onBindViewHolder(@NonNull final SubscriberViewHolder pSubscriptionViewHolder,
                                    int pI, @NonNull final CheckoutPlaceOrderModel pCheckoutPlaceOrderModel) {

        Log.d(TAG, "onBindViewHolder: "+pCheckoutPlaceOrderModel.getPackageordered());
        pSubscriptionViewHolder.paymentStatus.setText(pCheckoutPlaceOrderModel.getPaymentstatus());
        pSubscriptionViewHolder.transactiontime.setText(pCheckoutPlaceOrderModel.getOrdertimestamp().toString());
        pSubscriptionViewHolder.transactionId.setText(pCheckoutPlaceOrderModel.getTrans_id());
        pSubscriptionViewHolder.userMobile.setText(pCheckoutPlaceOrderModel.getMobile());
        pSubscriptionViewHolder.mOrderName.setText(pCheckoutPlaceOrderModel.getPackageordered());
        pSubscriptionViewHolder.mOrderCost.setText(pCheckoutPlaceOrderModel.getPackageprice());
        pSubscriptionViewHolder.mOrderDeliveryAddress.setText(pCheckoutPlaceOrderModel.getDeliveryaddress());
        pSubscriptionViewHolder.mPaymentMethod.setText(pCheckoutPlaceOrderModel.getPaymentmethod());
            if (pCheckoutPlaceOrderModel.getOrderStatus().equals("SUCCESS")) {
                pSubscriptionViewHolder.confirmBTN.setEnabled(false);
                pSubscriptionViewHolder.confirmBTN.setText("Confirmed");
                Drawable img = mContext.getResources().getDrawable(R.drawable.ic_check_white_24dp);
                pSubscriptionViewHolder.confirmBTN.setCompoundDrawablesWithIntrinsicBounds(img, null, null, null);
                pSubscriptionViewHolder.confirmBTN.setBackgroundColor(mContext.getResources().getColor(R.color.green_500));
                pSubscriptionViewHolder.cancelBTN.setVisibility(View.GONE);
            } else if(pCheckoutPlaceOrderModel.getOrderStatus().equals("FAILURE")){
                pSubscriptionViewHolder.cancelBTN.setEnabled(false);
                pSubscriptionViewHolder.cancelBTN.setText("CANCELED");
                Drawable img = mContext.getResources().getDrawable(R.drawable.ic_circle_cross_24dp);
                pSubscriptionViewHolder.cancelBTN.setCompoundDrawablesWithIntrinsicBounds(img, null, null, null);
                pSubscriptionViewHolder.cancelBTN.setBackgroundColor(mContext.getResources().getColor(R.color.red_900));
                pSubscriptionViewHolder.confirmBTN.setVisibility(View.GONE);
            }
         if(pCheckoutPlaceOrderModel.getOrderStatus().equals("pending")){
            pSubscriptionViewHolder.confirmBTN.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOrderListener.onClickVerify(v, pCheckoutPlaceOrderModel);
                    pSubscriptionViewHolder.confirmBTN.setEnabled(false);
                    pSubscriptionViewHolder.confirmBTN.setText("Confirmed");
                    Drawable img = mContext.getResources().getDrawable(R.drawable.ic_check_white_24dp);
                    pSubscriptionViewHolder.confirmBTN.setCompoundDrawablesWithIntrinsicBounds(img, null, null, null);
                    pSubscriptionViewHolder.confirmBTN.setBackgroundColor(mContext.getResources().getColor(R.color.green_500));
                    pSubscriptionViewHolder.cancelBTN.setVisibility(View.GONE);
                }
            });
            pSubscriptionViewHolder.cancelBTN.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    pSubscriptionViewHolder.cancelBTN.setEnabled(false);
                    mOrderListener.onClickFailde(v, pCheckoutPlaceOrderModel);
                    pSubscriptionViewHolder.cancelBTN.setText("Canceled");
                    Drawable img = mContext.getResources().getDrawable(R.drawable.ic_circle_cross_24dp);
                    pSubscriptionViewHolder.cancelBTN.setCompoundDrawablesWithIntrinsicBounds(img, null, null, null);
                    pSubscriptionViewHolder.cancelBTN.setBackgroundColor(mContext.getResources().getColor(R.color.red_900));
                    pSubscriptionViewHolder.confirmBTN.setVisibility(View.GONE);
                }
            });
        }
    }

    @NonNull
    @Override
    public CheckoutPlaceOrderModel getItem(int position) {
        return super.getItem(position);
    }

    @Override
    public SubscriberViewHolder onCreateViewHolder(ViewGroup group, int i) {
        View view = LayoutInflater.from(group.getContext())
                .inflate(R.layout.vendor_show_order_layout, group, false);
        return new SubscriberViewHolder(view);
    }

    class SubscriberViewHolder extends RecyclerView.ViewHolder {
        private TextView mOrderName;
        private TextView mOrderCost;
        private TextView transactionId;
        private TextView transactiontime;
        private TextView userMobile;
        private TextView paymentStatus;
        private TextView mOrderDeliveryAddress;
        private TextView mOrderDetails;
        private TextView mPaymentMethod;
        private Button confirmBTN, cancelBTN;

        private SubscriberViewHolder(@NonNull View itemView) {
            super(itemView);
            confirmBTN = itemView.findViewById(R.id.order_btn_confirm);
            cancelBTN = itemView.findViewById(R.id.order_btn_cancel);
            userMobile = itemView.findViewById(R.id.order_cust_mob);
            mOrderName = itemView.findViewById(R.id.order_pack_name);
            mOrderCost = itemView.findViewById(R.id.order_cost);
            mPaymentMethod=itemView.findViewById(R.id.order_payment_method);
            transactionId = itemView.findViewById(R.id.order_trans_id);
            transactiontime = itemView.findViewById(R.id.order_Time);
            paymentStatus = itemView.findViewById(R.id.order_payment_Status);
            mOrderDeliveryAddress=itemView.findViewById(R.id.order_delivery_Address);
            mOrderDetails=itemView.findViewById(R.id.order_details);
        }
    }

    public interface OrderListener {
        void onClickVerify(View pView, CheckoutPlaceOrderModel pCheckoutPlaceOrderModel);

        void onClickFailde(View pView, CheckoutPlaceOrderModel pCheckoutPlaceOrderModel);
    }

}

