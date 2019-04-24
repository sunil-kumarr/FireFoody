package com.example.rapidfood.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import com.material.components.C0445R;
import com.material.components.model.Social;
import com.material.components.utils.Tools;
import com.material.components.utils.ViewAnimation;
import java.util.ArrayList;
import java.util.List;

public class AdapterListExpand extends Adapter<ViewHolder> {
    private Context ctx;
    private List<Social> items = new ArrayList();
    private OnItemClickListener mOnItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(View view, Social social, int i);
    }

    public class OriginalViewHolder extends ViewHolder {
        public ImageButton bt_expand;
        public ImageView image;
        public View lyt_expand;
        public View lyt_parent;
        public TextView name;

        public OriginalViewHolder(View view) {
            super(view);
            this.image = (ImageView) view.findViewById(C0445R.id.image);
            this.name = (TextView) view.findViewById(C0445R.id.name);
            this.bt_expand = (ImageButton) view.findViewById(C0445R.id.bt_expand);
            this.lyt_expand = view.findViewById(C0445R.id.lyt_expand);
            this.lyt_parent = view.findViewById(C0445R.id.lyt_parent);
        }
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }

    public AdapterListExpand(Context context, List<Social> list) {
        this.items = list;
        this.ctx = context;
    }

    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new OriginalViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(C0445R.layout.item_expand, viewGroup, false));
    }

    public void onBindViewHolder(ViewHolder viewHolder, final int i) {
        if (viewHolder instanceof OriginalViewHolder) {
            final OriginalViewHolder originalViewHolder = (OriginalViewHolder) viewHolder;
            final Social social = (Social) this.items.get(i);
            originalViewHolder.name.setText(social.name);
            Tools.displayImageOriginal(this.ctx, originalViewHolder.image, social.image);
            originalViewHolder.lyt_parent.setOnClickListener(new OnClickListener() {
                public void onClick(View view) {
                    if (AdapterListExpand.this.mOnItemClickListener != null) {
                        AdapterListExpand.this.mOnItemClickListener.onItemClick(view, (Social) AdapterListExpand.this.items.get(i), i);
                    }
                }
            });
            originalViewHolder.bt_expand.setOnClickListener(new OnClickListener() {
                public void onClick(View view) {
                    ((Social) AdapterListExpand.this.items.get(i)).expanded = AdapterListExpand.this.toggleLayoutExpand(social.expanded ^ 1, view, originalViewHolder.lyt_expand);
                }
            });
            if (social.expanded != 0) {
                originalViewHolder.lyt_expand.setVisibility(0);
            } else {
                originalViewHolder.lyt_expand.setVisibility(8);
            }
            Tools.toggleArrow(social.expanded, originalViewHolder.bt_expand, false);
        }
    }

    private boolean toggleLayoutExpand(boolean z, View view, View view2) {
        Tools.toggleArrow(z, view);
        if (z) {
            ViewAnimation.expand(view2);
        } else {
            ViewAnimation.collapse(view2);
        }
        return z;
    }

    public int getItemCount() {
        return this.items.size();
    }
}
