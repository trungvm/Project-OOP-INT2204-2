package com.example.oop_project.filters.user;

import android.widget.Filter;

import com.example.oop_project.adapters.user.AdapterEquipmentBorrowed;
import com.example.oop_project.models.ModelEquipment;

import java.util.ArrayList;

public class FilterEquipmentBorrowed extends Filter {
    ArrayList<ModelEquipment> filterList;

    AdapterEquipmentBorrowed adapterEquipmentBorrowed;

    public FilterEquipmentBorrowed(ArrayList<ModelEquipment> filterList, AdapterEquipmentBorrowed adapterEquipmentBorrowed) {
        this.filterList = filterList;
        this.adapterEquipmentBorrowed = adapterEquipmentBorrowed;
    }

    @Override
    protected FilterResults performFiltering(CharSequence constraint) {
        FilterResults results = new FilterResults();
        if (constraint != null || constraint.length() > 0) {
            constraint = constraint.toString().toUpperCase();
            ArrayList<ModelEquipment> filterCategories = new ArrayList<>();

            for (int i = 0; i < filterList.size(); i++) {
                if (filterList.get(i).getTitle().toUpperCase().contains(constraint)) {
                    filterCategories.add(filterList.get(i));
                }
            }
            results.count = filterCategories.size();
            results.values = filterCategories;
        } else {
            results.count = filterList.size();
            results.values = filterList;
        }
        return results;
    }

    @Override
    protected void publishResults(CharSequence constraint, FilterResults results) {
        adapterEquipmentBorrowed.equipmentArrayList = (ArrayList<ModelEquipment>) results.values;
        adapterEquipmentBorrowed.notifyDataSetChanged();

    }
}
