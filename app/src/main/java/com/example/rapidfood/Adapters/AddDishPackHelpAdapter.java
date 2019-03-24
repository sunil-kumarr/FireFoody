package com.example.rapidfood.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.Toast;

import com.example.rapidfood.R;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;

public class AddDishPackHelpAdapter extends ArrayAdapter {
    static class viewHolder {
       CheckBox mCheckBox;
    }
    private Context mContext;
    private List<String> mSelected;
    private List<String> packs;
    public AddDishPackHelpAdapter(@NonNull Context context, int resource, List<String> packs) {
        super(context, resource);
        this.packs=packs;
        mSelected=new ArrayList<>();
        mContext=context;
    }
    public List<String> getSelectedPacks(){
        return mSelected;
    }
    @Override
    public int getCount() {
        return packs==null?0:packs.size();
    }

    @Override
    public String getItem(int index) {
        return this.packs.get(index);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View row = convertView;
        viewHolder pViewHolder;
        if (row == null) {
            LayoutInflater inflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.list_item_checkbox_style, parent, false);
            pViewHolder = new viewHolder();
            pViewHolder.mCheckBox=row.findViewById(R.id.pack_item_type);
            row.setTag(pViewHolder);
        }else{
            pViewHolder=(viewHolder)row.getTag();

        }
        pViewHolder.mCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckBox cb = (CheckBox) v;
                if(cb.isChecked()){
                    mSelected.add(getItem(position));
                    Toast.makeText(mContext, "Selected", Toast.LENGTH_SHORT).show();
                }
                else{
                    mSelected.remove(getItem(position));
                    Toast.makeText(mContext, "Removed", Toast.LENGTH_SHORT).show();
                }
            }
        });
        String s=getItem(position);
        pViewHolder.mCheckBox.setText(s);
        return row;
    }
}
