package com.example.rapidfood.Adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.rapidfood.Models.CheckoutPlaceOrderModel;
import com.example.rapidfood.R;
import com.example.rapidfood.VendorActivities.VendorShowOrderActivity;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

public class OrderListAdapter extends FirestoreRecyclerAdapter<CheckoutPlaceOrderModel, OrderListAdapter.ORDERLISTViewHolder> {

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
    protected void onBindViewHolder(@NonNull final ORDERLISTViewHolder pSubscriptionViewHolder,
                                    int pI, @NonNull final CheckoutPlaceOrderModel pCheckoutPlaceOrderModel) {

        Log.d(TAG, "onBindViewHolder: " + pCheckoutPlaceOrderModel.getPackageordered());
        pSubscriptionViewHolder.paymentStatus.setText(pCheckoutPlaceOrderModel.getPaymentstatus());
        pSubscriptionViewHolder.transactiontime.setText(pCheckoutPlaceOrderModel.getOrdertimestamp().toString());
        pSubscriptionViewHolder.transactionId.setText(pCheckoutPlaceOrderModel.getTrans_id());
        pSubscriptionViewHolder.userMobile.setText(pCheckoutPlaceOrderModel.getMobile());
        pSubscriptionViewHolder.mOrderName.setText(pCheckoutPlaceOrderModel.getPackageordered());
        pSubscriptionViewHolder.mOrderCost.setText(pCheckoutPlaceOrderModel.getPackageprice());
        pSubscriptionViewHolder.mOrderDeliveryAddress.setText(pCheckoutPlaceOrderModel.getDeliveryaddress());
        pSubscriptionViewHolder.mPaymentMethod.setText(pCheckoutPlaceOrderModel.getPaymentmethod());
        pSubscriptionViewHolder.orderConfirmStatus.setText(pCheckoutPlaceOrderModel.getOrderStatus());
        if(pCheckoutPlaceOrderModel.isCustom()) {
            pSubscriptionViewHolder.mOrderDetails.setText(String.valueOf(pCheckoutPlaceOrderModel.getSelecteditems()));
        }
        else{
            pSubscriptionViewHolder.mOrderDetails.setText("Default Order");
        }
        if (!pCheckoutPlaceOrderModel.getOrderStatus().equals("pending")) {
            pSubscriptionViewHolder.confirmBTN.setVisibility(View.GONE);
        } else {
            pSubscriptionViewHolder.confirmBTN.setVisibility(View.VISIBLE);
            pSubscriptionViewHolder.confirmBTN.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog vDialog = new AlertDialog.Builder(mContext)
                            .setTitle("CONFIRM ORDER")
                            .setIcon(R.drawable.ic_check_circlce_button)
                            .setMessage("Are you sure you want to confirm order?")
                            .setPositiveButton("CONFIRM ORDER", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    pSubscriptionViewHolder.confirmBTN.setVisibility(View.GONE);
                                    mOrderListener.onClickVerify(v, pCheckoutPlaceOrderModel);
                                }
                            })
                            .setNegativeButton("CANCEL ORDER", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    pSubscriptionViewHolder.confirmBTN.setVisibility(View.GONE);
                                    mOrderListener.onClickFailed(v, pCheckoutPlaceOrderModel);
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
    public CheckoutPlaceOrderModel getItem(int position) {
        return super.getItem(position);
    }

    @Override
    public ORDERLISTViewHolder onCreateViewHolder(ViewGroup group, int i) {
        View view = LayoutInflater.from(group.getContext())
                .inflate(R.layout.vendor_show_order_layout, group, false);
        return new ORDERLISTViewHolder(view);
    }

    class ORDERLISTViewHolder extends RecyclerView.ViewHolder {
        private TextView mOrderName;
        private TextView mOrderCost;
        private TextView transactionId;
        private TextView transactiontime;
        private TextView userMobile;
        private TextView paymentStatus;
        private TextView mOrderDeliveryAddress;
        private TextView mOrderDetails;
        private TextView mPaymentMethod;
        private Button confirmBTN;
        private TextView orderConfirmStatus;

        private ORDERLISTViewHolder(@NonNull View itemView) {
            super(itemView);
            confirmBTN = itemView.findViewById(R.id.order_btn_confirm);
            orderConfirmStatus = itemView.findViewById(R.id.order_confirm_Status);
            userMobile = itemView.findViewById(R.id.order_cust_mob);
            mOrderName = itemView.findViewById(R.id.order_pack_name);
            mOrderCost = itemView.findViewById(R.id.order_cost);
            mPaymentMethod = itemView.findViewById(R.id.order_payment_method);
            transactionId = itemView.findViewById(R.id.order_trans_id);
            transactiontime = itemView.findViewById(R.id.order_Time);
            paymentStatus = itemView.findViewById(R.id.order_payment_Status);
            mOrderDeliveryAddress = itemView.findViewById(R.id.order_delivery_Address);
            mOrderDetails = itemView.findViewById(R.id.order_details);
        }
    }

    public interface OrderListener {
        void onClickVerify(View pView, CheckoutPlaceOrderModel pCheckoutPlaceOrderModel);

        void onClickFailed(View pView, CheckoutPlaceOrderModel pCheckoutPlaceOrderModel);
    }

}

