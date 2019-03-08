package com.example.rapidfood.Adapters;

import android.content.Context;
import android.graphics.Point;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.example.rapidfood.R;

import androidx.annotation.NonNull;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.RecyclerView;

public class SubscriptionAdapter extends MultiSelectableAdapter<SubscriptionAdapter.SubViewHolder> {

    private Context mContext;
    public SubscriptionAdapter() {
    }

    @NonNull
    @Override
    public SubViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.subscription_tab, parent, false);
        itemView.getLayoutParams().width =(int)  (getScreenWidth() /3); /// THIS LINE WILL DIVIDE OUR VIEW INTO NUMBERS OF PARTS
        return new SubViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull SubViewHolder holder, int position) {


    }

    @Override
    public int getItemCount() {
        return 10;
    }
    public int getScreenWidth() {
        WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        return size.x;
    }
    class SubViewHolder extends RecyclerView.ViewHolder{
        TextView time, packtype, price, details;

        SubViewHolder(@NonNull final View itemView) {
            super(itemView);
            time = itemView.findViewById(R.id.texttime);
            packtype = itemView.findViewById(R.id.textpacktype);
            price = itemView.findViewById(R.id.textprice);
            details = itemView.findViewById(R.id.textdetails);
            itemView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {

                }
            });

        }


    }
}

