package com.example.oop_project.filters;

import android.widget.Filter;

import com.example.oop_project.adapters.AdapterCategory;
import com.example.oop_project.adapters.AdapterEquipmentAdmin;
import com.example.oop_project.models.ModelCategory;
import com.example.oop_project.models.ModelEquipment;

import java.util.ArrayList;

public class FilterEquipment extends Filter {
    ArrayList<ModelEquipment> filterList;
    AdapterEquipmentAdmin adapterEquipmentAdmin;

    public FilterEquipment(ArrayList<ModelEquipment> filterList, AdapterEquipmentAdmin adapterEquipmentAdmin) {
        this.filterList = filterList;
        this.adapterEquipmentAdmin = adapterEquipmentAdmin;
    }

    @Override
    protected FilterResults performFiltering(CharSequence constraint) {
        FilterResults results = new FilterResults();
        if(constraint != null && constraint.length() > 0){
            // change to uppercase, or lower case

            constraint = constraint.toString().toUpperCase();
            ArrayList<ModelEquipment> filterCategories = new ArrayList<>();

            for(int i = 0; i < filterList.size(); i++){
                if(filterList.get(i).getTitle().toUpperCase().contains(constraint)){
                    filterCategories.add(filterList.get(i));
                }
            }
            results.count = filterCategories.size();
            results.values = filterCategories;
        }else{
            results.count = filterList.size();
            results.values = filterList;
        }
        return results;
    }

    @Override
    protected void publishResults(CharSequence constraint, FilterResults results) {
        adapterEquipmentAdmin.equipmentArraylist= (ArrayList<ModelEquipment>)results.values;

        adapterEquipmentAdmin.notifyDataSetChanged();
    }
}
