package com.example.rapidfood.Utils;

import android.content.Context;
import android.view.View;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class CenterZoomLinearLayoutManager extends LinearLayoutManager {

    private final float mShrinkAmount = 0.15f;
    private final float mShrinkDistance = 0.9f;

    private Context mContext;

    public CenterZoomLinearLayoutManager(Context context) {
        super(context);
    }

    public CenterZoomLinearLayoutManager(Context context, int orientation, boolean reverseLayout) {
        super(context, orientation, reverseLayout);
        mContext=context;

    }

    @Override
    public void onLayoutCompleted(RecyclerView.State state) {
        super.onLayoutCompleted(state);
        scaleChildren();
    }

    @Override
    public int scrollVerticallyBy(int dy, RecyclerView.Recycler recycler, RecyclerView.State state) {
        int orientation = getOrientation();
        if (orientation == VERTICAL) {
            int scrolled = super.scrollVerticallyBy(dy, recycler, state);
            float midpoint = getHeight() / 2.f;
            float d0 = 0.f;
            float d1 = mShrinkDistance * midpoint;
            float s0 = 1.f;
            float s1 = 1.f - mShrinkAmount;
            for (int i = 0; i < getChildCount(); i++) {
                View child = getChildAt(i);
                float childMidpoint =
                        (getDecoratedBottom(child) + getDecoratedTop(child)) / 2.f;
                float d = Math.min(d1, Math.abs(midpoint - childMidpoint));
                float scale = s0 + (s1 - s0) * (d - d0) / (d1 - d0);
                child.setScaleX(scale);
                child.setScaleY(scale);
            }
            return scrolled;
        } else {
            return 0;
        }
    }

    @Override
    public int scrollHorizontallyBy(int dx, RecyclerView.Recycler recycler, RecyclerView.State state) {
        int orientation = getOrientation();
        if (orientation == HORIZONTAL) {
            int scrolled = super.scrollHorizontallyBy(dx, recycler, state);
            scaleChildren();
            return scrolled;
        } else {
            return 0;
        }
    }
    private void scaleChildren(){
        float midpoint = getWidth() / 2.f;
        float d0 = 0.f;
        float d1 = mShrinkDistance * midpoint;
        float s0 = 1.f;
        float s1 = 1.f - mShrinkAmount;

        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            assert child != null;
            float childMidpoint = (getDecoratedRight(child) + getDecoratedLeft(child)) / 2.f;
//            Toast.makeText(mContext, "item:"+i+"- "+childMidpoint+" "+midpoint, Toast.LENGTH_SHORT).show();
            float d = Math.min(d1, Math.abs(midpoint - childMidpoint));
            float scale = s0 + (s1 - s0) * (d - d0) / (d1 - d0);
            child.setScaleX(scale);
            child.setScaleY(scale);
        }
    }
}