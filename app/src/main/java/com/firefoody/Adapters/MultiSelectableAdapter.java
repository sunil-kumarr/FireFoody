package com.firefoody.Adapters;


import android.util.SparseBooleanArray;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

public abstract  class MultiSelectableAdapter<VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH> {

   private SparseBooleanArray mSelectedItems;

   MultiSelectableAdapter(){
       mSelectedItems=new SparseBooleanArray();
   }

   boolean isSelected(int position){
       return  getSelectedItems().contains(position);
   }

    public int getSelectedItemCount() {
        return mSelectedItems.size();
    }

    public List<Integer> getSelectedItems(){
       List<Integer> items=new ArrayList<Integer>(mSelectedItems.size());
      for(int i=0;i<mSelectedItems.size();i++){
          items.add(mSelectedItems.keyAt(i));
      }
      return items;
   }


   public void  clearSelection(){
       List<Integer> items=getSelectedItems();
       mSelectedItems.clear();
       for (Integer i:items){
           notifyItemChanged(i);
       }
   }

   public  void toggleSelection(int position){
       if(mSelectedItems.get(position,false)){
           mSelectedItems.delete(position);
       }
       else{
           mSelectedItems.put(position,true);
       }
       notifyItemChanged(position);
   }

}
