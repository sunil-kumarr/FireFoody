package com.firefoody.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firefoody.Models.AddressModel;
import com.firefoody.R;
import com.google.android.material.card.MaterialCardView;

import java.util.ArrayList;

public class AddressAdapter extends RecyclerView.Adapter {
    private ArrayList<AddressModel> myList ;
    private Context context;
     AddressAdapter(ArrayList<AddressModel> myList, Context context) {
       this.myList = myList;
       this.context = context;
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
         View myView;
        if (viewType == AddressModel.TYPE_ADDRESS) {
            myView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_address, parent, false);
            return new MyViewholder(myView);
        }
        myView = LayoutInflater.from(parent.getContext()).inflate(R.layout.add_new_address, parent, false);
        return new AddMoreHolder(myView);
    }

    @Override
    public int getItemViewType(int position) {
        switch (myList.get(position).getType())
        {
            case 0:return AddressModel.TYPE_ADDRESS;
            case 1:return AddressModel.TYPE_ADD;
            default: return -1;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        AddressModel item = myList.get(position);
        if(AddressModel.TYPE_ADDRESS == item.getType()) {
////            ((MyViewholder) holder).CustomerName
////            ((MyViewholder) holder).listBuyDate.setText(item.getItemPrice().toString());
//            final int cardColor = ((MyViewholder)holder).materialCardView.getCardBackgroundColor().getDefaultColor();
//            ((MyViewholder)holder).materialCardView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
////                    Intent intent = new Intent(context,);
////                    intent.putExtra("cardcolor",cardColor);
////                    context.startActivity(intent);
//                }
//            });
        }
        else{
            ((AddMoreHolder)holder).materialCardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    context.startActivity(new Intent(context,BucketDetails.class));
                }
            });
        }
    }
    @Override
    public int getItemCount() {
        return myList.size();
    }

    public  class MyViewholder extends RecyclerView.ViewHolder {
         TextView CustomerName;
         TextView CustomerAddress;
         MaterialCardView materialCardView;

          MyViewholder(@NonNull View itemView) {
            super(itemView);
            CustomerName = itemView.findViewById(R.id.customerName);
            CustomerAddress = itemView.findViewById(R.id.customerAddress);
            materialCardView = itemView.findViewById(R.id.itemListCardView);
        }
    }
    public  class AddMoreHolder extends RecyclerView.ViewHolder{
         ImageView add;
         TextView description;
         MaterialCardView materialCardView;

         AddMoreHolder(@NonNull View itemView) {
            super(itemView);
            add = itemView.findViewById(R.id.addmore);
            description = itemView.findViewById(R.id.add_desc);
            materialCardView = itemView.findViewById(R.id.listTabAdd);
        }
    }
}
